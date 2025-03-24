package com.woojhye.tasket.user.service;

import com.woojhye.tasket.user.domain.User;
import com.woojhye.tasket.user.dto.UserDto;
import com.woojhye.tasket.user.dto.UserRegisterRequestDto;
import com.woojhye.tasket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public User registerUser(UserRegisterRequestDto registerDto) {
        // 이메일 중복 체크
        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }


        // 사용자 생성
        User user = new User(
            registerDto.getEmail(),
            registerDto.getPassword(),
            registerDto.getNickname(),
                registerDto.getProfile()
        );
        return userRepository.save(user);
    }

    public void loginUser(UserDto userLoginDto) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException(userLoginDto.getEmail() + "NOT FOUND")));

    }
}
