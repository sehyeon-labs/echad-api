package kr.co.onmediagroup.user.controller;

import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  /**
   * 내 정보 조회
   */
  @GetMapping("/me")
  public User.UserMeRes getMe(@AuthenticationPrincipal User.UserPrincipal principal) {
    return userService.getMe(principal.getUserId());
  }

  /**
   * 내 정보 수정
   */
  @PostMapping("/me")
  public void updateMe(@AuthenticationPrincipal User.UserPrincipal principal,
                       @RequestBody User.UserUpdateReq req) {
    userService.updateMe(principal.getUserId(), req);
  }
}
