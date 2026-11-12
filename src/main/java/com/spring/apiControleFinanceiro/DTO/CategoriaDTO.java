package com.spring.apiControleFinanceiro.DTO;

import jakarta.validation.constraints.NotBlank;

public record CategoriaDTO(@NotBlank String nome) {

}
