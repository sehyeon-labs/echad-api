package kr.co.onmediagroup.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

import static kr.co.onmediagroup.util.JacksonUtils.OBJECT_MAPPER;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

  @Override
  public String convertToDatabaseColumn(List<String> attribute) {
    try {
      return attribute == null ? null :OBJECT_MAPPER.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("JSON writing error", e);
    }
  }

  @Override
  public List<String> convertToEntityAttribute(String dbData) {
    try {
      return dbData == null ? null : OBJECT_MAPPER.readValue(dbData, new TypeReference<List<String>>() {});
    } catch (IOException e) {
      throw new IllegalArgumentException("JSON reading error", e);
    }
  }
}
