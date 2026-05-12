package com.qma.auth.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email    = email;
        this.password = password;
    }

    public Long getId()                { return id; }
    public String getUsername()        { return username; }
    public void setUsername(String u)  { this.username = u; }
    public String getEmail()           { return email; }
    public void setEmail(String e)     { this.email = e; }
    public String getPassword()        { return password; }
    public void setPassword(String p)  { this.password = p; }
    public Role getRole()              { return role; }
    public void setRole(Role r)        { this.role = r; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
}
