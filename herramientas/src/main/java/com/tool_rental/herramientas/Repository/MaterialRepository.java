package com.tool_rental.herramientas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tool_rental.herramientas.Model.Material;

public interface MaterialRepository extends JpaRepository<Material, Integer> {

}
