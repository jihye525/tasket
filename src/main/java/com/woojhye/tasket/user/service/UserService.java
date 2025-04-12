package com.woojhye.tasket.user.service;

import com.woojhye.tasket.file.domain.Profile;
import com.woojhye.tasket.user.domain.UserEntity;
import com.woojhye.tasket.user.dto.SignUpDTO;
import com.woojhye.tasket.user.dto.UserUpdateDTO;
import com.woojhye.tasket.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void signUpProcess(SignUpDTO signUpDTO, Profile profile) {
        if (userRepository.existsByEmailAndDeletedFalse(signUpDTO.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(signUpDTO.getPassword());
        UserEntity user = SignUpDTO.toEntity(signUpDTO, encodedPassword);

        if (profile != null) {
            profile.setUser(user);
            user.setProfile(profile);
        }

        userRepository.save(user);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void updateUser(String email, UserUpdateDTO dto) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (dto.getPassword() != null){
            user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        }

        if(dto.getNickname() != null){
            user.setNickname(dto.getNickname());
        }

    }

    @Transactional
    public void deleteUser(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.setDeleted(true);

        userRepository.save(user);
    }
}
