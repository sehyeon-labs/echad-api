package kr.co.onmediagroup.template.service;

import kr.co.onmediagroup.exception.ForbiddenException;
import kr.co.onmediagroup.template.exception.PostException;
import kr.co.onmediagroup.template.model.dto.BaseTemplate;
import kr.co.onmediagroup.template.model.dto.Post;
import kr.co.onmediagroup.template.model.dto.PostBlock;
import kr.co.onmediagroup.template.model.entity.PostEntity;
import kr.co.onmediagroup.template.model.entity.PostBlockEntity;
import kr.co.onmediagroup.template.repository.PostRepository;
import kr.co.onmediagroup.template.repository.PostBlockRepository;
import kr.co.onmediagroup.template.repository.BaseTemplateRepository;
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
          throw new PostException.NoTemplate();
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

  public void updateOrders(String userId, String postId, List<PostBlock.PostBlockOrderReq> reqList) {
    validatePostOwnership(userId, postId);

    reqList.forEach(req -> {
      PostBlockEntity block = postBlockRepository.findByPostBlockIdAndPostId(req.postBlockId(), postId)
              .orElseThrow(() -> new IllegalArgumentException("block not found for the post"));
      block.updateSortOrder(req.sortOrder());
    });
  }

  public void swapOrders(String userId, String postId, PostBlock.PostBlockSwapReq swapReq) {
    validatePostOwnership(userId, postId);

    PostBlockEntity block1 = postBlockRepository.findByPostBlockIdAndPostId(swapReq.postBlockId1(), postId)
            .orElseThrow(() -> new IllegalArgumentException("block1 not found for the post"));
    PostBlockEntity block2 = postBlockRepository.findByPostBlockIdAndPostId(swapReq.postBlockId2(), postId)
            .orElseThrow(() -> new IllegalArgumentException("block2 not found for the post"));

    Integer tempOrder = block1.getSortOrder();
    block1.updateSortOrder(block2.getSortOrder());
    block2.updateSortOrder(tempOrder);
  }

  private void validatePostOwnership(String userId, String postId) {
    PostEntity post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("post not found"));

    if (!post.getUserId().equals(userId)) {
      throw new ForbiddenException("No permission to modify this post");
    }
  }
}
