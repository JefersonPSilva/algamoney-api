package com.algamoneyapi.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algamoneyapi.model.Categoria;
import com.algamoneyapi.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping
	public List<Categoria> listar(){ 
		//Caso não encontre nada retorna lista vazia
		return categoriaRepository.findAll();
	}
	
	@PostMapping
	//@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria, HttpServletResponse response) { 
		
		//Salva uma nova categoria
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		/**
		 * ServletUriComponentBuilder = Helper do Spring
		 * fromCurrentRequestUri = Pegar a URI da requisição atual
		 * path = adicionar o código
		 * buildAndExpand = adicionar o código da categoriaSalva na URI  
		 * response.setHeader = Settar o Header Location com essa URI
		 */
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
												.path("/{codigo}")
													.buildAndExpand(categoriaSalva.getCodigo()).toUri();
		//No Spring 2.0+ por padrão já retorna o Location
		//response.setHeader("Location", uri.toASCIIString());
		
		/**
		 * Retorna a categoria cadastrada para não ter necessidade de uma nova consulta
		 * created = informa qual e o status da requisição (201 CREATED)
		 * body = envia para o corpo o código da categoria cadastrada
		 */
		return ResponseEntity.created(uri).body(categoriaSalva);
	}
	
	@GetMapping("/{codigo}")
	public Optional<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
		//Optional do java 8 usada para representar um valor que pode ou não está presente
		//Caso o retorno seja null o código não está cadastrado
		return categoriaRepository.findById(codigo); 
	}
}
