package com.spring.apiControleFinanceiro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spring.apiControleFinanceiro.DTO.CategoriaDTO;
import com.spring.apiControleFinanceiro.exception.ConflitoException;
import com.spring.apiControleFinanceiro.exception.RecursoNaoEncontradoException;
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
		
		dto = new CategoriaDTO("Mercado");
	}
	
	@Test
	void deveListarTodasAsCategorias() {
		//ARRANGE
		Categoria categoria2 = new Categoria("Lazer");
		List<Categoria> categorias = List.of(categoria,categoria2);
		
		when(repository.findAll()).thenReturn(categorias);
		
		//ACT
		List<Categoria> resultado = service.listar();
		
		//ASSERT
		Assertions.assertNotNull(resultado);
		Assertions.assertEquals(2, resultado.size());
		verify(repository, times(1)).findAll();
	}

	@Test
	void deveBuscarPorID() {
		//ARRANGE
		when(repository.findById(categoria.getId())).thenReturn(Optional.of(categoria));
		
		//ACT
		Categoria resultado = service.buscarPorId(categoria.getId());
		
		//ASSERT
		Assertions.assertNotNull(resultado);
		Assertions.assertEquals(1L, categoria.getId());
		verify(repository, times(1)).findById(categoria.getId());
	}
	
	@Test
	void naoDeveBuscarPorID() {
		//ARRANGE
		when(repository.findById(categoria.getId())).thenReturn(Optional.empty());
		
		//ACT
		Exception naoBuscaException = assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(categoria.getId()));
		
		//ASSERT
		Assertions.assertEquals("Categoria inexistente!", naoBuscaException.getMessage());
		verify(repository, times(1)).findById(categoria.getId());
	}
	
	@Test
	void deveCriarCategoria() {
		//ARRANGE
		when(repository.save(any(Categoria.class))).thenAnswer(i -> i.getArgument(0));
		
		//ACT
		Categoria criada = service.criarCategoria(dto);
		
		//ASSERT
		Assertions.assertEquals("Mercado", criada.getNome());
		verify(repository, times(1)).save(any(Categoria.class));
	}
	
	@Test
	void naoDeveCriarCategoria() {
		//ARRANGE
		when(repository.existsByNomeIgnoreCase(dto.nome())).thenReturn(true);
		
		//ACT
		Exception naoCriaException = assertThrows(ConflitoException.class, () -> service.criarCategoria(dto));
		
		//ASSERT
		Assertions.assertEquals("Categoria já existente!", naoCriaException.getMessage());
		verify(repository, never()).save(any());
	}
	
	@Test
	void deveAtualizarCategoria() {
		//ARRANGE
		CategoriaDTO dto2 = new CategoriaDTO("Shopping");
		
		when(repository.findById(categoria.getId())).thenReturn(Optional.of(categoria));
		when(repository.save(any(Categoria.class))).thenAnswer(i -> i.getArgument(0));
		
		//ACT
		Categoria atualizada = service.atualizarCategoria(categoria.getId(), dto2);
		
		//ASSERT
		Assertions.assertNotNull(atualizada);
		Assertions.assertEquals("Shopping", atualizada.getNome());
		verify(repository, times(1)).save(any(Categoria.class));
	}
	
	@Test
	void naoDeveAtualizarCategoria() {
		//ARRANGE
		CategoriaDTO dto2 = new CategoriaDTO("Shopping");
		
		when(repository.findById(categoria.getId())).thenReturn(Optional.empty());
		
		//ACT
		Exception naoAtualizaException = assertThrows(RecursoNaoEncontradoException.class, () -> service.atualizarCategoria(categoria.getId(), dto2));
		
		//ASSERT
		Assertions.assertEquals("Categoria inexistente!", naoAtualizaException.getMessage());
		verify(repository, never()).save(any(Categoria.class));
	}
	
	@Test
	void deveRemoverCategoria() {
		//ARRANGE
		when(repository.findById(categoria.getId())).thenReturn(Optional.of(categoria));
		
		//ACT
		service.apagarCategoria(categoria.getId());
		
		//ASSERT
		verify(repository, times(1)).delete(categoria);
	}
	
	@Test
	void naoDeveRemoverCategoria() {
		//ARRANGE
		when(repository.findById(categoria.getId())).thenReturn(Optional.empty());
		
		//ACT
		Exception naoRemoveException = assertThrows(RecursoNaoEncontradoException.class, () -> service.apagarCategoria(categoria.getId()));
		
		//ASSERT
		Assertions.assertEquals("Categoria inexistente!", naoRemoveException.getMessage());
		verify(repository, never()).delete(categoria);
	}
}
