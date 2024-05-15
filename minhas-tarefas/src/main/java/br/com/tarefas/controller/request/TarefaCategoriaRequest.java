package br.com.tarefas.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TarefaCategoriaRequest {

	private Integer id;
	
	@NotBlank(message = "{categoria.nome.not-blank}")
	@Size(min=5, max = 150, message = "{categoria.nome.size}")
	private String nome;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


	
}
