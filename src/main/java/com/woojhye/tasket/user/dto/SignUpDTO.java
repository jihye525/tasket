package com.woojhye.tasket.user.dto;

import com.woojhye.tasket.user.domain.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class SignUpDTO {

    private String email;
    private String password;
    private String nickname;
    private String profile;

    public static User toEntity(SignUpDTO signUpDTO, String encodedPassword){
        return User.builder()
                .email(signUpDTO.getEmail())
                .password(encodedPassword)
                .nickname(signUpDTO.getNickname())
                .profile(signUpDTO.getProfile().toString())
                .role("ROLE_USER")
                .build();
    }
}