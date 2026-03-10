package kr.co.onmediagroup.template.controller;

import kr.co.onmediagroup.template.model.dto.TemplateCustom;
import kr.co.onmediagroup.template.service.TemplateCustomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/template/custom")
public class TemplateCustomController {

  private final TemplateCustomService templateCustomService;

  @GetMapping("/{customId}")
  @ResponseStatus(HttpStatus.OK)
  public List<TemplateCustom.TemplateCustomRes> findByCustomId(@PathVariable String customId) {
    return this.templateCustomService.findByCustomId(customId);
  }
}
