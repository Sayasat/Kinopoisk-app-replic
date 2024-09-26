package com.sayasat.exp2.kinopoisk_check.repositories;

import com.sayasat.exp2.kinopoisk_check.models.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer> {

    Optional<Director> findByFullName(String fullName);

}
