package com.spring.apiControleFinanceiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.apiControleFinanceiro.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
