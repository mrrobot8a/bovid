package com.alcadia.bovid.Repository.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alcadia.bovid.Models.Entity.HisotiralAuditor;
import com.alcadia.bovid.Models.Entity.User;


import java.util.Set;

public interface IHistorialAuditoriaRepository extends JpaRepository<HisotiralAuditor, Long> {
    void deleteByUsersId(Long userId);

    HisotiralAuditor findByUsers(User users);

    Set<HisotiralAuditor> findByUsersId(Long userId);
}
