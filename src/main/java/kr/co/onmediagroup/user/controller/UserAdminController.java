package kr.co.onmediagroup.user.controller;

import kr.co.onmediagroup.config.annotation.CheckAdminUser;
import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  /**
   * 유저 전체 목록 조회 (어드민용)
   *
   * @param pageable 페이징 및 정렬 정보 (기본값: 생성일 내림차순)
   * @return 페이징된 유저 목록
   */
  @GetMapping("")
  @CheckAdminUser
  @ResponseStatus(HttpStatus.OK)
  public Page<User.UserModel> findAll(
    @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
  ) {
    return this.userService.findAll(pageable);
  }
}
