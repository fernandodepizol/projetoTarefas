package br.com.tarefas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.tarefas.exception.TarefaStatusException;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.model.TarefaStatus;
import br.com.tarefas.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TarefaService {
	@Autowired
	private TarefaRepository repository;

	
	public List<Tarefa> getTodasTarefas(){
		return repository.findAll();
	}
	
	public List<Tarefa> getTarefasPorDescricao(String descricao){
		return repository.findByDescricao(descricao);
	}
	
	
	public Tarefa getTarefaPorId(Integer id) {
		return repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
	}
	
	public Tarefa salvarTarefa(Tarefa tarefa) {
		return repository.save(tarefa);
	}
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}
	
	public Tarefa iniciarTarefaPorId(Integer id) {
		Tarefa tarefa = getTarefaPorId(id);
		
		if(!TarefaStatus.ABERTO.equals(tarefa.getStatus()))
			throw new TarefaStatusException();
		
		tarefa.setStatus(TarefaStatus.EM_ANDAMENTO);
		repository.save(tarefa);
		return tarefa;
	}
	
	public Tarefa concluirTarefaPorId(Integer id) {
		Tarefa tarefa = getTarefaPorId(id);
		
		if(TarefaStatus.CANCELADA.equals(tarefa.getStatus()))
			throw new TarefaStatusException();
		
		tarefa.setStatus(TarefaStatus.CONCLUIDA);
		repository.save(tarefa);
		return tarefa;
	}
	
	public Tarefa cancelarTarefaPorId(Integer id) {
		Tarefa tarefa = getTarefaPorId(id);
		
		if(TarefaStatus.CONCLUIDA.equals(tarefa.getStatus()))
			throw new TarefaStatusException("Você não pode cancelar uma tarefa concluída");
		
		tarefa.setStatus(TarefaStatus.CANCELADA);
		repository.save(tarefa);
		return tarefa;
	}
}
