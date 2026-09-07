package kr.co.onmediagroup.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

/**
 * 인증 실패 시 호출되는 커스텀 AuthenticationEntryPoint 구현체
 *
 * 인증이 필요한 요청에서 인증 정보가 없거나 유효하지 않은 경우 401 Unauthorized 응답을 JSON 형태로 반환
 * ProblemDetail 형식 사용
 *
 * 사용 이유
 * - 컨트롤러 실행 이후 예외 처리
 * - 인증 실패 예외는 잡지 못함
 * - 상태 코드 및 헤더 조작 제한적
 * - JSON 응답 일관성 보장 어려움
 * 따라서 따로 커스텀 처리한다.
 *
 * */
@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private final ObjectMapper objectMapper;

  @Override
  public void commence(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException authException
  ) throws IOException, ServletException {
    log.info("CustomAuthenticationEntryPoint1");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json;charset=UTF-8");

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "invalid access token");
    problemDetail.setTitle("InvalidAccessToken");
    problemDetail.setInstance(URI.create(request.getRequestURI()));

    String json = objectMapper.writeValueAsString(problemDetail);
    response.getWriter().write(json);
  }

  public void commence(
    @NotNull HttpServletRequest request,
    @NotNull HttpServletResponse response,
    AuthException authException
  ) throws IOException {
    log.info("CustomAuthenticationEntryPoint2");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json;charset=UTF-8");

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "invalid access token");
    problemDetail.setTitle("InvalidAccessToken");
    problemDetail.setInstance(URI.create(request.getRequestURI()));

    String json = objectMapper.writeValueAsString(problemDetail);
    response.getWriter().write(json);
  }
}
