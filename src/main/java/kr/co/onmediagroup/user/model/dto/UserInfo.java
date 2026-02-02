package kr.co.onmediagroup.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserInfo {

  @Data
  public static class UserInfoModel{
    private String infoId;
    private String userId;
    private String groomName;
    private String brideName;
    private LocalDateTime weddingDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
  }

  @Builder
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UserInfoName{
    private String groomName;
    private String brideName;
  }
}
