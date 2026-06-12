package com.tool_rental.herramientas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tool_rental.herramientas.Model.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {

}
