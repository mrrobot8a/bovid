package com.alcadia.bovid.Repository.Dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.alcadia.bovid.Models.Entity.User;

import java.util.Optional;

/**
 * @author Jhon peralta
 */

public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
    
}