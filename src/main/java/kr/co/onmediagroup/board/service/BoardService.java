package kr.co.onmediagroup.board.service;

import kr.co.onmediagroup.board.exception.BoardException;
import kr.co.onmediagroup.board.model.dto.Board;
import kr.co.onmediagroup.board.model.entity.BoardEntity;
import kr.co.onmediagroup.board.repository.BoardRepository;
import kr.co.onmediagroup.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.co.onmediagroup.util.ModelConverter.MODEL_MAPPER;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
  private final BoardRepository boardRepository;

  public Page<Board.BoardRes> findAllByUserId(String userId, Pageable pageable) {
    Page<BoardEntity> entityPage = boardRepository.findAllByUserId(userId, pageable);

    if (entityPage.isEmpty()) {
      throw new BoardException.NoBoard();
    }

    return entityPage.map(entity -> MODEL_MAPPER.map(entity, Board.BoardRes.class));
  }

  public Board.BoardRes findByBoardId(String boardId, String userId) {
    BoardEntity entity = boardRepository.findById(boardId)
      .orElseThrow(BoardException.NoBoard::new);

    if (!entity.getUserId().equals(userId)) {
      throw new AuthException.UnauthorizedMe();
    }

    return MODEL_MAPPER.map(entity, Board.BoardRes.class);
  }

  public void update(String boardId, String userId, String boardSchema) {
    BoardEntity entity = boardRepository.findById(boardId)
      .orElseThrow(BoardException.NoBoard::new);

    if (!entity.getUserId().equals(userId)) {
      throw new AuthException.UnauthorizedMe();
    }

    entity.setBoardSchema(boardSchema);

    this.boardRepository.save(entity);
  }
}
