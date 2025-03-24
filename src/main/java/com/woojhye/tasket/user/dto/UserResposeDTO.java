package com.woojhye.tasket.user.dto;


import com.woojhye.tasket.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResposeDTO {
    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private String profile;

    public UserResposeDTO(User entity){
        this.userId = entity.getUserId();
        this.email = entity.getEmail();
        this.password = entity.getPassword();
        this.nickname = entity.getNickname();
        this.profile = entity.getProfile();

    }
}
