package com.kiran.spring_security_by_toptal.service;

import com.kiran.spring_security_by_toptal.entity.User;
import com.kiran.spring_security_by_toptal.repository.RefreshTokenRepository;
import com.kiran.spring_security_by_toptal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public Optional<User> createUser(User user) {
        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> getUser(String userId) {
        return null;
    }

    @Override
    public Optional<List<User>> getAllUser() {
        return null;
    }

    @Override
    public Optional<User> updateUser(String userId, User user) {
        return null;
    }

    @Override
    public Optional<Boolean> deleteUser(String userId, User user) {
        return null;
    }
}
