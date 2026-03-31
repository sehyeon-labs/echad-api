package kr.co.onmediagroup.api.naver.controller;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.onmediagroup.api.naver.dto.NaverResponse;
import kr.co.onmediagroup.api.naver.service.NaverService;
import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.user.model.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/naver")
public class NaverController {
  private final NaverService naverService;

  @PostMapping("/login")
  @Description("사용자 로그인")
  @ResponseStatus(value = HttpStatus.OK)
  public NaverResponse.AuthorizeResponse naverLogin () throws URISyntaxException {
    NaverResponse.AuthorizeResponse response = this.naverService.naverLogin();
    return response;
  }

  @PostMapping("/logout")
  @Description("사용자 로그아웃")
  @ResponseStatus(value = HttpStatus.OK)
  public void naverLogout(
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

  @GetMapping("/login/callback")
  @Description("사용자 로그인 콜백")
  @ResponseStatus(value = HttpStatus.OK)
  public User.UserLoginResponse naverLoginCallback (
    @RequestParam String code,
    @RequestParam String state,
    HttpServletResponse res
  ) throws IOException, URISyntaxException {
    User.UserLoginModel loginModel = this.naverService.naverLoginCallback(code, state);

    ResponseCookie cookie = ResponseCookie.from("access_token", loginModel.getToken())
      .httpOnly(true)
      .secure(true) // For HTTPS
      .path("/")
      .maxAge(60 * 60 * 24 * 7) // 7 days
      .build();
    res.addHeader("Set-Cookie", cookie.toString());

    UserInfo.UserInfoName userInfoName = loginModel.getUserInfo();

    User.UserLoginResponse userLoginResponse = User.UserLoginResponse.builder()
      .userId(loginModel.getUser().getUserId())
      .userLevel(loginModel.getUser().getUserLevel())
      .activeYn(loginModel.getUser().getActiveYn())
      .groomName(userInfoName != null ? userInfoName.getGroomName() : null)
      .brideName(userInfoName != null ? userInfoName.getBrideName() : null)
      .build();

    return userLoginResponse;
  }


}
