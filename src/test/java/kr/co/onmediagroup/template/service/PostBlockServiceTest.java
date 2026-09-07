package kr.co.onmediagroup.template.service;

import kr.co.onmediagroup.template.exception.PostBlockException;
import kr.co.onmediagroup.template.exception.PostException;
import kr.co.onmediagroup.template.model.dto.PostBlock;
import kr.co.onmediagroup.template.model.entity.PostBlockEntity;
import kr.co.onmediagroup.template.model.entity.PostEntity;
import kr.co.onmediagroup.template.repository.PostBlockRepository;
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
class PostBlockServiceTest {

    @Mock
    private PostBlockRepository postBlockRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostBlockService postBlockService;

    @Test
    @DisplayName("게시물 블록 상세 조회 성공")
    void findById_Success() {
        // Given
        String userId = "testUser";
        Integer postBlockId = 1;
        String postId = "testPost";

        PostBlockEntity blockEntity = PostBlockEntity.builder()
                .postBlockId(postBlockId)
                .postId(postId)
                .build();

        PostEntity postEntity = PostEntity.builder()
                .postId(postId)
                .userId(userId)
                .build();

        when(postBlockRepository.findById(postBlockId)).thenReturn(Optional.of(blockEntity));
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        // When
        PostBlock.PostBlockDetailDetailRes result = postBlockService.findById(userId, postBlockId);

        // Then
        assertNotNull(result);
        assertEquals(postBlockId, result.getPostBlockId());
        assertEquals(postId, result.getPostId());
    }

    @Test
    @DisplayName("게시물 블록 상세 조회 실패 - 데이터 없음")
    void findById_NoData() {
        // Given
        Integer postBlockId = 99;
        when(postBlockRepository.findById(postBlockId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PostBlockException.NoPostBlock.class, () -> postBlockService.findById("user", postBlockId));
    }

    @Test
    @DisplayName("게시물 블록 상세 조회 실패 - 권한 없음")
    void findById_Unauthorized() {
        // Given
        String userId = "owner";
        String otherUserId = "other";
        Integer postBlockId = 1;
        String postId = "testPost";

        PostBlockEntity blockEntity = PostBlockEntity.builder()
                .postBlockId(postBlockId)
                .postId(postId)
                .build();

        PostEntity postEntity = PostEntity.builder()
                .postId(postId)
                .userId(userId)
                .build();

        when(postBlockRepository.findById(postBlockId)).thenReturn(Optional.of(blockEntity));
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        // When & Then
        assertThrows(PostException.UnauthorizedPostAccess.class, () -> postBlockService.findById(otherUserId, postBlockId));
    }
}
