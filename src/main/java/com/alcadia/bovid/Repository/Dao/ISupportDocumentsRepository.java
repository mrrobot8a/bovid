package com.alcadia.bovid.Repository.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alcadia.bovid.Models.Entity.SupportDocument;

public interface ISupportDocumentsRepository  extends JpaRepository<SupportDocument,Long>{

    boolean existsByUrlFile(String urlFile);
}
