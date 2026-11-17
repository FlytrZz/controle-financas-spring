package com.spring.apiControleFinanceiro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.spring.apiControleFinanceiro.DTO.CategoriaDTO;
import com.spring.apiControleFinanceiro.DTO.TransacaoDTO;
import com.spring.apiControleFinanceiro.exception.RecursoNaoEncontradoException;
import com.spring.apiControleFinanceiro.model.Categoria;
import com.spring.apiControleFinanceiro.model.Tipo;
import com.spring.apiControleFinanceiro.model.Transacao;
import com.spring.apiControleFinanceiro.repository.CategoriaRepository;
import com.spring.apiControleFinanceiro.repository.TransacaoRepository;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {
	
	@Mock
	private TransacaoRepository repository;
	
	@Mock
	private CategoriaRepository categoriaRepository;
	
	@InjectMocks
	private TransacaoService service;
	
	private Categoria categoria;
	private Transacao transacao;
	private TransacaoDTO dto;
	
	@BeforeEach
	void setUp() {
		Long id = 1L;
		
		categoria = new Categoria("Débito");
		categoria.setId(id);
		
		transacao = new Transacao("Transação via PIX",new BigDecimal(30.00),LocalDate.of(2026,11,15),Tipo.DESPESA);
		transacao.setCategoria(categoria);
		ReflectionTestUtils.setField(transacao, "id", 2L);
		
		dto = new TransacaoDTO("Transação via PIX",new BigDecimal(20.00),LocalDate.of(2026,11,10),Tipo.DESPESA, 1L);
	}

	@Test
	void deveListarTodasAsTransacoes() {
		//ARRANGE
		Transacao transacao2 = new Transacao("Transação via Crédito",new BigDecimal(30.00),LocalDate.of(2026,11,15),Tipo.RECEITA); 
		List<Transacao> transacoes = List.of(transacao,transacao2);
		
		when(repository.findAll()).thenReturn(transacoes);
		
		//ACT
		List<Transacao> resultado = service.listar();
		
		//ASSERT
		Assertions.assertNotNull(resultado);
		Assertions.assertEquals(2, resultado.size());
		verify(repository, times(1)).findAll();
	}

	@Test
	void deveBuscarPorId() {
		//ARRANGE
		when(repository.findById(transacao.getId())).thenReturn(Optional.of(transacao));
		
		//ACT
		Transacao resultado = service.buscarPorId(transacao.getId());
		
		//ASSERT
		Assertions.assertNotNull(resultado);
		Assertions.assertEquals(2L, resultado.getId());
		verify(repository, times(1)).findById(transacao.getId());
	}
	
	@Test
	void naoDeveBuscarPorId() {
		//ARRANGE
		when(repository.findById(transacao.getId())).thenReturn(Optional.empty());
		
		//ACT
		Exception naoBuscaException = assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(transacao.getId()));
		
		//ASSERT
		Assertions.assertEquals("Transação inexistente!", naoBuscaException.getMessage());
		verify(repository, times(1)).findById(transacao.getId());
	}
	
	@Test
	void deveCriarTransacao() {
		//ARRANGE
		when(categoriaRepository.findById(categoria.getId())).thenReturn(Optional.of(categoria));
		when(repository.save(any(Transacao.class))).thenAnswer(i -> i.getArgument(0));
		
		//ACT
		Transacao criada = service.criarTransacao(dto);
		
		//ASSERT
		Assertions.assertEquals("Transação via PIX", criada.getDescricao());
		Assertions.assertEquals(Tipo.DESPESA, criada.getTipo());
		Assertions.assertEquals("Débito", criada.getCategoria().getNome());
		Assertions.assertEquals(new BigDecimal(20), criada.getValor());
		Assertions.assertEquals(LocalDate.of(2026, 11, 10), criada.getData());
		verify(categoriaRepository, times(1)).findById(categoria.getId());
		verify(repository, times(1)).save(any(Transacao.class));
	}
	
	@Test
	void naoDeveCriarTransacao() {
		//ARRANGE
		when(categoriaRepository.findById(categoria.getId())).thenReturn(Optional.empty());
		
		//ACT
		Exception naoCriaException = assertThrows(RecursoNaoEncontradoException.class, () -> service.criarTransacao(dto));
		
		//ASSERT
		Assertions.assertEquals("Categoria inexistente!", naoCriaException.getMessage());
		verify(categoriaRepository, times(1)).findById(categoria.getId());
	}
	
	@Test
	void deveAtualizarTransacao() {
		//ARRANGE
		TransacaoDTO novoDTO = new TransacaoDTO("Crédito", new BigDecimal(50.00), LocalDate.of(2026, 11, 12), Tipo.DESPESA, 1L);
		
		when(categoriaRepository.findById(categoria.getId())).thenReturn(Optional.of(categoria));
		when(repository.findById(transacao.getId())).thenReturn(Optional.of(transacao));
		when(repository.save(any(Transacao.class))).thenAnswer(i -> i.getArgument(0));
		
		//ACT
		Transacao resultado = service.atualizarTransacao(transacao.getId(), novoDTO);
		
		//ASSERT
		Assertions.assertEquals("Crédito", resultado.getDescricao());
		Assertions.assertEquals(new BigDecimal(50.00), resultado.getValor());
		Assertions.assertEquals(categoria, resultado.getCategoria());
		verify(categoriaRepository, times(1)).findById(categoria.getId());
		verify(repository, times(1)).findById(transacao.getId());
		verify(repository, times(1)).save(any(Transacao.class));
	}
	
	@Test
	void naoDeveAtualizarTransacao() {
		//ARRANGE
		TransacaoDTO novoDTO = new TransacaoDTO("Crédito", new BigDecimal(50.00), LocalDate.of(2026, 11, 12), Tipo.DESPESA, 1L);
		
		when(repository.findById(transacao.getId())).thenReturn(Optional.empty());
		
		//ACT
		Exception naoDeveAtualizaException = assertThrows(RecursoNaoEncontradoException.class, () -> service.atualizarTransacao(transacao.getId(), novoDTO));
		
		//ASSERT
		Assertions.assertEquals("Transação inexistente!", naoDeveAtualizaException.getMessage());
		verifyNoInteractions(categoriaRepository);
		verify(repository, never()).save(any());
	}
	
	@Test
	void deveRemoverTransacao() {
		//ARRANGE
		when(repository.findById(transacao.getId())).thenReturn(Optional.of(transacao));
		
		//ACT
		service.removerTransacao(transacao.getId());
		
		//ASSERT
		verify(repository, times(1)).delete(transacao);
	}
	
	@Test
	void naoDeveRemoverTransacao() {
		//ARRANGE
		when(repository.findById(transacao.getId())).thenReturn(Optional.empty());
		
		//ACT
		Exception naoRemoveException = assertThrows(RecursoNaoEncontradoException.class, () -> service.removerTransacao(transacao.getId()));
		
		//ASSERT
		Assertions.assertEquals("Transação inexistente!", naoRemoveException.getMessage());
		verify(repository, never()).delete(transacao);
	}
}
