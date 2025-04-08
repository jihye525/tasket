package com.woojhye.tasket.user.service;

import com.woojhye.tasket.user.domain.User;
import com.woojhye.tasket.user.dto.SignUpDTO;
import com.woojhye.tasket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;


    public void signUpProcess(SignUpDTO signUpDTO) {
        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(signUpDTO.getPassword());

        User user = SignUpDTO.toEntity(signUpDTO, encodedPassword);

        userRepository.save(user);
    }
}
