package kr.co.onmediagroup.template.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class TemplateCustom {
  @Getter
  @AllArgsConstructor
  public enum ActiveYn {
    Y("Y"),
    N("N");
    private final String activeYn;
  }

  @Data
  public static class TemplateCustomRes {
    private Integer templateCustomId;
    private String customId;
    private String userId;
    private Integer templateId;
    private Integer sortOrder;
    private Map<String, String> customSchema;
    private List<TemplateCustomImageRes> images;
    private TemplateCustom.ActiveYn activeYn;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
  }

  @Data
  public static class TemplateCustomImageRes {
    private Integer templateCustomImageId;
    private String imageUrl;
    private Integer sortOrder;
  }

  public record TemplateCustomReq(
    @NotNull Integer templateId,
    @NotNull Integer sortOrder,
    @NotBlank Map<String, String> customSchema
  ){
  }

  public record TemplateCustomCreateReq(
    @NotBlank String customId,
    List<TemplateCustom.TemplateCustomReq> templateCustomReqList
  ){
  }
}
