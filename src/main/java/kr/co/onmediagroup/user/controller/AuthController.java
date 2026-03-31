package kr.co.onmediagroup.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.co.onmediagroup.exception.AuthException;
import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.user.model.dto.UserInfo;
import kr.co.onmediagroup.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
  private final AuthService authService;
  private final kr.co.onmediagroup.api.naver.service.NaverService naverService;

  @GetMapping("/me")
  @Description("로그인 사용자 정보 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public User.UserLoginResponse authMe(
    @AuthenticationPrincipal User.UserPrincipal principal
  ) {
    if (principal == null) {
      throw new AuthException.UnauthorizedMe();
    }

    String userId = principal.getUserId();

    UserInfo.UserInfoName userInfoName = this.authService.getUserInfoName(userId);

    User.UserLoginResponse res = User.UserLoginResponse.builder()
      .userId(principal.getUserId())
      .userLevel(principal.getUserLevel())
      .activeYn(principal.getActiveYn())
      .groomName(userInfoName.getGroomName())
      .brideName(userInfoName.getBrideName())
      .build();

    return res;
  }

  @PostMapping("/login")
  @Description("사용자 로그인")
  @ResponseStatus(value = HttpStatus.OK)
  public User.UserLoginResponse login(
    @Valid @RequestBody User.UserLoginReq userLoginReq,
    HttpServletResponse response
  ) {
    User.UserLoginModel model = this.authService.login(
      userLoginReq.userId(),
      userLoginReq.userPassword()
    );

    ResponseCookie cookie = ResponseCookie.from("access_token", model.getToken())
      .httpOnly(true)
      .secure(false) // 운영: true / 로컬: false
      .sameSite("Strict")
      .path("/")
      .maxAge(Duration.ofDays(1))
      .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    User.UserLoginResponse res = User.UserLoginResponse.builder()
      .userId(model.getUser().getUserId())
      .userLevel(model.getUser().getUserLevel())
      .activeYn(model.getUser().getActiveYn())
      .groomName(model.getUserInfo().getGroomName())
      .brideName(model.getUserInfo().getBrideName())
      .build();

    return res;
  }

  @PostMapping("/logout")
  @Description("사용자 로그아웃")
  @ResponseStatus(value = HttpStatus.OK)
  public void logout(
    HttpServletResponse response
  ) {
    ResponseCookie cookie = ResponseCookie.from("access_token", "")
      .httpOnly(true)
      .secure(false)
      .sameSite("Strict")
      .path("/")
      .maxAge(0)
      .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  @PostMapping("/join")
  @Description("사용자 회원가입")
  @ResponseStatus(HttpStatus.CREATED)
  public User.UserJoinRes join(
    @Valid @RequestBody User.UserJoinReq userJoinReq
  ) {
    return this.authService.join(
      userJoinReq.userId(),
      userJoinReq.userPassword(),
      userJoinReq.userEmail(),
      userJoinReq.phoneNumber(),
      userJoinReq.phoneVerifiedYn(),
      userJoinReq.groomName(),
      userJoinReq.brideName(),
      userJoinReq.weddingDate()
    );
  }

  @PatchMapping("/password")
  @Description("비밀번호 수정")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updatePassword(
    @AuthenticationPrincipal User.UserPrincipal principal,
    @Valid @RequestBody User.UserPasswordUpdateReq userPasswordUpdateReq
  ) {
    String userId = principal.getUserId();

    this.authService.updatePassword(userId, userPasswordUpdateReq);
  }

  @PostMapping("/user/email-send")
  @Description("이메일 인증번호 발송")
  @ResponseStatus(HttpStatus.OK)
  public void sendEmailVerification(
    @Valid @RequestBody User.EmailSendReq emailSendReq
  ) {
    this.authService.sendVerificationEmail(emailSendReq.email());
  }

  @PostMapping("/user/email-verify")
  @Description("이메일 인증번호 확인")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void verifyEmailCode(
    @Valid @RequestBody User.EmailVerifyReq emailVerifyReq
  ) {
    this.authService.verifyEmailCode(emailVerifyReq.email(), emailVerifyReq.code());
  }

  @DeleteMapping("/user/delete")
  @Description("사용자 회원 탈퇴")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(
    @AuthenticationPrincipal User.UserPrincipal principal,
    @CookieValue(value = "access_token", required = false) String accessToken,
    HttpServletResponse response
  ) throws java.net.URISyntaxException {
    if (principal == null) {
      throw new AuthException.UnauthorizedMe();
    }

    String userId = principal.getUserId();

    // 회원 탈퇴 및 네이버 연동 해제 로직 호출
    this.naverService.withdraw(userId, accessToken);

    // 쿠키 삭제
    ResponseCookie cookie = ResponseCookie.from("access_token", "")
      .httpOnly(true)
      .secure(false)
      .sameSite("Strict")
      .path("/")
      .maxAge(0)
      .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }
}
