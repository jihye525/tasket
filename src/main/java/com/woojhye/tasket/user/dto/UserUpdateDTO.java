package com.woojhye.tasket.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    private String password;
    private String confirmPassword;
    private String nickname;
    //private String profile;      // 이미지 업로드 기능 완성되면 자료형 MultipartFile로 수정
}
