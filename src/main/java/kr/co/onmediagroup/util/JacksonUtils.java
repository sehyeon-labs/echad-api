package kr.co.onmediagroup.util;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson 전역 설정 유틸리티 클래스
 *
 * ObjectMapper 인스턴스를 전역에서 재사용할 수 있도록 제공
 *
 * 적용 포맷:
 * - LocalDate     → "yyyy-MM-dd"
 * - LocalTime     → "HH:mm:ss"  (0~23 시각)
 * - LocalDateTime → "yyyy-MM-dd'T'HH:mm:ss"
 *
 * */
public class JacksonUtils {
  public static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

  public static ObjectMapper createObjectMapper() {
    return new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .registerModule(javaDateTimeModule())
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // 알 수 없는 JSON 속성 무시
      .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false) // 무시된 필드 예외 무시
      ;
  }

  public static Module javaDateTimeModule() {
    SimpleModule module = new SimpleModule();

    // LocalDate -> "2024-07-26"
    module.addSerializer(LocalDate.class, new JsonSerializer<>() {
      @Override
      public void serialize(
        LocalDate localDate,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
      )
        throws IOException {
        jsonGenerator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate));
      }
    });

    // LocalTime -> "15:04:28"
    module.addSerializer(LocalTime.class, new JsonSerializer<>() {
      @Override
      public void serialize(
        LocalTime localTime,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
      )
        throws IOException {
        jsonGenerator.writeString(DateTimeFormatter.ofPattern("HH:mm:ss").format(localTime));
      }
    });

    // LocalDateTime -> "2024-07-26T15:04:28"
    module.addSerializer(LocalDateTime.class, new JsonSerializer<>() {
      @Override
      public void serialize(
        LocalDateTime localDateTime,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
      ) throws IOException {
        jsonGenerator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(localDateTime));
      }
    });

    return module;
  }
}
