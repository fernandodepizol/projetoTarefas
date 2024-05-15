package br.com.tarefas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.tarefas.model.TarefaCategoria;
import br.com.tarefas.repository.TarefaCategoriaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TarefaCategoriaService {

	@Autowired
	private TarefaCategoriaRepository repository;

	
	public List<TarefaCategoria> getTodasCategorias(){
		return repository.findAll();
	}
	
	/*
	 * public List<TarefaCategoria> getCategoriaPorNome(String nome){ return
	 * repository.findByDescricaoLike(nome); }
	 */
	
	
	public TarefaCategoria getCategoriaPorId(Integer id) {
		return repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
	}
	
	public TarefaCategoria salvarCategoria(TarefaCategoria categoria) {
		return repository.save(categoria);
	}
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}
}
