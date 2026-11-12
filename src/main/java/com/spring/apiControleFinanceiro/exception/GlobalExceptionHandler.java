package com.spring.apiControleFinanceiro.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RecursoNaoEncontradoException.class)
	public ResponseEntity<ErroResposta> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
		ErroResposta erro = new ErroResposta(404,ex.getMessage(),LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
}
