package br.com.tarefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.tarefas.model.Tarefa;
import java.util.List;


public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {
	
	public List<Tarefa> findByDescricao(String descricao);
	public List<Tarefa> findByDescricaoLike(String descricao);
	
	

}
