package kr.co.onmediagroup.template.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

public class TemplateCustom {

  @Data
  public static class TemplateCustomRes {
    private Integer templateCustomId;
    private String customId;
    private Integer templateId;
    private Integer sortOrder;
    private String customSchema;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
  }
}
