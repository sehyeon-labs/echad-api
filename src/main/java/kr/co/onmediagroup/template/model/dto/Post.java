package kr.co.onmediagroup.template.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<PostBlock.PostBlockRes> content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
  }

  public record PostCreateReq(
    @NotBlank String customId,
    List<PostBlock.PostBlockReq> postBlockReqList
  ){
  }
}
