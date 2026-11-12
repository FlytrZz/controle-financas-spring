package com.spring.apiControleFinanceiro.DTO;

import java.time.LocalDate;

import com.spring.apiControleFinanceiro.model.Categoria;
import com.spring.apiControleFinanceiro.model.Tipo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransacaoDTO(String descricao, @NotNull @Positive double valor, @NotNull LocalDate data, @NotNull Tipo tipo, Categoria categoria) {

}
