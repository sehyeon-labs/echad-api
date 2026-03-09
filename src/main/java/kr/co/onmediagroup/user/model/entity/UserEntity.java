package kr.co.onmediagroup.user.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.onmediagroup.user.model.dto.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

  @Id
  @Size(max = 255)
  @Column(name = "user_id")
  private String userId;

  @Size(max = 255)
  @Column(name = "user_password")
  private String userPassword;

  @NotBlank
  @Size(max = 255)
  @Column(name = "user_email")
  private String userEmail;

  @Size(max = 20)
  @Column(name = "phone_number")
  private String phoneNumber;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "phone_verified_yn")
  private User.VerifiedYn phoneVerifiedYn = User.VerifiedYn.N;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "user_level")
  private User.Level userLevel = User.Level.USER;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "active_yn")
  private User.ActiveYn activeYn = User.ActiveYn.Y;

  @Builder.Default
  @Column(name = "login_fail_count")
  private Integer loginFailCount = 0;

  @Column(name = "created_at")
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @LastModifiedDate
  private LocalDateTime updatedAt;


  // 로그인 실패 기록 초기화
  public UserEntity clearFailedLogin() {
    this.loginFailCount = 0;
    return this;
  }

  // 로그인 실패 기록 증가. 최대 실패 횟수 이상 실패 시, 사용자 비활성화
  public UserEntity increaseFailedLogin(Integer maxFailedLoginCount) {
    this.loginFailCount = this.loginFailCount + 1;
    if (loginFailCount >= maxFailedLoginCount) {
      this.activeYn = User.ActiveYn.N;
    }
    return this;
  }

  // 유저 정보 업데이트
  public UserEntity update(User.UserUpdateReq req) {
    this.userEmail = req.userEmail();
    this.phoneNumber = req.phoneNumber();
    this.phoneVerifiedYn = req.phoneVerifiedYn();
    return this;
  }
}
