package kr.co.onmediagroup.controllerAdvice;

import kr.co.onmediagroup.template.exception.PostException;
import kr.co.onmediagroup.template.exception.TemplateException;
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
public class TemplateControllerAdvice extends BaseControllerAdvice {
  @ExceptionHandler({TemplateException.class})
  public ProblemDetail handleTemplateException(TemplateException ex) {
    return this.exceptionResponse(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler({TemplateException.NoTemplate.class,})
  public ProblemDetail handleNotFound(RuntimeException ex) {
    return this.exceptionResponse(HttpStatus.NOT_FOUND, ex);
  }

  @ExceptionHandler({TemplateException.AlreadyExistTemplate.class})
  public ProblemDetail handleAlreadyExistTemplate(TemplateException.AlreadyExistTemplate ex) {
    return this.exceptionResponse(HttpStatus.CONFLICT, ex);
  }
}
