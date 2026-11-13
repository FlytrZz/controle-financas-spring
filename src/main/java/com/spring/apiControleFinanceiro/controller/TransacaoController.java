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

import com.spring.apiControleFinanceiro.DTO.TransacaoDTO;
import com.spring.apiControleFinanceiro.model.Transacao;
import com.spring.apiControleFinanceiro.service.TransacaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/transacoes")
public class TransacaoController {

	@Autowired
	private TransacaoService service;
	
	@GetMapping
	public List<Transacao> listar() {
		return service.listar();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Transacao> buscarPorId(@PathVariable("id") Long id) {
		return ResponseEntity.ok(service.buscarPorId(id));
	}
	
	@PostMapping
	public ResponseEntity<Transacao> cadastrar(@RequestBody @Valid TransacaoDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.criarTransacao(dto));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Transacao> atualizar(@PathVariable("id") Long id, @RequestBody @Valid TransacaoDTO dto) {
		return ResponseEntity.ok(service.atualizarTransacao(id,dto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable("id") Long id) {
		service.removerTransacao(id);
		return ResponseEntity.noContent().build();
	}
}
