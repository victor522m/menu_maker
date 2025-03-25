package com.bootcamp.menu_maker.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bootcamp.menu_maker.entity.PlatoBase;

@Repository
public interface PlatoRepository extends JpaRepository<PlatoBase, Long> {
}

//Estos repositorios heredan automáticamente métodos como save(), findById(), findAll(), deleteById(), entre otros
