package kr.co.onmediagroup.api.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class NaverRequest {

  @Getter
  @AllArgsConstructor
  public enum GrantType {
    AUTHORIZATION_CODE("authorization_code"),
    REFRESH_CODE("refresh_token"),
    DELETE("delete");
    private final String value;
  }

  @Data
  @Builder
  public static class AuthorizeRequest {
    private String response_type;
    private String client_id;
    @JsonProperty("redirect_uri")
    private String redirect_id;
    private String state;
  }

  @Data
  @Builder
  public static class TokenRequest {
    @Schema(description = "인증 과정에 대한 구분값")
    private String grant_type;
    @Schema(description = "애플리케이션 클라이언트 아이디")
    private String client_id;
    @Schema(description = "애플리케이션 클라이언트 시크릿")
    private String client_secret;
    @Schema(description = "인증 코드")
    private String code;
    @Schema(description = "사이트 간 요청 위조 공격 방지 상태 토큰값")
    @JsonProperty("state")
    private String state;
    @Schema(description = "리프레시 토큰")
    private String refresh_token;
    @Schema(description = "엑세스 토큰")
    private String access_token;
    @Schema(description = "인증 제공자", defaultValue = "NAVER")
    private String service_provider;
  }
}
