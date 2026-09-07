package kr.co.onmediagroup.user.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class User {

  @Getter
  @AllArgsConstructor
  public enum VerifiedYn{
    Y("Y"),
    N("N");
    private final String verifiedYn;
  }

  @Getter
  @AllArgsConstructor
  public enum Level{
    ADMIN("ADMIN"),
    USER("USER");
    private final String level;
  }

  @Getter
  @AllArgsConstructor
  public enum ActiveYn{
    Y("Y"),
    N("N");
    private final String active;
  }

  @Getter
  @AllArgsConstructor
  public enum SocialType{
    NORMAL("NORMAL"),
    NAVER("NAVER"),
    KAKAO("KAKAO"),
    GOOGLE("GOOGLE");
    private final String socialType;
  }

  @Getter
  @AllArgsConstructor
  public enum Gender{
    M("M"),
    F("F"),
    N("N");
    private final String gender;
  }

  @Data
  public static class UserModel{
    private String userId;
    private String userPassword;
    private String userEmail;
    private String phoneNumber;
    private VerifiedYn phoneVerifiedYn;
    private Level userLevel;
    private ActiveYn activeYn;
    private Integer loginFailCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
  }

  @Builder
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UserSimpleModel {
    private String userId;
    private User.Level userLevel;
    private User.ActiveYn activeYn;
  }

  @Builder
  @Data
  public static class UserLoginModel {
    private String token;
    private User.UserSimpleModel user;
    private UserInfo.UserInfoName userInfo;
  }

  @Builder
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UserLoginResponse{
    private String userId;
    private User.Level userLevel;
    private User.ActiveYn activeYn;
    private String groomName;
    private String brideName;
  }

  @Builder
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class UserJoinRes {
    private String userId;
    private String userEmail;
    private String phoneNumber;
    private VerifiedYn phoneVerifiedYn;
    private String groomName;
    private String brideName;
    private LocalDateTime weddingDate;
    private String userName;
  }


  public record UserJoinReq(
    @NotBlank @Size(max = 255) String userId,
    @NotBlank @Size(max = 255) String userPassword,
    @NotBlank @Size(max = 255) String userEmail,
    String phoneNumber,
    VerifiedYn phoneVerifiedYn,
    @NotBlank @Size(max = 100) String groomName,
    @NotBlank @Size(max = 100) String brideName,
    @NotNull LocalDateTime weddingDate
  ){
  }

  public record UserLoginReq(
    @NotBlank @Size(max = 255) String userId,
    @NotBlank @Size(max = 255) String userPassword
  ){
  }

  @Builder
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UserMeRes {
    private String userId;
    private String userEmail;
    private String phoneNumber;
    private VerifiedYn phoneVerifiedYn;
    private Level userLevel;
    private ActiveYn activeYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserInfo.UserInfoMeRes userInfo;
  }

  public record UserUpdateReq(
    @NotBlank @Size(max = 255) String userEmail,
    String phoneNumber,
    VerifiedYn phoneVerifiedYn,
    @NotBlank @Size(max = 100) String groomName,
    @NotBlank @Size(max = 100) String brideName,
    @NotNull LocalDateTime weddingDate
  ){
  }

  public record UserPasswordUpdateReq(
    @NotBlank String currentPassword,
    @NotBlank String newPassword,
    @NotBlank String newPasswordConfirm
  ){
  }

  public record EmailSendReq(
    @NotBlank @Email String email
  ){
  }

  public record EmailVerifyReq(
    @NotBlank @Email String email,
    @NotBlank String code
  ){
  }

  public record AdminUserJoinReq(
    @NotBlank @Size(max = 255) String userId,
    @NotBlank @Size(max = 255) String userPassword,
    @NotBlank @Email String userEmail,
    @Size(max = 100) String userName,
    String phoneNumber
  ){
  }


  /**
   * JWT 사용자 정보 DTO
   */
  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class MinimumUserPrincipal {
    private String userId;
    private User.Level userLevel;
    private User.ActiveYn activeYn;
  }

  public static class UserPrincipal extends MinimumUserPrincipal implements UserDetails {

    @Builder
    public UserPrincipal(String userId, User.Level userLevel, User.ActiveYn activeYn) {
      super(userId, userLevel, activeYn);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(new SimpleGrantedAuthority("ROLE_" + getUserLevel().name()));
    }

    @Override
    public String getPassword() { return null; }

    @Override
    public String getUsername() { return getUserId(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return getActiveYn() == User.ActiveYn.Y; }

    @Override
    public String toString() {
      return "UserPrincipal{" +
        "userId='" + getUserId() + '\'' +
        ", userLevel=" + getUserLevel() +
        ", activeYn=" + getActiveYn() +
        ", authorities=" + getAuthorities() +
        ", accountNonExpired=" + isAccountNonExpired() +
        ", accountNonLocked=" + isAccountNonLocked() +
        ", credentialsNonExpired=" + isCredentialsNonExpired() +
        ", enabled=" + isEnabled() +
        '}';
    }
  }

}

