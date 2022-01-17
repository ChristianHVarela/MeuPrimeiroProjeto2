package com.miragaia.controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miragaia.dto.UsuarioDTO;
import com.miragaia.exception.RegraNegocioException;
import com.miragaia.model.entity.Usuario;
import com.miragaia.service.LancamentoService;
import com.miragaia.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioService service;
	private final LancamentoService lancamentoService;
	
	@PostMapping("/salvar")
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		try {
			Usuario user = new Usuario(dto);
			Usuario userBanco = service.salvar(user);
			return new ResponseEntity(userBanco, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
		try {
			Usuario usuario = new Usuario(dto);
			Usuario userBanco = service.autenticar(usuario.getEmail(), usuario.getSenha());
			return new ResponseEntity(userBanco, HttpStatus.OK);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("/saldo/{id}")
	public ResponseEntity consultarSaldo(@PathVariable("id") Long id) {
		if(!service.findById(id).isPresent()) {
			return ResponseEntity.badRequest().body("Usuário não encontrado.");
		}
		BigDecimal saldo = lancamentoService.obterSaldoDoUsuario(id);
		return ResponseEntity.ok(saldo);
	}
}
