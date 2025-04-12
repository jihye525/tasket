package com.woojhye.tasket.file.service;

import com.woojhye.tasket.error.StorageException;
import com.woojhye.tasket.file.domain.Profile;
import com.woojhye.tasket.file.repository.ProfileRepository;
import com.woojhye.tasket.user.domain.UserEntity;
import com.woojhye.tasket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    @Value("${file.dir}")
    private String filePath;

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Transactional
    public Profile store(MultipartFile file) {
        Profile profile = saveFile(file);
        if (profile != null) {
            return profileRepository.save(profile);
        }
        return null;
    }

    // 파일 업로드
    private Profile saveFile(MultipartFile file) {
        Profile profile = null;
        if (file != null) {
            String originalName = file.getOriginalFilename();
            String storeName = UUID.randomUUID() + "_" + originalName;
            long size = file.getSize();

            try {
                File localFile = new File(filePath + "/" + storeName);
                file.transferTo(localFile);
                profile = Profile.builder()
                        .originalName(originalName)
                        .storeName(storeName)
                        .size(size)
                        .build();
            } catch (IllegalStateException | IOException e) {
                throw new StorageException(e.getMessage());
            }
        }
        return profile;
    }

    @Transactional
    public void update(String email, MultipartFile file) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + "NOT FOUND"));

        Profile profile = saveFile(file);
        profile.setUser(userEntity);
        userEntity.setProfile(profile);
        profileRepository.save(profile);
    }
    @Transactional
    public void deleteProfile(String email){
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + "NOT FOUND"));
        Profile existingProfile = userEntity.getProfile();

        if (existingProfile != null) {
            existingProfile.setDeleted(true);
            userEntity.setProfile(null);
            existingProfile.setUser(null);

            profileRepository.save(existingProfile);
        }

        userRepository.save(userEntity);
    }
}