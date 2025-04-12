package com.woojhye.tasket.file.repository;

import com.woojhye.tasket.file.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUserEmailAndDeletedFalse(String email);
}
