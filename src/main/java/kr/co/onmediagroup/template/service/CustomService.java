package kr.co.onmediagroup.template.service;

import kr.co.onmediagroup.template.exception.TemplateException;
import kr.co.onmediagroup.template.model.dto.Template;
import kr.co.onmediagroup.template.model.dto.TemplateCustom;
import kr.co.onmediagroup.template.model.entity.CustomEntity;
import kr.co.onmediagroup.template.model.entity.CustomTemplateEntity;
import kr.co.onmediagroup.template.model.entity.TemplateCustomEntity;
import kr.co.onmediagroup.template.repository.CustomRepository;
import kr.co.onmediagroup.template.repository.TemplateCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.onmediagroup.util.ModelConverter.MODEL_MAPPER;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomService {
    private final CustomRepository customRepository;
    private final TemplateCustomRepository templateCustomRepository;

    public TemplateCustom.CustomRes findByCustomId(String customId, String userId) {
        CustomEntity custom = customRepository.findByCustomIdAndUserIdAndDeletedAtIsNull(customId, userId)
                .orElseThrow(TemplateException.NoTemplate::new);

        List<CustomTemplateEntity> mappings = custom.getCustomTemplates();
        List<Integer> blockIds = mappings.stream()
                .map(CustomTemplateEntity::getTemplateCustomId)
                .toList();

        List<TemplateCustomEntity> blocks = templateCustomRepository.findAllWithDetailsByIds(blockIds, Template.ActiveYn.Y);
        Map<Integer, TemplateCustomEntity> blockMap = blocks.stream()
                .collect(Collectors.toMap(TemplateCustomEntity::getTemplateCustomId, b -> b));

        TemplateCustom.CustomRes res = MODEL_MAPPER.map(custom, TemplateCustom.CustomRes.class);

        List<TemplateCustom.CustomTemplateRes> content = mappings.stream()
                .filter(m -> blockMap.containsKey(m.getTemplateCustomId()))
                .map(m -> {
                    TemplateCustom.CustomTemplateRes ctr = new TemplateCustom.CustomTemplateRes();
                    ctr.setTemplateCustomId(m.getTemplateCustomId());
                    ctr.setSortOrder(m.getSortOrder());
                    ctr.setTemplateCustom(MODEL_MAPPER.map(blockMap.get(m.getTemplateCustomId()), TemplateCustom.TemplateCustomBlockRes.class));
                    return ctr;
                })
                .toList();

        res.setContent(content);
        return res;
    }
}
