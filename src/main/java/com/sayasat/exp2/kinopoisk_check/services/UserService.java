package com.sayasat.exp2.kinopoisk_check.services;

import com.sayasat.exp2.kinopoisk_check.dto.users.AuthenticationDTO;
import com.sayasat.exp2.kinopoisk_check.dto.users.RoleChangeDTO;
import com.sayasat.exp2.kinopoisk_check.dto.users.UserDTO;
import com.sayasat.exp2.kinopoisk_check.dto.users.UserInfoDTO;
import com.sayasat.exp2.kinopoisk_check.models.User;
import com.sayasat.exp2.kinopoisk_check.repositories.UserRepository;
import com.sayasat.exp2.kinopoisk_check.util.MovieException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserInfoDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToUserInfoDTO)
                .collect(Collectors.toList());
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserInfoDTO getUser(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return convertToUserInfoDTO(user.get());
        } else {
            throw new MovieException("User not found");
        }
    }

    @Transactional
    public void updateUser(AuthenticationDTO authenticationDTO, String currentUsername) {
        User userToUpdate = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new MovieException("User not found"));

        userToUpdate.setUsername(authenticationDTO.getUsername());
        if (authenticationDTO.getPassword() != null && !authenticationDTO.getPassword().isEmpty()) {
            userToUpdate.setPassword(passwordEncoder.encode(authenticationDTO.getPassword()));
        }
        userRepository.save(userToUpdate);
    }

    @Transactional
    public void deleteUser(int id) {
        User userToDelete = userRepository.findById(id).orElse(null);
        if (userToDelete != null) {
            userRepository.delete(userToDelete);
        } else {
            throw new MovieException("User not found");
        }
    }

    public void changeRole(RoleChangeDTO roleChangeDTO) {
        User user = userRepository.findByUsername(roleChangeDTO.getUsername()).orElse(null);
        if (user != null) {
            user.setRole(roleChangeDTO.getNewRole());
            userRepository.save(user);
        } else {
            throw new MovieException("User not found");
        }
    }

    public UserDTO convertUserToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User convertAuthenticationDTOToUser(AuthenticationDTO authenticationDTO) {
        return modelMapper.map(authenticationDTO, User.class);
    }

    public UserInfoDTO convertToUserInfoDTO(User user) {
        return modelMapper.map(user, UserInfoDTO.class);
    }
}
