package com.sayasat.exp2.kinopoisk_check.controllers;

import com.sayasat.exp2.kinopoisk_check.dto.users.AuthenticationDTO;
import com.sayasat.exp2.kinopoisk_check.dto.users.UserDTO;
import com.sayasat.exp2.kinopoisk_check.models.User;
import com.sayasat.exp2.kinopoisk_check.security.JWTUtil;
import com.sayasat.exp2.kinopoisk_check.services.RegistrationService;
import com.sayasat.exp2.kinopoisk_check.services.UserService;
import com.sayasat.exp2.kinopoisk_check.util.validator.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(ModelMapper modelMapper, UserValidator userValidator, RegistrationService registrationService, JWTUtil jwtUtil, AuthenticationManager authenticationManager, UserService userService) {
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid UserDTO userDTO,
                                                   BindingResult bindingResult) {
        User user = convertToUser(userDTO);

        userValidator.validate(user, bindingResult);

        if(bindingResult.hasErrors()) {
            return Map.of("message", "Ошибка!");
        }

        registrationService.register(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return Map.of("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }

        Optional<User> user = userService.findByUsername(authenticationDTO.getUsername());
        String token = jwtUtil.generateToken(authenticationDTO.getUsername(), user.get().getRole());
        return Map.of("jwt-token", token);
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
