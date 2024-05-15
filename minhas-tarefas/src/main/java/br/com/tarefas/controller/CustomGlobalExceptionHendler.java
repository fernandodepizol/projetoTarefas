package br.com.tarefas.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.tarefas.controller.response.ErroResponse;
import br.com.tarefas.exception.TarefaStatusException;
import jakarta.persistence.EntityNotFoundException;


@RestControllerAdvice
public class CustomGlobalExceptionHendler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErroResponse entityNotFoundHandler(EntityNotFoundException ex) {
		return new ErroResponse("Recurso não encontrado");
	}
	
	@ExceptionHandler(TarefaStatusException.class)
	public ResponseEntity<?> alteraStatusTarefaHandler(TarefaStatusException ex) {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
				.header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
				.body(Problem.create().withTitle("Método não permitido")
						.withDetail("Você não pode realizar esta operação: "+ ex.getMessage()));
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		List<ErroResponse> errors = ex.getBindingResult()
		.getFieldErrors()
		.stream()
		.map(x -> new ErroResponse(x.getField(), x.getDefaultMessage()))
		.collect(Collectors.toList());
		
		return ResponseEntity.badRequest().body(errors);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	ErroResponse entityBadCredentialsException(BadCredentialsException ex) {
		return new ErroResponse("Nome de usuário e/ou senha inválidos");
	}
}
