package kr.co.onmediagroup.template.controller;

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

  @GetMapping("")
  @ResponseStatus(HttpStatus.OK)
  public List<Template.TemplateResponse> findActiveTemplate() {
    return this.templateService.findActiveTempalte();
  }
}
