package com.qma.uc18.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String name;

    @Column
    private String provider; // "local" or "google"

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles;

    protected User() {}

    public User(String email, String password, String name, String provider, Set<String> roles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.provider = provider;
        this.roles = roles;
    }

    public Long getId()           { return id; }
    public String getEmail()      { return email; }
    public String getPassword()   { return password; }
    public String getName()       { return name; }
    public String getProvider()   { return provider; }
    public Set<String> getRoles() { return roles; }
    public void setPassword(String p) { this.password = p; }
}
