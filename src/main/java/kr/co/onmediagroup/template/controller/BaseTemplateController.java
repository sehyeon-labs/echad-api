package kr.co.onmediagroup.template.controller;

import kr.co.onmediagroup.config.annotation.Description;
import kr.co.onmediagroup.template.model.dto.BaseTemplate;
import kr.co.onmediagroup.template.service.BaseTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/template")
public class BaseTemplateController {
  private final BaseTemplateService baseTemplateService;

  @GetMapping("")
  @Description("활성화된 템플릿 목록 조회")
  @ResponseStatus(HttpStatus.OK)
  public List<BaseTemplate.TemplateRes> findActiveTemplates() {
    return this.baseTemplateService.findActiveTemplates();
  }
}
