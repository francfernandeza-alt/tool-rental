package com.tool_rental.herramientas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tool_rental.herramientas.Model.Mantenciones;

@Repository
public interface MantencionesRepository extends JpaRepository<Mantenciones, Integer>{

}
