package kr.co.onmediagroup.template.service;

import kr.co.onmediagroup.template.exception.PostBlockException;
import kr.co.onmediagroup.template.exception.PostException;
import kr.co.onmediagroup.template.exception.TemplateException;
import kr.co.onmediagroup.template.model.dto.BaseTemplate;
import kr.co.onmediagroup.template.model.dto.Post;
import kr.co.onmediagroup.template.model.dto.PostBlock;
import kr.co.onmediagroup.template.model.entity.PostEntity;
import kr.co.onmediagroup.template.model.entity.PostBlockEntity;
import kr.co.onmediagroup.template.repository.PostRepository;
import kr.co.onmediagroup.template.repository.PostBlockRepository;
import kr.co.onmediagroup.template.repository.BaseTemplateRepository;
import kr.co.onmediagroup.util.ModelConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostBlockService {
  private final PostBlockRepository postBlockRepository;
  private final BaseTemplateRepository baseTemplateRepository;
  private final PostRepository postRepository;

  @Transactional(readOnly = true)
  public PostBlock.PostBlockDetailDetailRes findById(String userId, Integer postBlockId) {
    PostBlockEntity block = postBlockRepository.findById(postBlockId)
            .orElseThrow(PostBlockException.NoPostBlock::new);

    // 권한 검사
    validatePostOwnership(userId, block.getPostId());

    return ModelConverter.MODEL_MAPPER.map(block, PostBlock.PostBlockDetailDetailRes.class);
  }

  public void saveBulk(
    String userId,
    String postId,
    List<PostBlock.PostBlockReq> reqList
  ) {
    PostEntity post = postRepository.findById(postId)
            .orElseGet(() -> postRepository.save(PostEntity.builder()
                    .postId(postId)
                    .userId(userId)
                    .activeYn(Post.ActiveYn.N)
                    .build()));

    List<PostBlockEntity> blocks = reqList.stream()
      .map(req -> {
        boolean existTemplate = this.baseTemplateRepository.existsByTemplateIdAndActiveYnAndDeletedAtIsNull(req.templateId(), BaseTemplate.ActiveYn.Y);
        if (!existTemplate) {
          throw new TemplateException.NoTemplate();
        }

        return PostBlockEntity.builder()
          .postId(postId)
          .templateId(req.templateId())
          .sortOrder(req.sortOrder())
          .customSchema(req.customSchema())
          .build();
      })
      .toList();

    this.postBlockRepository.saveAll(blocks);
  }

  // 게시물 블록의 순서를 일괄 업데이트
  public void updateOrders(
    String userId,
    String postId,
    List<PostBlock.PostBlockOrderReq> reqList
  ) {
    // 소유권 검사
    validatePostOwnership(userId, postId);

    reqList.forEach(req -> {
      PostBlockEntity block = postBlockRepository.findByPostBlockIdAndPostId(req.postBlockId(), postId)
              .orElseThrow(PostBlockException.NoPostBlock::new);
      block.updateSortOrder(req.sortOrder());
    });
  }

  // 두 게시물 블록의 순서를 서로 교체
  public void swapOrders(
    String userId,
    String postId,
    Integer postBlockId1,
    Integer postBlockId2
  ) {
    // 소유권 검사
    validatePostOwnership(userId, postId);

    PostBlockEntity block1 = postBlockRepository.findByPostBlockIdAndPostId(postBlockId1, postId)
            .orElseThrow(PostBlockException.NoPostBlock::new);
    PostBlockEntity block2 = postBlockRepository.findByPostBlockIdAndPostId(postBlockId2, postId)
            .orElseThrow(PostBlockException.NoPostBlock::new);

    Integer tempOrder = block1.getSortOrder();
    block1.updateSortOrder(block2.getSortOrder());
    block2.updateSortOrder(tempOrder);
  }

  // 게시물의 소유권을 검증
  private void validatePostOwnership(String userId, String postId) {
    PostEntity post = postRepository.findById(postId)
            .orElseThrow(PostException.NoPost::new);

    if (!post.getUserId().equals(userId)) {
      throw new PostException.UnauthorizedPostAccess();
    }
  }
}
