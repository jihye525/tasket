package com.woojhye.tasket.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpDTO {
    private String email;
    private String password;
    private String nickname;
    private String profile;
    private String role;
}