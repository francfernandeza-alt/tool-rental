package com.tool_rental.reservas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tool_rental.reservas.model.Resena;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Integer> {

    List<Resena> findByActivoTrue();

    Optional<Resena> findByIdResenaAndActivoTrue(Integer idResena);

    List<Resena> findByRutUsuarioAndActivoTrue(String rutUsuario);

    List<Resena> findByHerramientaIdAndActivoTrue(Integer herramientaId);

    List<Resena> findByReservaIdAndActivoTrue(Integer reservaId);
}
