package com.spring.apiControleFinanceiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.apiControleFinanceiro.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long>{

}
