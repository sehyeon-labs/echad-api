package kr.co.onmediagroup.controllerAdvice;

import kr.co.onmediagroup.template.exception.PostBlockException;
import kr.co.onmediagroup.template.exception.PostException;
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
public class PostControllerAdvice extends BaseControllerAdvice {
  @ExceptionHandler({PostException.class})
  public ProblemDetail handlePostException(PostException ex) {
    return this.exceptionResponse(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler({
    PostException.NoPost.class,
    PostBlockException.NoPostBlock.class
  })
  public ProblemDetail handleNotFound(RuntimeException ex) {
    return this.exceptionResponse(HttpStatus.NOT_FOUND, ex);
  }

  @ExceptionHandler({PostException.AlreadyExistPost.class})
  public ProblemDetail handleAlreadyExistPost(PostException.AlreadyExistPost ex) {
    return this.exceptionResponse(HttpStatus.CONFLICT, ex);
  }

  @ExceptionHandler({PostException.UnauthorizedPostAccess.class})
  public ProblemDetail handleUnauthorizedPostAccess(PostException.UnauthorizedPostAccess ex) {
    return this.exceptionResponse(HttpStatus.UNAUTHORIZED, ex);
  }
}
