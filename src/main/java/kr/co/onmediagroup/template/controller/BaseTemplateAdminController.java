package kr.co.onmediagroup.template.controller;

import jakarta.validation.Valid;
import kr.co.onmediagroup.config.annotation.CheckAdminUser;
import kr.co.onmediagroup.config.annotation.Description;
import kr.co.onmediagroup.template.model.dto.BaseTemplate;
import kr.co.onmediagroup.template.service.BaseTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/template")
public class BaseTemplateAdminController {
  private final BaseTemplateService baseTemplateService;

  @GetMapping("")
  @CheckAdminUser
  @Description("관리자용 템플릿 목록 조회")
  @ResponseStatus(HttpStatus.OK)
  public Page<BaseTemplate.TemplateRes> findAll(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("sortOrder").ascending());
    return this.baseTemplateService.findAllTemplates(pageable);
  }

  @GetMapping("/{templateId}")
  @CheckAdminUser
  @Description("어드민용 템플릿 상세 조회")
  @ResponseStatus(HttpStatus.OK)
  public BaseTemplate.TemplateRes findById(
    @PathVariable Integer templateId
  ) {
    return this.baseTemplateService.findAdminTemplateById(templateId);
  }

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
