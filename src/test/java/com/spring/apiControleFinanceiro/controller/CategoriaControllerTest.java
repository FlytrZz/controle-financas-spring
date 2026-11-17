package com.spring.apiControleFinanceiro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.spring.apiControleFinanceiro.DTO.CategoriaDTO;
import com.spring.apiControleFinanceiro.exception.RecursoNaoEncontradoException;
import com.spring.apiControleFinanceiro.model.Categoria;
import com.spring.apiControleFinanceiro.service.CategoriaService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockitoBean
	private CategoriaService service;
	
	private Categoria categoria;
	private CategoriaDTO dto;
	
	@BeforeEach
	void setUp() {
		categoria = new Categoria("Mercado");
		categoria.setId(1L);
		
		dto = new CategoriaDTO("Lazer");
	}
	
	@Test
	void testar200ParaListarCategorias() throws Exception {
		//ARRANGE
		Categoria categoria2 = new Categoria("Shopping");
		when(service.listar()).thenReturn(List.of(categoria, categoria2));
		
		//ACT + ASSERT
		mvc.perform(get("/v1/categorias"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(2))
		.andExpect(jsonPath("$[0].nome").value("Mercado"))
		.andExpect(jsonPath("$[1].nome").value("Shopping"));
	}
	
	@Test
	void testar200ParaListarSemElementos() throws Exception {
		//ARRANGE
		when(service.listar()).thenReturn(List.of());
		
		//ACT + ASSERT
		mvc.perform(get("/v1/categorias"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(0));
	}
	
	@Test
	void testar200ParaBuscarPorID() throws Exception {
		//ARRANGE
		when(service.buscarPorId(categoria.getId())).thenReturn(categoria);
		
		//ACT + ASSERT
		mvc.perform(get("/v1/categorias/{id}", categoria.getId()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.nome").value("Mercado"));
	}
	
	@Test
	void testar404ParaBuscarPorID() throws Exception {
		//ARRANGE
		when(service.buscarPorId(categoria.getId())).thenThrow(new RecursoNaoEncontradoException("Categoria inexistente!"));
		
		//ACT + ASSERT
		mvc.perform(get("/v1/categorias/{id}", categoria.getId()))
		.andExpect(status().isNotFound());
	}
	
	@Test
	void testar201ParaCriarCategoria() throws Exception {
		//ARRANGE
		when(service.criarCategoria(any(CategoriaDTO.class))).thenReturn(categoria);
		
		//ACT + ASSERT
		mvc.perform(post("/v1/categorias")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(dto)))
		.andExpect(status().isCreated());
	}
	
	@Test
	void testar400ParaCriarCategoria() throws Exception {
		//ARRANGE
		CategoriaDTO dtoInvalido = new CategoriaDTO("");
		when(service.criarCategoria(dtoInvalido)).thenReturn(categoria);
		
		//ACT + ASSERT
		mvc.perform(post("/v1/categorias")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(dtoInvalido)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void testar200ParaAtualizarCategoria() throws Exception {
		//ARRANGE
		CategoriaDTO dtoAtualizada = new CategoriaDTO("Estádio");
		when(service.atualizarCategoria(categoria.getId(), dtoAtualizada)).thenReturn(categoria);
		
		//ACT + ASSERT
		mvc.perform(put("/v1/categorias/{id}", categoria.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(dtoAtualizada)))
		.andExpect(status().isOk());
	}
	
	@Test
	void testar404ParaAtualizarCategoria() throws Exception {
		//ARRANGE
		CategoriaDTO dtoAtualizada = new CategoriaDTO("Estádio");
		when(service.atualizarCategoria(categoria.getId(), dtoAtualizada)).thenThrow(new RecursoNaoEncontradoException("Categoria inexistente!"));
		
		//ACT + ASSERT
		mvc.perform(put("/v1/categorias/{id}", categoria.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(dtoAtualizada)))
		.andExpect(status().isNotFound());
	}
	
	@Test
	void testar204ParaRemoverCategoria() throws Exception {
		//ARRANGE
		doNothing().when(service).apagarCategoria(categoria.getId());
		
		//ACT + ASSERT
		mvc.perform(delete("/v1/categorias/{id}", categoria.getId()))
		.andExpect(status().isNoContent());
	}
	
	@Test
	void testar404ParaRemoverCategoria() throws Exception {
		//ARRAMNGE
		doThrow(new RecursoNaoEncontradoException("Categoria inexistente!")).when(service).apagarCategoria(categoria.getId());
		
		//ACT + ASSERT
		mvc.perform(delete("/v1/categorias/{id}", categoria.getId()))
		.andExpect(status().isNotFound());
	}
	
}
