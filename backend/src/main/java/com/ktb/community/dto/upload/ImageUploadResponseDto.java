package com.ktb.community.dto.upload;

import lombok.Getter;

@Getter
public class ImageUploadResponseDto {

    private final String imageUrl;

    public ImageUploadResponseDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
