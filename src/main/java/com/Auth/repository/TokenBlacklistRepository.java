package com.Auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Auth.entity.TokenBlacklist;



public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    boolean existsByToken(String token);
}

