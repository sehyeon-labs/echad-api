package kr.co.onmediagroup.template.controller;

import jakarta.validation.Valid;
import kr.co.onmediagroup.config.annotation.Description;
import kr.co.onmediagroup.template.model.dto.PostBlock;
import kr.co.onmediagroup.template.service.PostBlockService;
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
@RequestMapping("/post/block")
public class PostBlockController {

    private final PostBlockService postBlockService;

    @PutMapping("/{postId}")
    @Description("게시물 블록 전체 순서 수정")
    @ResponseStatus(HttpStatus.OK)
    public void updateOrders(
      @AuthenticationPrincipal User.UserPrincipal principal,
      @PathVariable String postId,
      @Valid @RequestBody List<PostBlock.PostBlockOrderReq> reqList
    ) {
        String userId = principal.getUserId();

        this.postBlockService.updateOrders(
          userId,
          postId,
          reqList
        );
    }

    @PatchMapping("/{postId}/sort")
    @Description("게시물 블록 2개 순서 교환")
    @ResponseStatus(HttpStatus.OK)
    public void swapOrders(
      @AuthenticationPrincipal User.UserPrincipal principal,
      @PathVariable String postId,
      @Valid @RequestBody PostBlock.PostBlockSwapReq swapReq
    ) {
        String userId = principal.getUserId();

        postBlockService.swapOrders(
          userId,
          postId,
          swapReq.postBlockId1(),
          swapReq.postBlockId2()
        );
    }
}
