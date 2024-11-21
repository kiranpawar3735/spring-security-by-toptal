package com.kiran.spring_security_by_toptal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kiran.spring_security_by_toptal.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

    public void deleteByUser_UserId(String userId);
}