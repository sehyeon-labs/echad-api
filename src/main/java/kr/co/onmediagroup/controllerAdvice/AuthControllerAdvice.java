package kr.co.onmediagroup.controllerAdvice;

import kr.co.onmediagroup.exception.AuthException;
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
public class AuthControllerAdvice extends BaseControllerAdvice {
  @ExceptionHandler({AuthException.class})
  public ProblemDetail handleAuthException(AuthException ex) {
    return this.exceptionResponse(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler({AuthException.UnauthorizedMe.class})
  public ProblemDetail handleUnauthorizedException(AuthException.UnauthorizedMe ex) {
    return this.exceptionResponse(HttpStatus.UNAUTHORIZED, ex);
  }
}
