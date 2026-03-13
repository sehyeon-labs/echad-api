package kr.co.onmediagroup.template.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Post {
  @Getter
  @AllArgsConstructor
  public enum ActiveYn {
    Y("Y"),
    N("N");
    private final String activeYn;
  }

  @Data
  public static class PostRes {
    private String postId;
    private String userId;
    private Post.ActiveYn activeYn;
    private LocalDateTime deletedAt;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<PostBlockRes> content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
  }

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

  public record PostCreateReq(
    @NotBlank String customId,
    List<Post.PostBlockReq> postBlockReqList
  ){
  }
}
