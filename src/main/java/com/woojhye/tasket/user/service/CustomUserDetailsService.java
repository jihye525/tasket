package com.woojhye.tasket.user.service;

import com.woojhye.tasket.user.domain.User;
import com.woojhye.tasket.user.dto.CustomUserDetails;
import com.woojhye.tasket.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User userData = userRepository.findByEmail(email);    // 해당 이메일이 존재하면, userData 변수에 저장됨

        if (userData == null) {
            throw new UsernameNotFoundException("해당 이메일을 가진 사용자가 없습니다: " + email);
        }

        return new CustomUserDetails(userData);
    }

}