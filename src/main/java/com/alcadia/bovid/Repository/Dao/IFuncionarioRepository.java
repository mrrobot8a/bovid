package com.alcadia.bovid.Repository.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alcadia.bovid.Models.Entity.Funcionario;

public interface IFuncionarioRepository  extends JpaRepository<Funcionario,Long>{

    
    
}
