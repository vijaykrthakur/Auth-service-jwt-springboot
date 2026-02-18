package com.Auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Auth.entity.RefreshToken;
import com.Auth.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	 Optional<RefreshToken> findByToken(String token);
	    void deleteByUser(User user);
}