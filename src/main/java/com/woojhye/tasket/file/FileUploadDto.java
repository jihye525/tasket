package com.woojhye.tasket.file;


import lombok.Builder;
import lombok.Getter;

@Getter
public class FileUploadDto {

    private Long id; // 파일 번호 (PK)
    private final String originalName; // 원본 파일명
    private final String storeName; // 저장 파일명
    private final long size; // 파일 크기

    @Builder
    public FileUploadDto(String originalName, String storeName, long size) {
        this.originalName = originalName;
        this.storeName = storeName;
        this.size = size;
    }

    public Profile toEntity() {
        return Profile.builder()
                .originalName(originalName)
                .storeName(storeName)
                .size(size)
                .build();
    }
}