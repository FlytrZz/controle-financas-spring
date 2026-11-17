package com.spring.apiControleFinanceiro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.spring.apiControleFinanceiro.DTO.TransacaoDTO;
import com.spring.apiControleFinanceiro.exception.RecursoNaoEncontradoException;
import com.spring.apiControleFinanceiro.model.Categoria;
import com.spring.apiControleFinanceiro.model.Tipo;
import com.spring.apiControleFinanceiro.model.Transacao;
import com.spring.apiControleFinanceiro.service.TransacaoService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(TransacaoController.class)
class TransacaoControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockitoBean
	private TransacaoService service;
	
	@Autowired
	private ObjectMapper mapper;
	
	private Transacao transacao;
	private Categoria categoria;
	private TransacaoDTO dto;
	
	@BeforeEach
	void setUp() {
		categoria = new Categoria("Mercado");
		categoria.setId(1L);
		
		transacao = new Transacao("Transação via PIX", new BigDecimal(50.00), LocalDate.of(2026, 11, 10), Tipo.RECEITA);
		transacao.setCategoria(categoria);
		ReflectionTestUtils.setField(transacao, "id", 2L);
		
		dto = new TransacaoDTO("Transação via Débito", new BigDecimal(30.00), LocalDate.of(2026, 06, 15), Tipo.DESPESA, 1L);
	}
	
	@Test
	void testar200paraListar() throws Exception {
		//ARRANGE
		Transacao transacao2 = new Transacao("Transação via Crédito", new BigDecimal(25.00), LocalDate.of(2026, 05, 02), Tipo.DESPESA);
		transacao2.setCategoria(categoria);
		ReflectionTestUtils.setField(transacao2, "id", 3L);
		
		when(service.listar()).thenReturn(List.of(transacao,transacao2));
		
		//ACT + ASSERT
		mvc.perform(get("/v1/transacoes"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(2))
		.andExpect(jsonPath("$[0].descricao").value("Transação via PIX"))
		.andExpect(jsonPath("$[0].valor").value(50.00))
		.andExpect(jsonPath("$[0].tipo").value(Tipo.RECEITA.name()))
		.andExpect(jsonPath("$[0].categoria.nome").value("Mercado"))
		.andExpect(jsonPath("$[1].descricao").value("Transação via Crédito"))
		.andExpect(jsonPath("$[1].valor").value(25.00));
	}
	
	@Test
	void testar200ParaListarSemElementos() throws Exception {
		//ARRANGE
		when(service.listar()).thenReturn(List.of());
				
		//ACT + ASSERT
		mvc.perform(get("/v1/transacoes"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(0));
	}
	
	@Test
	void testar200ParaBuscarPorID() throws Exception {
		//ARRANGE
		when(service.buscarPorId(transacao.getId())).thenReturn(transacao);
		
		//ACT + ASSERT
		mvc.perform(get("/v1/transacoes/{id}", transacao.getId()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.descricao").value("Transação via PIX"))
		.andExpect(jsonPath("$.valor").value(50.00))
		.andExpect(jsonPath("$.tipo").value(Tipo.RECEITA.name()))
		.andExpect(jsonPath("$.categoria.nome").value("Mercado"));
	}
	
	@Test
	void testar404ParaBuscarID() throws Exception{
		//ARRANGE
		when(service.buscarPorId(transacao.getId())).thenThrow(new RecursoNaoEncontradoException("Transação inexistente!"));
		
		//ACT + ASSERT
		mvc.perform(get("/v1/transacoes/{id}", transacao.getId()))
		.andExpect(status().isNotFound());
	}
	
	@Test
	void testar201ParaCriarTransacao() throws Exception {
		//ARRANGE
		when(service.criarTransacao(any(TransacaoDTO.class))).thenReturn(transacao);
		
		//ACT + ASSERT
		mvc.perform(post("/v1/transacoes")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(dto)))
		.andExpect(status().isCreated());
	}
	
	@Test
	void testar400ParaCriarTransacao() throws Exception {
		//ARRANGE
		TransacaoDTO dtoInvalido = new TransacaoDTO("Transação via PIX", new BigDecimal("-50.00"), LocalDate.of(2026, 11, 10), Tipo.RECEITA, 1L);
		
		//ACT + ASSERT
		mvc.perform(post("/v1/transacoes")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(dtoInvalido)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void testar200ParaAtualizarTransacao() throws Exception {
		//ARRANGE
		TransacaoDTO dtoAtualizada = new TransacaoDTO("Transação via Crédito", new BigDecimal(25.00), LocalDate.of(2026, 05, 02), Tipo.DESPESA, 1L);
		
		when(service.atualizarTransacao(transacao.getId(), dtoAtualizada)).thenReturn(transacao);
		
		//ACT + ASSERT
		mvc.perform(put("/v1/transacoes/{id}", transacao.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(dtoAtualizada)))
		.andExpect(status().isOk());
	}
	
	@Test
	void testar404ParaAtualizarTransacao() throws Exception {
		//ARRANGE
		TransacaoDTO dtoAtualizada = new TransacaoDTO("Transação via Crédito", new BigDecimal(25.00), LocalDate.of(2026, 05, 02), Tipo.DESPESA, 1L);
		
		when(service.atualizarTransacao(transacao.getId(), dtoAtualizada)).thenThrow(new RecursoNaoEncontradoException("Transação inexistente!"));
		
		//ACT + ASSERT
		mvc.perform(put("/v1/transacoes/{id}", transacao.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(dtoAtualizada)))
		.andExpect(status().isNotFound());
	}
	
	@Test
	void testar204ParaRemoverTransacao() throws Exception {
		//ARRANGE
		doNothing().when(service).apagarTransacao(transacao.getId());
		
		//ACT + ASSERT
		mvc.perform(delete("/v1/transacoes/{id}", transacao.getId()))
		.andExpect(status().isNoContent());
	}
	
	@Test
	void testar404ParaRemoverTransacao() throws Exception {
		//ARRANGE
		doThrow(new RecursoNaoEncontradoException("Transação inexistente!")).when(service).apagarTransacao(transacao.getId());
		
		//ACT + ASSERT
		mvc.perform(delete("/v1/transacoes/{id}", transacao.getId()))
		.andExpect(status().isNotFound());
	}
}
