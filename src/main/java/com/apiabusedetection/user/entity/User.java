package com.apiabusedetection.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;
    @Column(nullable = false, unique = true, length = 50)
            @Size(min = 3, max = 50, message = "USERNAME_INVALID")
     String username;
    @Column(nullable = false, unique = true, length = 100)
    String email;
    @Column(name = "password_hash", nullable = false, length = 255)
            @Size(min = 8, message = "INVALID_PASSWORD")
    String passwordHash;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    Role role;
    @Column(nullable = false)
    boolean enabled = true;
    @Column(name = "created_at", insertable = false, updatable = false)
    LocalDateTime createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false)
     LocalDateTime updatedAt;


}
