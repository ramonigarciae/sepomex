package com.rg.sepomex.importsepomex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rg.sepomex.importsepomex.entities.TipoAsentamiento;

@Repository
public interface TipoAsentamientoRepository extends JpaRepository<TipoAsentamiento, Long>{

}
