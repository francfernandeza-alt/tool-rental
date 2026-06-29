package com.toolrental.usuario.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toolrental.usuario.model.Comuna;
@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Integer> {

}
