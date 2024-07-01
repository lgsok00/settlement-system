package com.sparta.settlementsystem.user.repository;

import com.sparta.settlementsystem.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
