package com.tool_rental.herramientas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tool_rental.herramientas.Model.Herramienta;
@Repository
public interface HerramientaRepository extends JpaRepository<Herramienta, Integer> {

}
