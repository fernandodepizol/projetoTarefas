package br.com.tarefas.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.tarefas.exception.TarefaStatusException;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.model.TarefaStatus;
import br.com.tarefas.repository.TarefaRepository;
import br.com.tarefas.services.TarefaService;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

	@Mock
	private TarefaRepository repository;
	
	@InjectMocks
	private TarefaService service;
	
	@Test
	void naoDeveConcluirTarefaCancelada() {
		
		Integer idExemplo = 1;
		Tarefa tarefa= new Tarefa();
		tarefa.setId(idExemplo);
		tarefa.setDescricao("Tarefa 01");
		tarefa.setStatus(TarefaStatus.CANCELADA);
		Mockito.when(repository.findById(idExemplo)).thenReturn(Optional.of(tarefa));
		Assertions.assertThrows(TarefaStatusException.class, 
				() -> service.concluirTarefaPorId(idExemplo));
		
	}
	
	@Test
	void naoDeveCancelarTarefaConcluida() {
		Tarefa tarefa = new Tarefa();
		tarefa.setId(1);
		tarefa.setDescricao("Tarefa 01");
		tarefa.setStatus(TarefaStatus.CONCLUIDA);
		
		Mockito.when(repository.findById(1)).thenReturn(Optional.of(tarefa));
		
		Assertions.assertThrows(TarefaStatusException.class, 
				() -> service.cancelarTarefaPorId(1));
		
	}
	
	@Test
	void deveIniciarTarefa() {
		Tarefa tarefa = new Tarefa();
		tarefa.setId(1);
		tarefa.setDescricao("Tarefa 01");
		tarefa.setStatus(TarefaStatus.ABERTO);
		
		Mockito.when(repository.findById(1)).thenReturn(Optional.of(tarefa));
		
		Assertions.assertThrows(TarefaStatusException.class, 
				() -> service.iniciarTarefaPorId(1));
		
	}
	
}
