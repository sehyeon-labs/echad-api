package kr.co.onmediagroup.board.controller;

import jakarta.validation.Valid;
import kr.co.onmediagroup.board.model.dto.Board;
import kr.co.onmediagroup.board.service.BoardService;
import kr.co.onmediagroup.config.annotation.CheckUserLevel;
import kr.co.onmediagroup.user.model.dto.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

  private final BoardService boardService;

  @CheckUserLevel
  @GetMapping("")
  @ResponseStatus(HttpStatus.OK)
  public Page<Board.BoardRes> findAll(
    @AuthenticationPrincipal User.MinimumUserPrincipal userPrincipal,
    @ParameterObject Pageable pageable
  ) {
    String userId = userPrincipal.getUserId();
    return boardService.findAllByUserId(userId, pageable);
  }

  @CheckUserLevel
  @GetMapping("/{board_id}")
  @ResponseStatus(HttpStatus.OK)
  public Board.BoardRes findOne(
    @AuthenticationPrincipal User.MinimumUserPrincipal userPrincipal,
    @PathVariable("board_id") String boardId
  ) {
    String userId = userPrincipal.getUserId();
    return boardService.findByBoardId(boardId, userId);
  }

  @CheckUserLevel
  @PatchMapping("/{board_id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void update(
    @AuthenticationPrincipal User.MinimumUserPrincipal userPrincipal,
    @PathVariable("board_id") String boardId,
    @Valid @RequestBody Board.BoardUpdateVO updateVO
  ) {
    String userId = userPrincipal.getUserId();
    boardService.update(
      boardId,
      userId,
      updateVO.boardSchema());
  }
}
