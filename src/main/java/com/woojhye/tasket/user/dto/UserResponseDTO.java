package com.woojhye.tasket.user.dto;

import jakarta.persistence.Column;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long user_id;
    private String email;
    private String password;
    private String nickname;
    private String profile;
    private String role;
}
