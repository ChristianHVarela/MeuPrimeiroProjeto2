package com.miragaia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.miragaia.dto.AtualizaStatusDTO;
import com.miragaia.dto.LancamentoDTO;
import com.miragaia.enuns.StatusLancamento;
import com.miragaia.enuns.TipoLancamento;
import com.miragaia.exception.RegraNegocioException;
import com.miragaia.model.entity.Lancamento;
import com.miragaia.model.entity.Usuario;
import com.miragaia.service.LancamentoService;
import com.miragaia.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {
	
	private final LancamentoService service;
	private final UsuarioService usuarioService;
	
	@PostMapping("/salvar")
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento entidade = service.converterDTO(dto);
			entidade = service.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/atualizar/{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
		return service.findById(id).map( data -> {
			try {
				Lancamento entidade = service.converterDTO(dto);
				entidade.setId(data.getId());
				service.atualizar(entidade);
				return ResponseEntity.ok(entidade);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity delete(@PathVariable("id") Long id) {
		return service.findById(id).map(data ->{
			service.deletar(data);
			return new ResponseEntity(data, HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));
	}
	
	@GetMapping("/buscar")
	public ResponseEntity buscar(
				@RequestParam(value = "descricao", required = false)String descricao,
				@RequestParam(value = "mes", required = false)Integer mes,
				@RequestParam(value = "ano", required = false)Integer ano,
				@RequestParam(value = "tipo", required = false)String tipo,
				@RequestParam("usuario")Long idUsuario) {
		Lancamento filtro = new Lancamento();
		filtro.setAno(ano);
		filtro.setMes(mes);
		filtro.setDescricao(descricao);
		filtro.setTipo(TipoLancamento.valueOf(tipo));
		Optional<Usuario> user = usuarioService.findById(idUsuario);
		if(!user.isPresent()) {
			return ResponseEntity.badRequest().body("Usuario não encontrado.");
		} else {
			filtro.setUsuario(user.get());
		}
		List<Lancamento> lancamentos = service.buscar(filtro);
		return ResponseEntity.ok(lancamentos);
	}
	
	@PutMapping("/atualizaStatus/{id}")
	public ResponseEntity atualizaStatus(@PathVariable("id")Long id, @RequestBody AtualizaStatusDTO dto) {
		return service.findById(id).map(data -> {
			StatusLancamento statusFormatado = StatusLancamento.valueOf(dto.getStatus());
			if(statusFormatado == null) {
				return ResponseEntity.badRequest().body("Status não é valido, insira um status correto.");
			}
			try {
				data.setStatus(statusFormatado);
				data = service.atualizar(data);
				return ResponseEntity.ok(data);
			} catch (Exception e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));
	}
}
