package kr.co.onmediagroup.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * ModelMapper 설정 클래스
 *
 `* Model Mapper는 스프링 빈으로 사용하지 않고, 단순 전역 스태틱 변수를 참조하여 사용
 `* 매퍼 파편화 개선, 불필요한 의존성 주입을 개선하고, 사용성의 편의를 위함
 `*/
@Component
public class ModelConverter {
  public static final ModelMapper MODEL_MAPPER = ModelConverter.createModelMapper();

  @Bean
  public ModelMapper modelMapper() { return ModelConverter.createModelMapper(); }

  public static ModelMapper createModelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    return modelMapper;
  }
}
