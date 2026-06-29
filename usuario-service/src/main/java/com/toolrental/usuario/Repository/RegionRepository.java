package com.toolrental.usuario.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toolrental.usuario.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer>{

}
