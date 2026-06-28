package com.ktb.community.controller;

import com.ktb.community.annotation.LoginUser;
import com.ktb.community.common.ApiResponse;
import com.ktb.community.dto.comments.CommentListResponseDto;
import com.ktb.community.dto.comments.CommentRequestDto;
import com.ktb.community.dto.comments.CommentResponseDto;
import com.ktb.community.dto.comments.CommentUpdateResponseDto;
import com.ktb.community.entity.User;
import com.ktb.community.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
            @LoginUser User loginUser,
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDto request
    ) {
        CommentResponseDto response = commentService.createComment(loginUser, postId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("create_comment_success", response));
    }

    @PostMapping("/comments/{commentId}/replies")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createReply(
            @LoginUser User loginUser,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto request
    ) {
        CommentResponseDto response = commentService.createReply(loginUser, commentId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("create_reply_success", response));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommentListResponseDto>>> getCommentsList(
            @PathVariable Long postId
    ) {
        List<CommentListResponseDto> response = commentService.getCommentsList(postId);

        return ResponseEntity.ok(
                new ApiResponse<>("get_comments_success", response)
        );
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentUpdateResponseDto>> updateComment(
            @LoginUser User loginUser,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto request
    ) {
        CommentUpdateResponseDto response = commentService.updateComment(loginUser, commentId, request);

        return ResponseEntity.ok(
                new ApiResponse<>("update_comment_success", response)
        );
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteComment(
            @LoginUser User loginUser,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(loginUser, commentId);

        return ResponseEntity.ok(
                new ApiResponse<>("delete_comment_success", true)
        );
    }
}
