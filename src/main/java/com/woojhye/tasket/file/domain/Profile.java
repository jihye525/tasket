package com.woojhye.tasket.file.domain;

import com.woojhye.tasket.base.BaseEntity;
import com.woojhye.tasket.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Getter
@NoArgsConstructor
@Entity(name = "profile")
public class Profile extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private long id;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "size", nullable = false)
    private long size;

    @Setter
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    public Profile(String originalName, String storeName, long size) {
        this.originalName = originalName;
        this.storeName = storeName;
        this.size = size;
    }

}