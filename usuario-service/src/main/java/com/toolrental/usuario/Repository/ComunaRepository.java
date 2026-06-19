package com.toolrental.usuario.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toolrental.usuario.Model.Comuna;
@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Integer> {

}
