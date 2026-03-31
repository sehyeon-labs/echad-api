package kr.co.onmediagroup.api.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

  public class NaverResponse {

    @Data
    @Builder
    public static class AuthorizeResponse {
      private String url;
      private String state;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenResponse {
      @JsonProperty("access_token")
      private String access_token;
      @JsonProperty("refresh_token")
      private String refresh_token;
      private String token_type;
      private Integer expires_in;
      private String error;
      @JsonProperty("error_description")
      private String errorDescription;
      private String result;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileResponse {
      private String id;
      private String name;
      private String email;
      private String mobile;
      private String age;
      private String gender;
    }
  }
