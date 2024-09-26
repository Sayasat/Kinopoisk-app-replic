package com.sayasat.exp2.kinopoisk_check.services;

import com.sayasat.exp2.kinopoisk_check.models.User;
import com.sayasat.exp2.kinopoisk_check.repositories.UserRepository;
import com.sayasat.exp2.kinopoisk_check.security.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsImplService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsImplService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserDetailsImpl(user.get());
    }
}
