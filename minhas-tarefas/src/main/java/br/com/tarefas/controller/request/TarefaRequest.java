package br.com.tarefas.controller.request;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//classe criada para salvar os dados
public class TarefaRequest {

	
	
	private Integer id;
	
	@NotBlank(message = "{tarefa.descricao.not-blank}")
	@Size(min=5, max = 150, message = "{tarefa.descricao.size}")
	private String descricao;
	
	@FutureOrPresent(message = "{tarefa.descricao.future-or-presente}")
	private LocalDate dataEntrega;
	
	@NotNull(message = "{tarefa.categoria.not-null}")
	@Min(value =1, message = "tarefa.categoria.min")
	private Integer categoriaId;
	
	@NotNull(message = "{tarefa.usuario.not-null}")
	@Min(value =1, message = "tarefa.usuario.min")
	private Integer usuarioId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(LocalDate dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public Integer getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Integer categoriaId) {
		this.categoriaId = categoriaId;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}
	
	
	
}
