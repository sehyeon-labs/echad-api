package kr.co.onmediagroup.user.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

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
  public static class UserInfoMeRes {
    private String infoId;
    private String groomName;
    private String brideName;
    private LocalDateTime weddingDate;
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class UserInfoAndUserRes extends UserInfoMeRes {
    private User.UserMeRes user;
  }

  @Builder
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UserInfoName{
    private String groomName;
    private String brideName;
  }

  public record UserInfoUpdateMeReq(
    @NotBlank @Size(max = 100) String groomName,
    @NotBlank @Size(max = 100) String brideName,
    @NotNull LocalDateTime weddingDate
  ){
  }
}
