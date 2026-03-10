package kr.co.onmediagroup.template.model.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

public class TemplateCustom {

  @Data
  public static class TemplateCustomRes {
    private Integer templateCustomId;
    private String customId;
    private Integer templateId;
    private Integer sortOrder;
    private String customSchema;
    private List<TemplateCustomImageRes> images;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
  }

  @Data
  public static class TemplateCustomImageRes {
    private Integer templateCustomImageId;
    private String imageUrl;
    private Integer sortOrder;
  }
}
