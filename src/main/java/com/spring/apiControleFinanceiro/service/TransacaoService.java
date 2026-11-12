package com.spring.apiControleFinanceiro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.apiControleFinanceiro.DTO.TransacaoDTO;
import com.spring.apiControleFinanceiro.model.Transacao;
import com.spring.apiControleFinanceiro.repository.TransacaoRepository;

@Service
public class TransacaoService {

	@Autowired
	private TransacaoRepository repository;

	public List<Transacao> listar() {
		// TODO Auto-generated method stub
		return null;
	}

	public Transacao buscarPorId(Long id) {
		// TODO Auto-generated method stub
		
	}

	public Transacao criarTransacao(TransacaoDTO dto) {
		// TODO Auto-generated method stub
		
	}

	public Transacao atualizarTransacao(Long id, TransacaoDTO dto) {
		// TODO Auto-generated method stub
		
	}

	public Transacao removerTransacao(Long id) {
		// TODO Auto-generated method stub
		
	}
}
