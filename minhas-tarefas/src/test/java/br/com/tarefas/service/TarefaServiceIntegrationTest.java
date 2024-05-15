package br.com.tarefas.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.tarefas.exception.TarefaStatusException;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.model.TarefaStatus;
import br.com.tarefas.services.TarefaService;

@SpringBootTest
public class TarefaServiceIntegrationTest {
	
	@Autowired
	private TarefaService service;
	
	@Test
	void deveIniciarTarefa() {
		Tarefa tarefa = service.iniciarTarefaPorId(3);
		Assertions.assertEquals(TarefaStatus.EM_ANDAMENTO, tarefa.getStatus());
	}
	
	
	@Test
	void naoDeveIniciarTarefaConcluida() {
		Tarefa tarefa = service.getTarefaPorId(3);
		
		if(!TarefaStatus.ABERTO.equals(tarefa.getStatus()))
			throw new TarefaStatusException();
		
		
		tarefa.setStatus(TarefaStatus.CONCLUIDA);
		service.salvarTarefa(tarefa);
		//tarefa = service.iniciarTarefaPorId(3);
		Assertions.assertThrows(TarefaStatusException.class, 
				() -> service.iniciarTarefaPorId(3));
	}
	
	@Test
	void naoDeveConcluirTarefaCancelada() {
		Tarefa tarefa = service.getTarefaPorId(3);
		
		if(TarefaStatus.CONCLUIDA.equals(tarefa.getStatus()))
			throw new TarefaStatusException();
		
		
		tarefa.setStatus(TarefaStatus.CANCELADA);
		service.salvarTarefa(tarefa);
		Assertions.assertThrows(TarefaStatusException.class, 
				() -> service.concluirTarefaPorId(3));
	}
}
