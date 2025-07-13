package com.ecomerce.service;

import com.ecomerce.model.Usuario;
import com.ecomerce.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Clase de Servicio para Usuario.
 * Contiene la lógica de negocio para la gestión de usuarios.
 * Interactúa con UsuarioRepository para acceder a los datos.
 */
@Service // Indica que esta clase es un componente de servicio de Spring.
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Inyección de dependencias: Spring inyecta una instancia de UsuarioRepository.
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Guarda un nuevo usuario o actualiza uno existente.
     * @param usuario El objeto Usuario a guardar.
     * @return El Usuario guardado.
     */
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Obtiene todos los usuarios.
     * @return Una lista de todos los usuarios.
     */
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Obtiene un usuario por su ID.
     * @param id El ID del usuario.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Elimina un usuario por su ID.
     * @param id El ID del usuario a eliminar.
     */
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Puedes añadir más métodos de lógica de negocio aquí, como:
    // - validarUsuario(Usuario usuario)
    // - encriptarPassword(String password)
    // - iniciarSesion(String nombreUsuario, String password)
}
