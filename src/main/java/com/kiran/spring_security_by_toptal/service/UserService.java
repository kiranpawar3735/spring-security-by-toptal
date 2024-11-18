package com.kiran.spring_security_by_toptal.service;

import com.kiran.spring_security_by_toptal.beans.SignUpRequest;
import com.kiran.spring_security_by_toptal.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> createUser(User user);

    Optional<User> getUser(String userId);

    Optional<List<User>> getAllUser();

    Optional<User> updateUser(String userId, User user);

    Optional<Boolean> deleteUser(String userId, User user);
}