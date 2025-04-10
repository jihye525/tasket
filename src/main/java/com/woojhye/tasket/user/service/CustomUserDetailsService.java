package com.woojhye.tasket.user.service;

import com.woojhye.tasket.user.domain.UserEntity;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> _userData = userRepository.findByEmail(email);

        if (_userData.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        UserEntity userData = _userData.get();

        // 탈퇴한 유저 로그인 제한
        if (!userData.isActive()) {
            throw new UsernameNotFoundException("탈퇴한 회원입니다.");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("ROLE_ADMIN".equals(userData.getRole())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return new org.springframework.security.core.userdetails.User(
                userData.getEmail(),
                userData.getPassword(),
                authorities
        );
    }
}
