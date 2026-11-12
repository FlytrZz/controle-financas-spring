package com.spring.apiControleFinanceiro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.apiControleFinanceiro.DTO.CategoriaDTO;
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
		Categoria categoria = repository.findById(id);
		if(categoria.)
	}

	public void criarCategoria(CategoriaDTO dto) {
		// TODO Auto-generated method stub
		
	}

	public void atualizarCategoria(Long id, CategoriaDTO dto) {
		// TODO Auto-generated method stub
		
	}

	public void apagarCategoria() {
		// TODO Auto-generated method stub
		
	}
}
