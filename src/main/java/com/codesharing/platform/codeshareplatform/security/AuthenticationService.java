package com.codesharing.platform.codeshareplatform.security;

import com.codesharing.platform.codeshareplatform.model.Users;
import com.codesharing.platform.codeshareplatform.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AuthenticationService implements AuthenticationProvider {

    private UserRepository userRepository;
    private HashService hashService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public AuthenticationService(UserRepository userRepository, HashService hashService) {
        this.userRepository = userRepository;
        this.hashService = hashService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();
        logger.info("Checking user of email: " + email);
        String password = authentication.getCredentials().toString();

        Optional<Users> user = Optional.ofNullable(userRepository.findUserByEmail(email));

        if (user.isPresent()) {
            logger.info("User present of email: " + email);
            String salt = user.get().getSalt();
            String hashedPassword = hashService.getHashedValue(password, salt);
            if (user.get().getPassword().equals(hashedPassword)) {
                logger.info("Correct details for email: " + email);
                return new UsernamePasswordAuthenticationToken(email, password, new ArrayList<>());
            }
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean equals = authentication.equals(UsernamePasswordAuthenticationToken.class);
        logger.info("Details: " + equals);
        return equals;
    }
}
