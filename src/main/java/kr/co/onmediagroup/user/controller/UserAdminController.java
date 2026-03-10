package kr.co.onmediagroup.user.controller;

import kr.co.onmediagroup.config.annotation.CheckAdminUser;
import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class UserAdminController {
  private final UserService userService;

  @GetMapping("")
  @CheckAdminUser
  @Description("유저 목록 조회")
  @ResponseStatus(HttpStatus.OK)
  public Page<User.UserModel> findAll(
    @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
  ) {
    return this.userService.findAll(pageable);
  }
}
