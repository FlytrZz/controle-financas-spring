package com.spring.apiControleFinanceiro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.apiControleFinanceiro.DTO.CategoriaDTO;
import com.spring.apiControleFinanceiro.model.Categoria;
import com.spring.apiControleFinanceiro.service.CategoriaService;

@RestController
@RequestMapping("/v1/categorias")

public class CategoriaController {

	@Autowired
	private CategoriaService service;
	
	@GetMapping
	public List<Categoria> listar(){
		return service.listar();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> buscarPorId(@PathVariable("id") Long id){
		service.buscarPorId(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PostMapping
	public ResponseEntity<Object> cadastrar(CategoriaDTO dto) {
		service.criarCategoria(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> atualizar(@PathVariable("id") Long id, CategoriaDTO dto) {
		service.atualizarCategoria(id,dto);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deletar(@PathVariable("id") Long id) {
		service.apagarCategoria();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
