package com.qma.auth.security;

import com.qma.auth.model.User;
import com.qma.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired private UserRepository userRepository;

    /**
     * Loads user by email (email is used as the Spring Security "username").
     * Called by DaoAuthenticationProvider during the JWT validation chain.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("No user with email: " + email));

        // Guard against legacy OAuth rows that have no password stored.
        // Such accounts cannot log in via password — they must re-register.
        String password = user.getPassword();
        if (password == null || password.isBlank()) {
            throw new UsernameNotFoundException(
                "Account has no password set. Please register again.");
        }

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            password,
            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
