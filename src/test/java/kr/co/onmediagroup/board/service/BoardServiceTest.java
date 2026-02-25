package kr.co.onmediagroup.board.service;

import kr.co.onmediagroup.board.exception.BoardException;
import kr.co.onmediagroup.board.model.dto.Board;
import kr.co.onmediagroup.board.model.entity.BoardEntity;
import kr.co.onmediagroup.board.repository.BoardRepository;
import kr.co.onmediagroup.exception.AuthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

  @Mock
  private BoardRepository boardRepository;

  @InjectMocks
  private BoardService boardService;

  @Test
  @DisplayName("보드 목록 조회 성공")
  void findAllByUserId_Success() {
    // given
    String userId = "user123";
    BoardEntity entity = BoardEntity.builder()
      .boardId("board1")
      .userId(userId)
      .templateId(1)
      .boardSchema("{}")
      .activeYn(Board.ActiveYn.Y)
      .build();
    Page<BoardEntity> page = new PageImpl<>(Collections.singletonList(entity));
    given(boardRepository.findAllByUserId(any(), any())).willReturn(page);

    // when
    Page<Board.BoardRes> result = boardService.findAllByUserId(userId, PageRequest.of(0, 10));

    // then
    assertFalse(result.isEmpty());
    assertEquals("board1", result.getContent().get(0).getBoardId());
  }

  @Test
  @DisplayName("보드 목록 조회 실패 - 데이터 없음")
  void findAllByUserId_NoBoard() {
    // given
    given(boardRepository.findAllByUserId(any(), any())).willReturn(Page.empty());

    // when & then
    assertThrows(BoardException.NoBoard.class, () -> boardService.findAllByUserId("user123", PageRequest.of(0, 10)));
  }

  @Test
  @DisplayName("보드 상세 조회 실패 - 타인 소유")
  void findByBoardId_Unauthorized() {
    // given
    String userId = "user123";
    String otherUserId = "other";
    BoardEntity entity = BoardEntity.builder()
      .boardId("board1")
      .userId(otherUserId)
      .build();
    given(boardRepository.findById("board1")).willReturn(Optional.of(entity));

    // when & then
    assertThrows(AuthException.UnauthorizedMe.class, () -> boardService.findByBoardId("board1", userId));
  }
}
