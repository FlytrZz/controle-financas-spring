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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.apiControleFinanceiro.DTO.CategoriaDTO;
import com.spring.apiControleFinanceiro.model.Categoria;
import com.spring.apiControleFinanceiro.service.CategoriaService;

import jakarta.validation.Valid;

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
	public ResponseEntity<Categoria> buscarPorId(@PathVariable("id") Long id){
		return ResponseEntity.ok(service.buscarPorId(id));
	}
	
	@PostMapping
	public ResponseEntity<Categoria> cadastrar(@RequestBody @Valid CategoriaDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.criarCategoria(dto));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Categoria> atualizar(@PathVariable("id") Long id, @RequestBody @Valid CategoriaDTO dto) {
		return ResponseEntity.ok(service.atualizarCategoria(id, dto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable("id") Long id) {
		service.apagarCategoria(id);
		return ResponseEntity.noContent().build();
	}
}
