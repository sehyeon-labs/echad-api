package kr.co.onmediagroup.controllerAdvice;

import kr.co.onmediagroup.util.StringUtil;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

/**
 * 기본 컨트롤러 어드바이스
 *
 * 하위 컨트롤러 어드바이스는 이 클래스를 상속 하여 사용해야 함.
 * */
public class BaseControllerAdvice {

  private String parseExceptionMessage(Exception exception) {
    String message = "no detail message for %s".formatted(exception.getClass().getSimpleName());

    if (StringUtil.isExist(exception.getMessage())) {
      message = exception.getMessage();
    }

    return message;
  }

  public ProblemDetail exceptionResponse(HttpStatusCode status, String detail) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
    problemDetail.setTitle(status.toString());

    return problemDetail;
  }

  public ProblemDetail exceptionResponse(HttpStatusCode status, Exception exception) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(status);
    problemDetail.setDetail(parseExceptionMessage(exception));
    problemDetail.setTitle(status.toString());

    return problemDetail;
  }
}