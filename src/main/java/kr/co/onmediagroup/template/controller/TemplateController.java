package kr.co.onmediagroup.template.controller;

import kr.co.onmediagroup.config.annotation.Description;
import kr.co.onmediagroup.template.model.dto.Template;
import kr.co.onmediagroup.template.service.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/template")
public class TemplateController {
  private final TemplateService templateService;

  @GetMapping("/live")
  @Description("활성 상태의 템플릿 조회")
  @ResponseStatus(HttpStatus.OK)
  public List<Template.TemplateRes> findActiveTemplate() {
    return this.templateService.findActiveTemplates();
  }
}
