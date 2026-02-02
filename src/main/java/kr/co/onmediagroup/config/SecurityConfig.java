package kr.co.onmediagroup.config;

import kr.co.onmediagroup.exception.CustomAuthenticationEntryPoint;
import kr.co.onmediagroup.filter.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.time.Duration;
import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfig {
  private final JWTAuthenticationFilter jwtAuthenticationFilter;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  /**
   * 시큐리티 필터 체인 설정
   *
   * CSRF, 기본 폼 로그인 비활성화
   * 세션 X, Stateless 정책 적용
   *
   * */
  @Bean
  @Order(SecurityProperties.BASIC_AUTH_ORDER)
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity
      .headers(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .csrf(AbstractHttpConfigurer::disable)
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      // 권한 설정
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/auth/**").permitAll()
        .anyRequest().authenticated()
      )
      // 인증 실패 시 예외 처리
      .exceptionHandling(exception -> exception
        .authenticationEntryPoint(customAuthenticationEntryPoint)
      )
      // JWT 인증 필터 등록
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

      // 커스텀 CORS 설정
      .cors(cors -> cors.configurationSource(corsConfigurationScource()));
    return httpSecurity.build();
  }

  /**
   * 커스텀 CORS 설정
   *
   * 요청 헤더의 Origin 값을 읽어 허용 도메인 동작 처리
   * 허용 HTTP 메서드 및 헤더 설정
   * 인증 관련 헤더(Authorization)도 허용하여 JWT 토큰 전달 가능하게 설정
   * 자격 증명(Credentials) 허용
   *
   * */
  public CorsConfigurationSource corsConfigurationScource() {
    return request -> {
      CorsConfiguration corsConfiguration = new CorsConfiguration();

      String origin = request.getHeader("Origin");
      if (origin != null && !origin.isBlank()) {
        corsConfiguration.addAllowedOrigin(origin);
      }

      corsConfiguration.setAllowedMethods(List.of("POST", "GET", "OPTIONS", "PUT", "PATCH", "DELETE"));
      corsConfiguration.setMaxAge(Duration.ofSeconds(3600));
      corsConfiguration.setAllowedHeaders(List.of("Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization"));
      corsConfiguration.setExposedHeaders(List.of("Content-Length", "Authorization"));
      corsConfiguration.setAllowCredentials(true);

      return corsConfiguration;
    };
  }

  /**
   * 비밀번호 암호화를 위한 PasswordEncoder 빈 등록
   *
   * BCryptPasswordEncoder 사용
   * */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
