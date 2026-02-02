package kr.co.onmediagroup.controllerAdvice;

import kr.co.onmediagroup.user.exception.UserException;
import kr.co.onmediagroup.user.exception.UserInfoException;
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
public class UserControllerAdvice extends BaseControllerAdvice {
  @ExceptionHandler({UserException.class})
  public ProblemDetail handleUserException(UserException ex) {
    return this.exceptionResponse(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler({UserException.AlreadyExistUserId.class})
  public ProblemDetail handleAlreadyExistUserIdException(UserException.AlreadyExistUserId ex) {
    return this.exceptionResponse(HttpStatus.CONFLICT, ex);
  }
}
