package com.woojhye.tasket.user.dto;

import com.woojhye.tasket.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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

    private MultipartFile profile;
    public static User toEntity(SignUpDTO signUpDTO, String encodedPassword){
        return User.builder()
                .email(signUpDTO.getEmail())
                .password(encodedPassword)
                .nickname(signUpDTO.getNickname())
                .role("ROLE_USER")
                .build();
    }
}