package kr.co.onmediagroup.template.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.co.onmediagroup.template.exception.TemplateException;
import kr.co.onmediagroup.template.model.dto.Template;
import kr.co.onmediagroup.template.model.entity.TemplateEntity;
import kr.co.onmediagroup.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.co.onmediagroup.util.ModelConverter.MODEL_MAPPER;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TemplateService {
  private final TemplateRepository templateRepository;

  public List<Template.TemplateResponse> findActiveTemplates() {
    List<TemplateEntity> entityList = this.templateRepository
      .findByActiveYnAndDeletedAtIsNullOrderBySortOrderDesc(Template.ActiveYn.Y);

    if (entityList.isEmpty()) {
      throw new TemplateException.NoTemplate();
    }

    List<Template.TemplateResponse> res = entityList.stream()
      .map(entity -> MODEL_MAPPER.map(entity, Template.TemplateResponse.class))
      .toList();

    return res;
  }

  public Template.TemplateResponse create(
    String title,
    String previewUrl,
    Template.IsPremium isPremium,
    String templateSchem,
    Integer schemaVersion
  ) {
    // 중복검사
    boolean existsByTitleAndSchemaVersion = this.templateRepository.existsByTitleAndSchemaVersion(title, schemaVersion);

    if (!existsByTitleAndSchemaVersion) {
      throw new TemplateException.AlreadyExistTemplate();
    }

    TemplateEntity entity = TemplateEntity.builder()
      .title(title)
      .previewUrl(previewUrl)
      .isPremium(isPremium)
      .templateSchema(templateSchem)
      .schemaVersion(schemaVersion)
      .build();

    entity = this.templateRepository.save(entity);

    Template.TemplateResponse res = MODEL_MAPPER.map(entity, Template.TemplateResponse.class);

    return res;
  }
}
