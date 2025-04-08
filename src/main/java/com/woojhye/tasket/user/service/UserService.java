package com.woojhye.tasket.user.service;

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

    public void signUpProcess(SignUpDTO signUpDTO) {
        //active = true인 사용자만 중복 검사
        if (userRepository.existsByEmailAndActiveTrue(signUpDTO.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(signUpDTO.getPassword());

        UserEntity user = SignUpDTO.toEntity(signUpDTO, encodedPassword);

        userRepository.save(user);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void updateUser(String email, UserUpdateDTO dto) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.setActive(false); // soft delete 처리
    }
}
