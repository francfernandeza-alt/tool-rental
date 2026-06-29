package com.toolrental.usuario.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toolrental.usuario.model.Direccion;
@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer>{

}
