package com.toolrental.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toolrental.usuario.model.Usuario111;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario111, String> {

}


