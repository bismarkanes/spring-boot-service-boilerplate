package com.bismark.serviceboilerplate.repository;

import com.bismark.serviceboilerplate.Entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    List<UserDetail> findByUsername(String username);
}
