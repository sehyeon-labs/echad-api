package kr.co.onmediagroup.template.service;

import kr.co.onmediagroup.template.exception.TemplateException;
import kr.co.onmediagroup.template.model.dto.Template;
import kr.co.onmediagroup.template.model.dto.TemplateCustom;
import kr.co.onmediagroup.template.model.entity.CustomEntity;
import kr.co.onmediagroup.template.model.entity.CustomTemplateEntity;
import kr.co.onmediagroup.template.model.entity.TemplateCustomEntity;
import kr.co.onmediagroup.template.repository.CustomRepository;
import kr.co.onmediagroup.template.repository.TemplateCustomRepository;
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
public class TemplateCustomService {
  private final TemplateCustomRepository templateCustomRepository;
  private final TemplateRepository templateRepository;
  private final CustomRepository customRepository;

  public void saveBulk(
    String userId,
    String customId,
    List<TemplateCustom.TemplateCustomReq> reqList
  ) {
    // 1. Custom 게시물 생성 또는 조회
    CustomEntity custom = customRepository.findById(customId)
            .orElseGet(() -> customRepository.save(CustomEntity.builder()
                    .customId(customId)
                    .userId(userId)
                    .activeYn(TemplateCustom.ActiveYn.N)
                    .build()));

    // 2. 블록들 생성 및 매핑
    List<CustomTemplateEntity> mappings = reqList.stream()
      .map(req -> {
        boolean existTemplate = this.templateRepository.existsByTemplateIdAndActiveYnAndDeletedAtIsNull(req.templateId(), Template.ActiveYn.Y);
        if (!existTemplate) {
          throw new TemplateException.NoTemplate();
        }

        // 새로운 블록 생성
        TemplateCustomEntity block = templateCustomRepository.save(TemplateCustomEntity.builder()
          .userId(userId)
          .templateId(req.templateId())
          .customSchema(req.customSchema())
          .build());

        return CustomTemplateEntity.builder()
          .customId(customId)
          .templateCustomId(block.getTemplateCustomId())
          .sortOrder(req.sortOrder())
          .build();
      })
      .toList();

    custom.getCustomTemplates().addAll(mappings);
    this.customRepository.save(custom);
  }
}
