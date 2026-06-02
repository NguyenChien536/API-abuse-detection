package com.apiabusedetection.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@Table(name = "invalidated_token")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvalidatedToken {
    @Id
    @Column(length = 36, nullable = false)
    String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_time", nullable = false)
    Date expiryTime;
}
