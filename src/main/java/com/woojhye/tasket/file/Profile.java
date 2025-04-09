package com.woojhye.tasket.file;

import com.woojhye.tasket.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Getter
@NoArgsConstructor
@Entity(name = "profile")
public class Profile {
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

    @Column(name = "delete_yn")
    private String deleteYn ="N";

    @Builder
    public Profile(String originalName, String storeName, long size) {
        this.originalName = originalName;
        this.storeName = storeName;
        this.size = size;
    }

}