package kr.co.onmediagroup.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Map;

import static kr.co.onmediagroup.util.JacksonUtils.OBJECT_MAPPER;

@Converter
public class StringMapConverter implements AttributeConverter<Map<String, String>, String> {
  @Override
  public String convertToDatabaseColumn(Map attribute) {
    if (attribute == null) {
      return null;
    }

    try {
      return  OBJECT_MAPPER.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to convert Map to JSON", e);
    }
  }

  @Override
  public Map<String, String> convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }

    try {
      return OBJECT_MAPPER.readValue(dbData, Map.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to convert JSON to Map", e);
    }
  }
}
