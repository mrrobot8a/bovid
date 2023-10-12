package com.alcadia.bovid.Repository.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alcadia.bovid.Models.Entity.Ganadero;

@Repository
public interface IGanaderoRepository extends JpaRepository<Ganadero,Long>  {
    
    
}
