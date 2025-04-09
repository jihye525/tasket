package com.woojhye.tasket.file;

import com.woojhye.tasket.error.StorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    @Value("${file.dir}")
    private String filePath;

    private final ProfileRepository profileRepository;

    @Transactional
    public void store(MultipartFile file) {
        FileUploadDto fileDto = saveFile(file);
        if (fileDto != null) {
            profileRepository.save(fileDto.toEntity());
        }
    }

    // 파일 업로드
    private FileUploadDto saveFile(MultipartFile file) {
        FileUploadDto dto = null;
        if (file != null) {
            String originalName = file.getOriginalFilename();
            String storeName = UUID.randomUUID() + "_" + originalName;
            long size = file.getSize();

            try {
                // 파일 업로드
                log.info("file path : {}", filePath);
                File localFile = new File(filePath + "/" + storeName);
                file.transferTo(localFile);
                dto = FileUploadDto.builder()
                        .originalName(originalName)
                        .storeName(storeName)
                        .size(size)
                        .build();
                log.info("파일 저장 완료: {}", localFile.getCanonicalPath());
            } catch (IllegalStateException | IOException e) {
                throw new StorageException(e.getMessage());
            }
        }
        return dto;
    }
}