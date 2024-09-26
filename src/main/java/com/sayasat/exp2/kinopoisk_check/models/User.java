package com.sayasat.exp2.kinopoisk_check.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    @NotEmpty(message = "Username should consist smth")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "Password shouldn't be empty")
    private String password;

    @Column(name = "email")
    @NotEmpty(message = "Email should exist")
    @Email
    private String email;

    @Column(name = "role")
    private String role;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "user")
    private List<Comment> reviews;
}
