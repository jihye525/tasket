package com.woojhye.tasket.user.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private UserResponseDTO userResponseDTO;
    public CustomUserDetails(UserResponseDTO dto, Collection<? extends GrantedAuthority> authorities) {
        super(dto.getEmail(), dto.getPassword(), authorities);
        this.userResponseDTO =dto;
    }

    public UserResponseDTO getUserResposeDTO() {
        return userResponseDTO;
    }

}