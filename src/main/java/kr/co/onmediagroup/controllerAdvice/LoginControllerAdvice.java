package kr.co.onmediagroup.controllerAdvice;

import kr.co.onmediagroup.user.exception.LoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 로그인 관련 예외를 전역 처리하는 컨트롤러 어드바이스
 *
 * 예외 응답에 로그인 실패 횟수가 있으면 포함하여 클라이언트에 전달
 * */
@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoginControllerAdvice extends BaseControllerAdvice{

  // 로그인 예외 실패 수 있으면, 예외 응답에 실패 수 포함.
  @ExceptionHandler({LoginException.class})
  public ProblemDetail handleLoginFail(LoginException ex) {
    ProblemDetail problemDetail = this.exceptionResponse(HttpStatus.UNAUTHORIZED, ex);
    if (ex.getLoginFailCount() != null) {
      problemDetail.setProperty("loginFailCount", ex.getLoginFailCount());
    }
    return problemDetail;
  }
}
