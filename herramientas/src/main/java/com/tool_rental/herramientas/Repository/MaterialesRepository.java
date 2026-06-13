package com.tool_rental.herramientas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tool_rental.herramientas.Model.Materiales;

@Repository
public interface MaterialesRepository extends JpaRepository<Materiales, Integer> {

}
