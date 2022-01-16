package com.miragaia.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miragaia.exception.RegraNegocioException;
import com.miragaia.model.entity.Usuario;
import com.miragaia.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;
	
	@Transactional
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> userBanco = repository.findByEmail(email);
		if(!userBanco.isPresent()) {
			throw new RegraNegocioException("Email invalido.");
		}
		if(!userBanco.get().getSenha().equalsIgnoreCase(senha)) {
			throw new RegraNegocioException("Senha Invalida.");
		}
		return userBanco.get();
	}
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		validarEmail(usuario.getEmail());
		validarUsuario(usuario);
		return repository.save(usuario);
	}
	
	public void validarEmail(String email) {
		boolean exists = repository.existsByEmail(email);
		if(exists) {
			throw new RegraNegocioException("Já existe um usuário com esse Email.");
		}
	}
	
	public void validarUsuario(Usuario usuario) {
		if(usuario.getNome() == null || usuario.getNome().trim().equals("")) {
			throw new RegraNegocioException("Digite um nome válido.");
		}
		if(usuario.getSenha() == null || usuario.getSenha().length() > 8) {
			throw new RegraNegocioException("Digite uma senha válida");
		}
		if(usuario.getEmail() == null || usuario.getEmail().trim().equals("")) {
			throw new RegraNegocioException("Digite um email valido.");
		}
	}
	

}
