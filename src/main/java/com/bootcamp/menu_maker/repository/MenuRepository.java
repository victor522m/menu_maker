package com.bootcamp.menu_maker.repository;


import com.bootcamp.menu_maker.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}



//Estos repositorios heredan automáticamente métodos como save(), findById(), findAll(), deleteById(), entre otros