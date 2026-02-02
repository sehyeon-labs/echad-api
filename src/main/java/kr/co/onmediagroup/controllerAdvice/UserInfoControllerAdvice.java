package kr.co.onmediagroup.controllerAdvice;

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
public class UserInfoControllerAdvice extends BaseControllerAdvice {
  @ExceptionHandler({UserInfoException.class})
  public ProblemDetail handleUserInfoException(UserInfoException ex) {
    return this.exceptionResponse(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler({UserInfoException.NoUserInfo.class})
  public ProblemDetail handleNoUserInfoException(UserInfoException.NoUserInfo ex) {
    return this.exceptionResponse(HttpStatus.NOT_FOUND, ex);
  }
}
