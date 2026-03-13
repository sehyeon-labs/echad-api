package kr.co.onmediagroup.controllerAdvice;

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

  @ExceptionHandler({PostException.NoTemplate.class})
  public ProblemDetail handleNoTemplate(PostException.NoTemplate ex) {
    return this.exceptionResponse(HttpStatus.NOT_FOUND, ex);
  }

  @ExceptionHandler({PostException.AlreadyExistTemplate.class})
  public ProblemDetail handleAlreadyExistTemplate(PostException.AlreadyExistTemplate ex) {
    return this.exceptionResponse(HttpStatus.CONFLICT, ex);
  }
}
