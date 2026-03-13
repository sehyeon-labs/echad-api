package kr.co.onmediagroup.template.controller;

import jakarta.validation.Valid;
import kr.co.onmediagroup.config.annotation.CheckAdminUser;
import kr.co.onmediagroup.config.annotation.Description;
import kr.co.onmediagroup.template.model.dto.BaseTemplate;
import kr.co.onmediagroup.template.service.BaseTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/template")
public class BaseTemplateAdminController {
  private final BaseTemplateService baseTemplateService;

  @PostMapping("")
  @CheckAdminUser
  @Description("템플릿 생성")
  @ResponseStatus(HttpStatus.CREATED)
  public BaseTemplate.TemplateRes create(
    @Valid @RequestBody BaseTemplate.TemplateCreateReq templateCreateReq
  ) {
    return this.baseTemplateService.create(
      templateCreateReq.title(),
      templateCreateReq.component(),
      templateCreateReq.componentType(),
      templateCreateReq.previewUrl(),
      templateCreateReq.isPremium(),
      templateCreateReq.templateSchema(),
      templateCreateReq.sortOrder()
    );
  }
}
