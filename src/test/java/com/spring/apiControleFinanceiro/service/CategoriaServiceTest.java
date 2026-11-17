package com.spring.apiControleFinanceiro.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spring.apiControleFinanceiro.DTO.CategoriaDTO;
import com.spring.apiControleFinanceiro.model.Categoria;
import com.spring.apiControleFinanceiro.repository.CategoriaRepository;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

	@Mock
	private CategoriaRepository repository;
	
	@InjectMocks
	private CategoriaService service;
	
	private Categoria categoria;
	private CategoriaDTO dto;
	
	@BeforeEach
	void setUp() {
		categoria = new Categoria("Débito");
		categoria.setId(1L);
		
		dto = new CategoriaDTO("Débito");
	}
	
	@Test
	void deveListarTodasAsCategorias() {
		//ARRANGE
		
		
		//ACT
		//ASSERT
	}

}
