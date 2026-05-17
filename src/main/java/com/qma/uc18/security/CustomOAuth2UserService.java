package com.qma.uc18.security;

import com.qma.uc18.model.User;
import com.qma.uc18.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

/**
 * Loads OAuth2 user from Google, saves/updates in MySQL.
 * On first login, creates the user with a random password (unusable for local login).
 * NOTE: PasswordEncoder is instantiated directly (not autowired) to avoid circular
 * dependency with SecurityConfig → CustomOAuth2UserService → PasswordEncoder → SecurityConfig.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired private UserRepository userRepository;
    // Direct instantiation — breaks circular dep with SecurityConfig
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name  = oAuth2User.getAttribute("name");

        // Create or update the user in our DB
        userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User(
                email,
                passwordEncoder.encode(UUID.randomUUID().toString()), // random unusable password
                name != null ? name : email,
                "google",
                Set.of("USER")
            );
            return userRepository.save(newUser);
        });

        return oAuth2User;
    }
}
