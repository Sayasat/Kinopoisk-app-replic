package com.sayasat.exp2.kinopoisk_check.controllers;

import com.sayasat.exp2.kinopoisk_check.dto.users.AuthenticationDTO;
import com.sayasat.exp2.kinopoisk_check.dto.users.UserInfoDTO;
import com.sayasat.exp2.kinopoisk_check.dto.movies.MovieResponse;
import com.sayasat.exp2.kinopoisk_check.models.User;
import com.sayasat.exp2.kinopoisk_check.services.UserContextService;
import com.sayasat.exp2.kinopoisk_check.services.UserService;
import com.sayasat.exp2.kinopoisk_check.util.MovieErrorResponse;
import com.sayasat.exp2.kinopoisk_check.util.MovieException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.sayasat.exp2.kinopoisk_check.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserContextService userContextService;

    public UserController(UserService userService, ModelMapper modelMapper, UserContextService userContextService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userContextService = userContextService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public MovieResponse getUsers() {
        return new MovieResponse(userService.getAllUsers());
    }

    @GetMapping("/me")
    public UserInfoDTO getMyUser() {
        String username = userContextService.getCurrentUsername();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return convertToUserInfoDTO(user);
    }

    @PatchMapping("/me")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid AuthenticationDTO userDTO,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
            return ResponseEntity.badRequest().build();
        }
        String username = userContextService.getCurrentUsername();
        userService.updateUser(userDTO, username);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.UPGRADE_REQUIRED);
    }

    @ExceptionHandler
    private ResponseEntity<MovieErrorResponse> handleException(MovieException e) {
        MovieErrorResponse response = new MovieErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public UserInfoDTO convertToUserInfoDTO(User user) {
        return modelMapper.map(user, UserInfoDTO.class);
    }
}
