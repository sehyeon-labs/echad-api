package kr.co.onmediagroup.template.controller;

import jakarta.validation.Valid;
import kr.co.onmediagroup.config.annotation.Description;
import kr.co.onmediagroup.template.model.dto.TemplateCustom;
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

  @GetMapping("/{customId}")
  @Description("커스텀 템플릿 조회")
  @ResponseStatus(HttpStatus.OK)
  public List<TemplateCustom.TemplateCustomRes> findByCustomId(
    @AuthenticationPrincipal User.UserPrincipal principal,
    @PathVariable String customId
  ) {
    String userId = principal.getUserId();

    return this.templateCustomService.findByCustomId(
      customId,
      userId
    );
  }

  @PostMapping("")
  @Description("커스텀 템플릿 벌크 생성")
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
