package kr.co.onmediagroup.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.onmediagroup.exception.AuthException;
import kr.co.onmediagroup.exception.CustomAuthenticationEntryPoint;
import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 커스텀 필터
 *
 * HTTP 요청의 Authorization 헤더에서 JWT를 추출하고 유효성 검증을 수행합니다.
 * 유효한 토큰인 경우 SecurityContext에 인증 정보를 저장합니다.
 * 인증 실패 또는 토큰이 없을 경우, 인증 예외는 CustomAuthenticationEntryPoint에서 처리됩니다.
 * OncePerRequestFilter를 상속하여 요청당 한 번만 필터가 실행됩니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final JWTUtil jwtUtil;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {

    try {
      String token = resolveToken(request);

      if (token != null && jwtUtil.verifyToken(token)) {

        User.UserPrincipal principal =
          jwtUtil.verifyTokenWithUserPrincipal(token);

        if (principal != null && principal.getUserLevel() != null) {
          UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
              principal,
              null,
              principal.getAuthorities()
            );

          SecurityContextHolder.getContext()
            .setAuthentication(authentication);

          log.debug("JWT authenticated user={}", principal.getUserId());
        }
      }

      filterChain.doFilter(request, response);

    } catch (AuthException ex) {
      SecurityContextHolder.clearContext();
      customAuthenticationEntryPoint.commence(request, response, ex);
    }
  }

  private String resolveToken(HttpServletRequest request) {
    // 1. Authorization 헤더 우선
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }

    // 2. HttpOnly Cookie
    if (request.getCookies() != null) {
      for (var cookie : request.getCookies()) {
        if ("access_token".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }

    return null;
  }
}
