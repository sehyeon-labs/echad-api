package kr.co.onmediagroup.user.controller;

import kr.co.onmediagroup.config.annotation.CheckAdminUser;
import kr.co.onmediagroup.config.annotation.Description;
import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class UserAdminController {

    private final UserService userService;

    @GetMapping("")
    @CheckAdminUser
    @Description("관리자용 사용자 목록 조회")
    public Page<User.UserMeRes> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return userService.getUsers(pageable);
    }
}
