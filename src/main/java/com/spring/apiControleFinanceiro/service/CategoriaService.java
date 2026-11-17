package com.spring.apiControleFinanceiro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.apiControleFinanceiro.DTO.CategoriaDTO;
import com.spring.apiControleFinanceiro.exception.ConflitoException;
import com.spring.apiControleFinanceiro.exception.RecursoNaoEncontradoException;
import com.spring.apiControleFinanceiro.model.Categoria;
import com.spring.apiControleFinanceiro.repository.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;

	public List<Categoria> listar() {
		return repository.findAll();
	}

	public Categoria buscarPorId(Long id) {
		return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Categoria inexistente!"));
	}

	public Categoria criarCategoria(CategoriaDTO dto) {
		if (repository.existsByNomeIgnoreCase(dto.nome())) {
			throw new ConflitoException("Categoria já existente!");
		}
		Categoria categoria = new Categoria(dto.nome());
		return repository.save(categoria);
	}

	public Categoria atualizarCategoria(Long id, CategoriaDTO dto) {
		Categoria categoria = buscarPorId(id);
		categoria.setNome(dto.nome());
		return repository.save(categoria);
	}

	public void apagarCategoria(Long id) {
		Categoria categoria = buscarPorId(id);
		repository.delete(categoria);
	}
}
