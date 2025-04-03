package com.woojhye.tasket.user.service;

import com.woojhye.tasket.user.domain.User;
import com.woojhye.tasket.user.dto.SignUpDTO;
import com.woojhye.tasket.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public void signUpProcess(SignUpDTO signUpDTO) {

        //db에 이미 동일한 email을 가진 회원이 존재하는지?
        boolean isUser = userRepository.existsByEmail(signUpDTO.getEmail());
        if (isUser) {
            return;
        }

//        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
//            throw new RuntimeException("이미 존재하는 이메일입니다.");
//        }     // 위의 코드 개선된 버전

        User data = new User();

        data.setEmail(signUpDTO.getEmail());
        data.setPassword(bCryptPasswordEncoder.encode(signUpDTO.getPassword()));
        data.setNickname(signUpDTO.getNickname());
        data.setRole("ROLE_USER");

        userRepository.save(data);
    }
}
