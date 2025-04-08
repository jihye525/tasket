package com.woojhye.tasket.user.dto;

import com.woojhye.tasket.user.domain.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpDTO {


    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String confirmPassword;

    @NotEmpty(message = "닉네임은 필수항목입니다.")
    @Size(min = 3, max = 25)
    private String nickname;

    private String profile;

    public static UserEntity toEntity(SignUpDTO signUpDTO, String encodedPassword){
        return UserEntity.builder()
                .email(signUpDTO.getEmail())
                .password(encodedPassword)
                .nickname(signUpDTO.getNickname())
                .profile(signUpDTO.getProfile().toString())
                .role("ROLE_USER")
                .active(true)
                .build();
    }

}