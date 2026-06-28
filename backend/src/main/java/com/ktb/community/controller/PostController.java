package com.ktb.community.controller;

import com.ktb.community.annotation.LoginUser;
import com.ktb.community.common.ApiResponse;
import com.ktb.community.dto.likes.LikeResponseDto;
import com.ktb.community.dto.post.PostCreateResponseDto;
import com.ktb.community.dto.post.PostDetailResponseDto;
import com.ktb.community.dto.post.PostListResponseDto;
import com.ktb.community.dto.post.PostRequestDto;
import com.ktb.community.dto.post.PostUpdateResponseDto;
import com.ktb.community.dto.report.ReportRequestDto;
import com.ktb.community.dto.report.ReportResponseDto;
import com.ktb.community.entity.User;
import com.ktb.community.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostCreateResponseDto>> createPost(
            @LoginUser User loginUser,
            @Valid @RequestBody PostRequestDto request
    ) {
        PostCreateResponseDto response = postService.createPost(loginUser, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>("create_post_success", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostListResponseDto>>> getPostList() {
        List<PostListResponseDto> response = postService.getPostList();

        return ResponseEntity.ok(
                new ApiResponse<>("get_posts_success", response)
        );
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponseDto>> getPostDetail(
            @LoginUser User loginUser,
            @PathVariable Long postId
    ) {
        PostDetailResponseDto response = postService.getPostDetail(loginUser, postId);

        return ResponseEntity.ok(
                new ApiResponse<>("get_post_detail_success", response)
        );
    }

    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostUpdateResponseDto>> updatePost(
            @LoginUser User loginUser,
            @PathVariable Long postId,
            @Valid @RequestBody PostRequestDto request
    ) {
        PostUpdateResponseDto response = postService.updatePost(loginUser, postId, request);

        return ResponseEntity.ok(
                new ApiResponse<>("update_post_success", response)
        );
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Boolean>> deletePost(
            @LoginUser User loginUser,
            @PathVariable Long postId
    ) {
        postService.deletePost(loginUser, postId);

        return ResponseEntity.ok(
                new ApiResponse<>("delete_post_success", true)
        );
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<LikeResponseDto>> likePost(
            @LoginUser User loginUser,
            @PathVariable Long postId
    ) {
        LikeResponseDto response = postService.likePost(loginUser, postId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("like_post_success", response));
    }

    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<LikeResponseDto>> unlikePost(
            @LoginUser User loginUser,
            @PathVariable Long postId
    ) {
        LikeResponseDto response = postService.unlikePost(loginUser, postId);

        return ResponseEntity.ok(
                new ApiResponse<>("unlike_post_success", response)
        );
    }

    @PostMapping("/{postId}/reports")
    public ResponseEntity<ApiResponse<ReportResponseDto>> reportPost(
            @LoginUser User loginUser,
            @PathVariable Long postId,
            @Valid @RequestBody ReportRequestDto request
    ) {
        ReportResponseDto response = postService.reportPost(loginUser, postId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("report_post_success", response));
    }
}
