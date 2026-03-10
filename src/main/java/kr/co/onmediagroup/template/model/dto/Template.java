package kr.co.onmediagroup.template.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

public class Template {

  @Getter
  @AllArgsConstructor
  public enum Component {
    HEADER("HEADER"),
    TEXT("TEXT"),
    BUTTON("BUTTON"),
    IMG("IMG"),
    CONTENT("CONTENT"),
    LOCATION("LOCATION");
    private final String component;
  }

  @Getter
  @AllArgsConstructor
  public enum IsPremium {
    Y("Y"),
    N("N");
    private final String isPremium;
  }

  @Getter
  @AllArgsConstructor
  public enum ActiveYn {
    Y("Y"),
    N("N");
    private final String activeYn;
  }

  @Data
  public static class TemplateModel {
    private Integer templateId;
    private String title;
    private Component component;
    private Integer componentType;
    private String previewUrl;
    private IsPremium isPremium;
    private String templateSchema;
    private Integer sortOrder;
    private ActiveYn activeYn;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
  }

  @Data
  public static class TemplateResponse {
    private Integer templateId;
    private String title;
    private Component component;
    private Integer componentType;
    private String previewUrl;
    private IsPremium isPremium;
    private String templateSchema;
    private Integer sortOrder;
    private ActiveYn activeYn;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
  }

  public record TemplateReq(
    @NotBlank @Size(max = 255) String title,
    @NotNull Component component,
    @NotNull Integer componentType,
    @Size(max = 255) String previewUrl,
    IsPremium isPremium,
    @NotBlank String templateSchema,
    Integer sortOrder
  ){
  }
}
