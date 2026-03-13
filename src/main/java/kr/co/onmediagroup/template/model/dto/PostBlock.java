package kr.co.onmediagroup.template.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

public class PostBlock {

  @Data
  public static class PostBlockRes {
    private Integer postBlockId;
    private Integer sortOrder;
    private PostBlockDetailRes detail;
  }

  @Data
  public static class PostBlockDetailRes {
    private Integer templateId;
    private Map<String, String> customSchema;
    private List<PostBlockImageRes> images;
  }

  @Data
  public static class PostBlockImageRes {
    private Integer postBlockImageId;
    private String imageUrl;
    private Integer sortOrder;
  }

  public record PostBlockReq(
    @NotNull Integer templateId,
    @NotNull Integer sortOrder,
    @NotNull Map<String, String> customSchema
  ){
  }

  public record PostBlockOrderReq(
    @NotNull Integer postBlockId,
    @NotNull Integer sortOrder
  ) {
  }

  public record PostBlockSwapReq(
    @NotNull Integer postBlockId1,
    @NotNull Integer postBlockId2
  ) {
  }
}
