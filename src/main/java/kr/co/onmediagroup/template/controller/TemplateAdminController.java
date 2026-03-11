package kr.co.onmediagroup.template.controller;

import jakarta.validation.Valid;
import kr.co.onmediagroup.config.annotation.CheckAdminUser;
import kr.co.onmediagroup.config.annotation.Description;
import kr.co.onmediagroup.template.model.dto.Template;
import kr.co.onmediagroup.template.service.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/template")
public class TemplateAdminController {
  private final TemplateService templateService;

  @PostMapping("")
  @CheckAdminUser
  @Description("템플릿 생성")
  @ResponseStatus(HttpStatus.CREATED)
  public Template.TemplateRes create(
    @Valid @RequestBody Template.TemplateCreateReq templateCreateReq
  ) {
    return this.templateService.create(
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
