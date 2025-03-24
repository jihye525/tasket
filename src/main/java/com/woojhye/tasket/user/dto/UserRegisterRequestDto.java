package com.woojhye.tasket.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequestDto {
    private String email;
    private String password;
    private String nickname;
    private String profile;
} 