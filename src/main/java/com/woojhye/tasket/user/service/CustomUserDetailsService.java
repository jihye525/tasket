package com.woojhye.tasket.user.service;

import com.woojhye.tasket.user.domain.User;
import com.woojhye.tasket.user.dto.CustomUserDetails;
import com.woojhye.tasket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User userData = userRepository.findByEmail(email);    // 해당 이메일이 존재하면, userData 변수에 저장됨

        if (userData == null) {
            throw new UsernameNotFoundException("해당 이메일을 가진 사용자가 없습니다: " + email);
        }

        System.out.println("✅ 사용자 조회 성공: " + email);
        System.out.println("🔐 저장된 암호화된 비밀번호: " + userData.getPassword());
        System.out.println("🎭 권한: " + userData.getRole());

        return new org.springframework.security.core.userdetails.User(userData.getEmail(), userData.getPassword(), List.of(new SimpleGrantedAuthority(userData.getRole())));
    }

}