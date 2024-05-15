package br.com.tarefas.controller;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.tarefas.controller.assembler.TarefaCategoriaModelAssembler;
import br.com.tarefas.controller.assembler.TarefaModelAssembler;
import br.com.tarefas.controller.request.TarefaCategoriaRequest;
import br.com.tarefas.controller.response.TarefaCategoriaResponse;
import br.com.tarefas.controller.response.TarefaResponse;
import br.com.tarefas.model.TarefaCategoria;
import br.com.tarefas.services.TarefaCategoriaService;

@RestController
@RequestMapping("/categoria")
public class TarefaCategoriaController {

	@Autowired
	private TarefaCategoriaService service;
	
	@Autowired
	private ModelMapper mapper;
	

	@Autowired
	private TarefaCategoriaModelAssembler assembler;
	
	@GetMapping()
	public CollectionModel<EntityModel<TarefaCategoriaResponse>> todasCategorias(){
		List<TarefaCategoria> categorias = service.getTodasCategorias();
		
		List<EntityModel<TarefaCategoriaResponse>> tarefaCategoriaModel = categorias
				.stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(tarefaCategoriaModel,
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TarefaCategoriaController.class).todasCategorias())
				.withSelfRel()
		);
		/*return categorias.stream()
				.map(categoria -> mapper.map(categoria, TarefaCategoriaResponse.class))
				.collect(Collectors.toList());*/
	}
	
	@GetMapping("/{id}")
	public TarefaCategoriaResponse umaCategoria(@PathVariable Integer id) {
		return mapper.map(
				service.getCategoriaPorId(id),
				TarefaCategoriaResponse.class);
	}
	
	@PostMapping()
	public ResponseEntity<EntityModel<TarefaCategoriaResponse>> salvarCategoria(@RequestBody TarefaCategoriaRequest categoriaReq) {
		TarefaCategoria categoria = mapper.map(categoriaReq, TarefaCategoria.class);
		TarefaCategoria categoriaSalva = service.salvarCategoria(categoria);
		
		EntityModel<TarefaCategoriaResponse> categoriaModel = assembler.toModel(categoriaSalva);
		
		
		
		return ResponseEntity.created(categoriaModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(categoriaModel);
	}
	
	
	@DeleteMapping("/{id}")
	public void excluirCategoria(@PathVariable Integer id) {
		service.deleteById(id);
	}
	
}
