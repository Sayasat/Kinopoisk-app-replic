package com.sayasat.exp2.kinopoisk_check.repositories;

import com.sayasat.exp2.kinopoisk_check.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
