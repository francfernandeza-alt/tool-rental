package com.tool_rental.reservas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tool_rental.reservas.model.TipoReserva;

@Repository
public interface TipoReservaRepository extends JpaRepository<TipoReserva, Integer> {

    List<TipoReserva> findByActivoTrue();

    Optional<TipoReserva> findByIdTipoReservaAndActivoTrue(Integer idTipoReserva);
}
