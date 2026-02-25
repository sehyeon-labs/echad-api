package kr.co.onmediagroup.controllerAdvice;

import kr.co.onmediagroup.board.exception.BoardException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BoardControllerAdvice extends BaseControllerAdvice {

  @ExceptionHandler({BoardException.class})
  public ProblemDetail handleBoardException(BoardException ex) {
    return this.exceptionResponse(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler({BoardException.NoBoard.class})
  public ProblemDetail handleNoBoard(BoardException.NoBoard ex) {
    return this.exceptionResponse(HttpStatus.NOT_FOUND, ex);
  }
}
