package com.woojhye.tasket.user.service;

import com.woojhye.tasket.user.domain.User;
import com.woojhye.tasket.user.dto.SignUpDTO;
import com.woojhye.tasket.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public void signUpProcess(SignUpDTO signUpDTO) {
        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        signUpDTO.setPassword(bCryptPasswordEncoder.encode(signUpDTO.getPassword()));

        User user = SignUpDTO.toEntity(signUpDTO);

        userRepository.save(user);
    }
}
