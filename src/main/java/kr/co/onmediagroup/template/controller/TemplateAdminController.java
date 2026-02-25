package kr.co.onmediagroup.template.controller;

import jakarta.validation.Valid;
import kr.co.onmediagroup.config.annotation.CheckAdminUser;
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
  @ResponseStatus(HttpStatus.CREATED)
  public Template.TemplateResponse create(
    @Valid @RequestBody Template.TemplateReq templateReq
  ) {
    return this.templateService.create(
      templateReq.title(),
      templateReq.previewUrl(),
      templateReq.isPremium(),
      templateReq.templateSchema(),
      templateReq.schemaVersion()
    );
  }
}
