package kr.co.onmediagroup.template.service;

import kr.co.onmediagroup.template.exception.TemplateException;
import kr.co.onmediagroup.template.model.dto.BaseTemplate;
import kr.co.onmediagroup.template.model.dto.Post;
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
    List<Post.PostBlockReq> reqList
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
}
