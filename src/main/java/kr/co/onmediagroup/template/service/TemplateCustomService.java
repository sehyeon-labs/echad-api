package kr.co.onmediagroup.template.service;

import kr.co.onmediagroup.template.exception.TemplateException;
import kr.co.onmediagroup.template.model.dto.Template;
import kr.co.onmediagroup.template.model.dto.TemplateCustom;
import kr.co.onmediagroup.template.model.entity.TemplateCustomEntity;
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

  // 커스텀 템플릿 조회
  public List<TemplateCustom.TemplateCustomRes> findByCustomId(
    String customId,
    String userId
  ) {
    // 활동 상태의 템플릿 확인
    List<TemplateCustomEntity> entityList = this.templateCustomRepository
      .findByCustomIdWithActiveTemplate(customId, userId, Template.ActiveYn.Y, TemplateCustom.ActiveYn.Y);

    if (entityList.isEmpty()) {
      throw new TemplateException.NoTemplate();
    }

    return entityList.stream()
      .map(entity -> MODEL_MAPPER.map(entity, TemplateCustom.TemplateCustomRes.class))
      .toList();
  }

  // 커스텀 템플릿 벌크 저장
  public void saveBulk(
    String userId,
    String customId,
    List<TemplateCustom.TemplateCustomReq> reqList
  ) {
    // 입력받은 리스트 내에서 sortOrder 중복 여부 확인
    List<Integer> sortOrders = reqList.stream()
      .map(TemplateCustom.TemplateCustomReq::sortOrder)
      .toList();

    if (sortOrders.size() != sortOrders.stream().distinct().count()) {
      throw new TemplateException("duplicate sortOrder in request");
    }

    // DB에 이미 존재하는 (userId, customId, sortOrder) 인지 확인
    boolean existConflict = this.templateCustomRepository.existsByUserIdAndCustomIdAndSortOrderInAndDeletedAtIsNull(
      userId,
      customId,
      sortOrders
    );

    if (existConflict) {
      throw new TemplateException.AlreadyExistTemplate();
    }

    // 새로운 데이터 생성 및 저장
    List<TemplateCustomEntity> entityList = reqList.stream()
      .map(req -> {
        // 템플릿 유효성 검사
        boolean existTemplate = this.templateRepository.existsByTemplateIdAndActiveYnAndDeletedAtIsNull(req.templateId(), Template.ActiveYn.Y);
        if (!existTemplate) {
          throw new TemplateException.NoTemplate();
        }

        return TemplateCustomEntity.builder()
          .userId(userId)
          .customId(customId)
          .templateId(req.templateId())
          .sortOrder(req.sortOrder())
          .customSchema(req.customSchema())
          .build();
      })
      .toList();

    this.templateCustomRepository.saveAll(entityList);
  }
}
