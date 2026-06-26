package com.tool_rental.reservas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tool_rental.reservas.model.Resena;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Integer> {

    List<Resena> findByRutUsuario(String rutUsuario);

    List<Resena> findByHerramientaId(Integer herramientaId);

    List<Resena> findByReservaId(Integer reservaId);
}


