package com.miragaia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miragaia.dto.UsuarioDTO;
import com.miragaia.exception.RegraNegocioException;
import com.miragaia.model.entity.Usuario;
import com.miragaia.service.UsuarioService;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService service;
	
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
}
