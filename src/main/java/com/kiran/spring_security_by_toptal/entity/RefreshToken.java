package com.kiran.spring_security_by_toptal.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.Data;

@Data
@Document
public class RefreshToken {

    @Id
    private String refreshTokenId;

    @DocumentReference(lazy = true)
    private final User user;
}
