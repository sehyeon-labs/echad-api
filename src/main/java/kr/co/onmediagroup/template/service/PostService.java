package kr.co.onmediagroup.template.service;

import kr.co.onmediagroup.template.exception.PostException;
import kr.co.onmediagroup.template.model.dto.BaseTemplate;
import kr.co.onmediagroup.template.model.dto.Post;
import kr.co.onmediagroup.template.model.dto.PostBlock;
import kr.co.onmediagroup.template.model.entity.PostEntity;
import kr.co.onmediagroup.template.model.entity.PostBlockEntity;
import kr.co.onmediagroup.template.repository.PostRepository;
import kr.co.onmediagroup.template.repository.PostBlockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.co.onmediagroup.util.ModelConverter.MODEL_MAPPER;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostBlockRepository postBlockRepository;

    public Post.PostRes findByPostId(String postId, String userId) {
        PostEntity post = postRepository.findByPostIdAndUserIdAndDeletedAtIsNull(postId, userId)
                .orElseThrow(PostException.NoTemplate::new);

        List<PostBlockEntity> blocks = postBlockRepository.findAllByPostIdWithDetails(postId, BaseTemplate.ActiveYn.Y);

        Post.PostRes res = MODEL_MAPPER.map(post, Post.PostRes.class);

        List<PostBlock.PostBlockRes> content = blocks.stream()
                .map(block -> {
                    PostBlock.PostBlockRes pbr = new PostBlock.PostBlockRes();
                    pbr.setPostBlockId(block.getPostBlockId());
                    pbr.setSortOrder(block.getSortOrder());
                    pbr.setDetail(MODEL_MAPPER.map(block, PostBlock.PostBlockDetailRes.class));
                    return pbr;
                })
                .toList();

        res.setContent(content);
        return res;
    }
}
