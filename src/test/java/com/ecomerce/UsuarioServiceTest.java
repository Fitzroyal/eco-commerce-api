package com.ecomerce.service;

import com.ecomerce.model.Usuario;
import com.ecomerce.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para UsuarioService.
 * Utiliza Mockito para simular (mockear) las dependencias (UsuarioRepository)
 * y JUnit 5 para definir y ejecutar las pruebas.
 */
class UsuarioServiceTest {

    @Mock // Anotación de Mockito para crear un objeto simulado (mock) de UsuarioRepository.
    private UsuarioRepository usuarioRepository;

    @InjectMocks // Anotación de Mockito para inyectar el mock de UsuarioRepository en UsuarioService.
    private UsuarioService usuarioService;

    /**
     * Método que se ejecuta antes de cada prueba.
     * Inicializa los mocks y el objeto bajo prueba (usuarioService).
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks anotados en esta clase.
    }

    /**
     * Prueba para el método guardar(Usuario).
     * Verifica que el usuario se guarda correctamente y que el método save() del repositorio es llamado.
     */
    @Test
    void testGuardarUsuario() {
        // 1. Arrange (Preparar): Crear un objeto Usuario para la prueba.
        Usuario usuario = new Usuario(null, "Test", "User", "test@example.com", "pass", "123", "dir", LocalDate.now(), LocalDate.of(1990, 1, 1), "M");
        // Definir el comportamiento del mock: cuando se llame a usuarioRepository.save(cualquierUsuario),
        // devuelve un nuevo Usuario con un ID asignado (simulando la DB).
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(new Usuario(1L, "Test", "User", "test@example.com", "pass", "123", "dir", LocalDate.now(), LocalDate.of(1990, 1, 1), "M"));

        // 2. Act (Actuar): Llamar al método que estamos probando.
        Usuario usuarioGuardado = usuarioService.guardar(usuario);

        // 3. Assert (Verificar): Comprobar los resultados.
        assertNotNull(usuarioGuardado); // Asegura que el objeto no es nulo.
        assertNotNull(usuarioGuardado.getId()); // Verifica que se asignó un ID.
        assertEquals("Test", usuarioGuardado.getNombre()); // Verifica que el nombre sea correcto.
        // Verifica que el método save() del mock fue llamado exactamente una vez con el objeto usuario.
        verify(usuarioRepository, times(1)).save(usuario);
    }

    /**
     * Prueba para el método obtenerPorId(Long) cuando el usuario existe.
     */
    @Test
    void testObtenerPorIdExistente() {
        // 1. Arrange: Crear un usuario simulado que el repositorio "encontraría".
        Usuario usuario = new Usuario(1L, "Test", "User", "test@example.com", "pass", "123", "dir", LocalDate.now(), LocalDate.of(1990, 1, 1), "M");
        // Definir el comportamiento del mock: cuando se llame a findById(1L), devuelve un Optional con el usuario.
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // 2. Act: Llamar al método.
        Optional<Usuario> encontrado = usuarioService.obtenerPorId(1L);

        // 3. Assert:
        assertTrue(encontrado.isPresent()); // Verifica que el Optional no está vacío.
        assertEquals("Test", encontrado.get().getNombre()); // Verifica el nombre del usuario encontrado.
        verify(usuarioRepository, times(1)).findById(1L); // Verifica que findById() fue llamado una vez.
    }

    /**
     * Prueba para el método obtenerPorId(Long) cuando el usuario no existe.
     */
    @Test
    void testObtenerPorIdNoExistente() {
        // 1. Arrange: Definir el comportamiento del mock: cuando se llame a findById(2L), devuelve un Optional vacío.
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        // 2. Act: Llamar al método.
        Optional<Usuario> encontrado = usuarioService.obtenerPorId(2L);

        // 3. Assert:
        assertFalse(encontrado.isPresent()); // Verifica que el Optional está vacío.
        verify(usuarioRepository, times(1)).findById(2L); // Verifica que findById() fue llamado una vez.
    }

    /**
     * Prueba para el método obtenerTodos().
     */
    @Test
    void testObtenerTodos() {
        // 1. Arrange: Crear una lista de usuarios simulados.
        Usuario usuario1 = new Usuario(1L, "Ana", "G", "ana@e.com", "p1", "1", "d1", LocalDate.now(), LocalDate.of(1990, 1, 1), "F");
        Usuario usuario2 = new Usuario(2L, "Juan", "P", "juan@e.com", "p2", "2", "d2", LocalDate.now(), LocalDate.of(1985, 5, 5), "M");
        // Definir el comportamiento del mock: cuando se llame a findAll(), devuelve la lista de usuarios.
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        // 2. Act: Llamar al método.
        List<Usuario> usuarios = usuarioService.obtenerTodos();

        // 3. Assert:
        assertFalse(usuarios.isEmpty()); // Verifica que la lista no está vacía.
        assertEquals(2, usuarios.size()); // Verifica el tamaño de la lista.
        assertEquals("Ana", usuarios.get(0).getNombre()); // Verifica un elemento específico.
        verify(usuarioRepository, times(1)).findAll(); // Verifica que findAll() fue llamado una vez.
    }

    /**
     * Prueba para el método eliminar(Long).
     */
    @Test
    void testEliminarUsuario() {
        // 1. Arrange: No necesitamos un retorno específico para deleteById, solo verificar que se llama.
        Long idAEliminar = 1L;
        // No es necesario mockear el comportamiento de deleteById si solo verificamos la llamada.

        // 2. Act: Llamar al método.
        usuarioService.eliminar(idAEliminar);

        // 3. Assert: Verificar que el método deleteById() del mock fue llamado exactamente una vez con el ID correcto.
        verify(usuarioRepository, times(1)).deleteById(idAEliminar);
    }
}
