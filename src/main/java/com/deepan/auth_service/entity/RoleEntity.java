package com.deepan.auth_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_sequence",
            allocationSize = 50
    )
    private Long id;

    // Store roles like: ROLE_USER, ROLE_ADMIN
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    // getters/setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}