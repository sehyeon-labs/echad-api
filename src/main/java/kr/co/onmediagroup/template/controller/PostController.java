package kr.co.onmediagroup.template.controller;

import jakarta.validation.Valid;
import kr.co.onmediagroup.config.annotation.Description;
import kr.co.onmediagroup.template.model.dto.Post;
import kr.co.onmediagroup.template.service.PostBlockService;
import kr.co.onmediagroup.template.service.PostService;
import kr.co.onmediagroup.user.model.dto.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/template/custom")
public class PostController {
  private final PostBlockService postBlockService;
  private final PostService postService;

  @GetMapping("/{postId}")
  @Description("게시물 상세 조회")
  @ResponseStatus(HttpStatus.OK)
  public Post.PostRes findByPostId(
    @AuthenticationPrincipal User.UserPrincipal principal,
    @PathVariable String postId
  ) {
    String userId = principal.getUserId();

    return this.postService.findByPostId(
      postId,
      userId
    );
  }

  @PostMapping("")
  @Description("게시물 및 블록 생성")
  @ResponseStatus(HttpStatus.CREATED)
  public void create(
    @AuthenticationPrincipal User.UserPrincipal principal,
    @Valid @RequestBody Post.PostCreateReq postCreateReq
  ) {
    String userId = principal.getUserId();
    String postId = postCreateReq.customId();
    List<Post.PostBlockReq> postBlockReqList = postCreateReq.postBlockReqList();

    this.postBlockService.saveBulk(
      userId,
      postId,
      postBlockReqList
    );
  }
}
