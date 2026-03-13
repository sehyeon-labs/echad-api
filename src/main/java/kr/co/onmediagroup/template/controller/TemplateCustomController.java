package kr.co.onmediagroup.template.controller;

import jakarta.validation.Valid;
import kr.co.onmediagroup.config.annotation.Description;
import kr.co.onmediagroup.template.model.dto.TemplateCustom;
import kr.co.onmediagroup.template.service.CustomService;
import kr.co.onmediagroup.template.service.TemplateCustomService;
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
public class TemplateCustomController {
  private final TemplateCustomService templateCustomService;
  private final CustomService customService;

  @GetMapping("/{customId}")
  @Description("커스텀 템플릿 게시물 상세 조회")
  @ResponseStatus(HttpStatus.OK)
  public TemplateCustom.CustomRes findByCustomId(
    @AuthenticationPrincipal User.UserPrincipal principal,
    @PathVariable String customId
  ) {
    String userId = principal.getUserId();

    return this.customService.findByCustomId(
      customId,
      userId
    );
  }

  @PostMapping("")
  @Description("커스텀 템플릿 게시물 및 요소 생성")
  @ResponseStatus(HttpStatus.CREATED)
  public void create(
    @AuthenticationPrincipal User.UserPrincipal principal,
    @Valid @RequestBody TemplateCustom.TemplateCustomCreateReq templateCustomCreateReq
  ) {
    String userId = principal.getUserId();
    String customId = templateCustomCreateReq.customId();
    List<TemplateCustom.TemplateCustomReq> templateCustomReqList = templateCustomCreateReq.templateCustomReqList();

    this.templateCustomService.saveBulk(
      userId,
      customId,
      templateCustomReqList
    );
  }
}
