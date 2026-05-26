package com.apiabusedetection.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {

    @Id
    @Column(nullable = false, unique = true, length = 50)
    String name;

    @Column(nullable = false, length = 100)
    String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_name", referencedColumnName = "name"),
            inverseJoinColumns = @JoinColumn(name = "permission_name", referencedColumnName = "name")
    )
    @Builder.Default
    Set<Permission> permissions = new HashSet<>();
}
