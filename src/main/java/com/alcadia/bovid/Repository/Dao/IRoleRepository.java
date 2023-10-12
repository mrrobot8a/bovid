package com.alcadia.bovid.Repository.Dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.alcadia.bovid.Models.Entity.Role;

@Repository
public interface IRoleRepository  extends JpaRepository<Role, Integer>{
    Optional<Role> findByAuthority(String authority);

    Void deleteByAuthority(String authority);
}
