package com.woojhye.tasket.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    private Long userId;

    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    private String profile;

    public User(String email, String password, String nickname, String profile) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profile = profile;
    }
}

