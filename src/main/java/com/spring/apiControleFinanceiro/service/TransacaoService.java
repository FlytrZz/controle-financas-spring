package com.spring.apiControleFinanceiro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.apiControleFinanceiro.DTO.TransacaoDTO;
import com.spring.apiControleFinanceiro.exception.RecursoNaoEncontradoException;
import com.spring.apiControleFinanceiro.model.Categoria;
import com.spring.apiControleFinanceiro.model.Transacao;
import com.spring.apiControleFinanceiro.repository.CategoriaRepository;
import com.spring.apiControleFinanceiro.repository.TransacaoRepository;

@Service
public class TransacaoService {

	@Autowired
	private TransacaoRepository repository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	public List<Transacao> listar() {
		return repository.findAll();
	}

	public Transacao buscarPorId(Long id) {
		return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Transação inexistente!"));
	}

	public Transacao criarTransacao(TransacaoDTO dto) {
		Transacao transacao = new Transacao(dto.descricao(),dto.valor(),dto.data(),dto.tipo());
		transacao.setCategoria(buscarCategoria(dto.idCategoria()));
		return repository.save(transacao);
	}

	public Transacao atualizarTransacao(Long id, TransacaoDTO dto) {
		Transacao transacao = buscarPorId(id);
		transacao.setDescricao(dto.descricao());
		transacao.setValor(dto.valor());
		transacao.setData(dto.data());
		transacao.setTipo(dto.tipo());
		transacao.setCategoria(buscarCategoria(dto.idCategoria()));
		return repository.save(transacao);
		
	}

	public void removerTransacao(Long id) {
		Transacao transacao = buscarPorId(id);
		repository.delete(transacao);
	}
	
	public Categoria buscarCategoria (Long idCategoria) {
		return categoriaRepository.findById(idCategoria).orElseThrow(() -> new RecursoNaoEncontradoException("Categoria inexistente!"));
	}
}
