package com.miragaia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miragaia.exception.RegraNegocioException;
import com.miragaia.model.entity.Usuario;
import com.miragaia.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Transactional
	public Usuario autenticar(String email, String senha) {
		
		return null;
	}
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		
		return null;
	}
	
	public void validar(String email) {
		boolean exists = usuarioRepository.existsByEmail(email);
		if(exists) {
			throw new RegraNegocioException("Já existe um usuário com esse Email.");
		}
	}

}
