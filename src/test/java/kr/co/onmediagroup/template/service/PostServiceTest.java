package kr.co.onmediagroup.template.service;

import kr.co.onmediagroup.template.exception.PostException;
import kr.co.onmediagroup.template.model.dto.Post;
import kr.co.onmediagroup.template.model.entity.PostEntity;
import kr.co.onmediagroup.template.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("게시물 활성화 상태 변경 성공")
    void changeActive_Success() {
        // Given
        String postId = "post1";
        String userId = "user1";
        Post.ActiveYn activeYn = Post.ActiveYn.Y;

        PostEntity post = PostEntity.builder()
                .postId(postId)
                .userId(userId)
                .activeYn(Post.ActiveYn.N)
                .build();

        when(postRepository.findByPostIdAndDeletedAtIsNull(postId)).thenReturn(Optional.of(post));

        // When
        postService.changeActive(postId, userId, activeYn);

        // Then
        assertEquals(activeYn, post.getActiveYn());
    }

    @Test
    @DisplayName("게시물이 존재하지 않는 경우 실패")
    void changeActive_Fail_NoPost() {
        // Given
        String postId = "nonexistent";
        String userId = "user1";
        Post.ActiveYn activeYn = Post.ActiveYn.Y;

        when(postRepository.findByPostIdAndDeletedAtIsNull(postId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PostException.NoPost.class, () -> postService.changeActive(postId, userId, activeYn));
    }

    @Test
    @DisplayName("소유자가 아닌 경우 실패")
    void changeActive_Fail_Unauthorized() {
        // Given
        String postId = "post1";
        String userId = "user1";
        String otherUserId = "user2";
        Post.ActiveYn activeYn = Post.ActiveYn.Y;

        PostEntity post = PostEntity.builder()
                .postId(postId)
                .userId(otherUserId)
                .activeYn(Post.ActiveYn.N)
                .build();

        when(postRepository.findByPostIdAndDeletedAtIsNull(postId)).thenReturn(Optional.of(post));

        // When & Then
        assertThrows(PostException.UnauthorizedPostAccess.class, () -> postService.changeActive(postId, userId, activeYn));
    }

    // template test
}
