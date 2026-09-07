package kr.co.onmediagroup.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.onmediagroup.config.annotation.Description;
import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.user.model.dto.UserInfo;
import kr.co.onmediagroup.user.service.UserInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/info")
public class UserInfoController {

  private final UserInfoService userInfoService;

  @PatchMapping("/me")
  @Description("자기 자신 정보 수정")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateMe(
    @AuthenticationPrincipal User.MinimumUserPrincipal userPrincipal,
    @Valid @RequestBody UserInfo.UserInfoUpdateMeReq req
  ) {
    String userId = userPrincipal.getUserId();
    userInfoService.updateMe(
      userId,
      req.groomName(),
      req.brideName(),
      req.weddingDate()
    );
  }
}
