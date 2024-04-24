
package com.alcadia.bovid.Repository.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alcadia.bovid.Models.Entity.MarcaGanadera;

@Repository
public interface IMarcaGanaderaRepository extends JpaRepository<MarcaGanadera, Long> {

    MarcaGanadera findByEtiqueta(String nameEtiqueta);

    
} 