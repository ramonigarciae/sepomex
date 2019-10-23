package com.rg.sepomex.importsepomex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rg.sepomex.importsepomex.entities.Ciudad;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

}
