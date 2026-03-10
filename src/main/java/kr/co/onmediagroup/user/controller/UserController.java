package kr.co.onmediagroup.user.controller;

import jakarta.validation.Valid;
import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  @GetMapping("/me")
  @Description("자기 자신 정보 조회")
  @ResponseStatus(HttpStatus.OK)
  public User.UserMeRes getMe(
    @AuthenticationPrincipal User.UserPrincipal principal
  ) {
    String userId = principal.getUserId();

    User.UserMeRes userMeRes = userService.getMe(userId);

    return userMeRes;
  }


  @PostMapping("/me")
  @Description("자기 자신 정보 수정")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateMe(
    @AuthenticationPrincipal User.MinimumUserPrincipal userPrincipal,
    @Valid @RequestBody User.UserUpdateReq req
  ) {
    String userId = userPrincipal.getUserId();
    userService.updateMe(
      userId,
      req.userEmail(),
      req.phoneNumber(),
      req.phoneVerifiedYn(),
      req.groomName(),
      req.brideName(),
      req.weddingDate()
    );
  }
}
