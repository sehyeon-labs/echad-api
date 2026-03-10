package kr.co.onmediagroup.template.service;

import kr.co.onmediagroup.template.exception.TemplateException;
import kr.co.onmediagroup.template.model.dto.TemplateCustom;
import kr.co.onmediagroup.template.model.entity.TemplateCustomEntity;
import kr.co.onmediagroup.template.repository.TemplateCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.co.onmediagroup.util.ModelConverter.MODEL_MAPPER;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TemplateCustomService {

  private final TemplateCustomRepository templateCustomRepository;

  public List<TemplateCustom.TemplateCustomRes> findByCustomId(String customId) {
    List<TemplateCustomEntity> entityList = this.templateCustomRepository
      .findByCustomIdWithActiveTemplate(customId);

    if (entityList.isEmpty()) {
      throw new TemplateException.NoTemplate();
    }

    return entityList.stream()
      .map(entity -> MODEL_MAPPER.map(entity, TemplateCustom.TemplateCustomRes.class))
      .toList();
  }
}
