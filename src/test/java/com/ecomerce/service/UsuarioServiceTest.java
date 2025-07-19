package com.ecomerce.service;

import com.ecomerce.model.Usuario;
import com.ecomerce.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; // Importa todos los métodos estáticos de Mockito

/**
 * Clase de pruebas unitarias para UsuarioService.
 * Utiliza JUnit 5 para el framework de pruebas y Mockito para la creación de mocks.
 */
@ExtendWith(MockitoExtension.class) // Habilita la integración de Mockito con JUnit 5
public class UsuarioServiceTest {

    @Mock // Crea un mock de UsuarioRepository
    private UsuarioRepository usuarioRepository;

    @InjectMocks // Inyecta los mocks (en este caso, usuarioRepository) en UsuarioService
    private UsuarioService usuarioService;

    private Usuario usuarioEjemplo; // Objeto de usuario de ejemplo para usar en las pruebas

    @BeforeEach // Este método se ejecuta antes de cada prueba
    void setUp() {
        // Inicializa un usuario de ejemplo para asegurar un estado limpio en cada prueba
        usuarioEjemplo = new Usuario(
                1L, // ID de ejemplo
                "Juan",
                "Perez",
                "juan.perez@example.com",
                "password123",
                "+56912345678",
                "Calle Ficticia 123",
                LocalDate.of(2024, 7, 18), // Fecha de registro actual
                LocalDate.of(1990, 1, 1),
                "Masculino"
        );
    }

    @Test
    @DisplayName("Debe guardar un usuario exitosamente")
    void guardarUsuario_debeGuardarExitosamente() {
        // Define el comportamiento del mock: cuando se llame a save(), devuelve el usuario de ejemplo
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEjemplo);

        // Llama al método del servicio que estamos probando
        Usuario usuarioGuardado = usuarioService.guardar(usuarioEjemplo);

        // Verifica que el usuario no sea nulo y que su ID sea el esperado
        assertNotNull(usuarioGuardado, "El usuario guardado no debería ser nulo");
        assertEquals(usuarioEjemplo.getId(), usuarioGuardado.getId(), "El ID del usuario guardado debe coincidir");
        assertEquals(usuarioEjemplo.getEmail(), usuarioGuardado.getEmail(), "El email del usuario guardado debe coincidir");

        // Verifica que el método save() del repositorio fue llamado exactamente una vez con cualquier objeto Usuario
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe obtener un usuario por ID si existe")
    void obtenerUsuarioPorId_debeRetornarUsuarioSiExiste() {
        // Define el comportamiento del mock: cuando se llame a findById() con el ID del ejemplo, devuelve el usuario de ejemplo
        when(usuarioRepository.findById(usuarioEjemplo.getId())).thenReturn(Optional.of(usuarioEjemplo));

        // Llama al método del servicio
        Optional<Usuario> encontrado = usuarioService.obtenerPorId(usuarioEjemplo.getId());

        // Verifica que el usuario esté presente y que sus datos coincidan
        assertTrue(encontrado.isPresent(), "El usuario debería estar presente");
        assertEquals(usuarioEjemplo.getEmail(), encontrado.get().getEmail(), "El email del usuario encontrado debe coincidir");

        // Verifica que findById() fue llamado una vez
        verify(usuarioRepository, times(1)).findById(usuarioEjemplo.getId());
    }

    @Test
    @DisplayName("No debe obtener un usuario por ID si no existe")
    void obtenerUsuarioPorId_debeRetornarVacioSiNoExiste() {
        // Define el comportamiento del mock: cuando se llame a findById() con un ID que no existe, devuelve un Optional vacío
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        // Llama al método del servicio
        Optional<Usuario> encontrado = usuarioService.obtenerPorId(2L);

        // Verifica que el Optional esté vacío
        assertFalse(encontrado.isPresent(), "El usuario no debería estar presente");

        // Verifica que findById() fue llamado una vez
        verify(usuarioRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Debe obtener todos los usuarios")
    void obtenerTodosLosUsuarios_debeRetornarListaDeUsuarios() {
        // Crea una lista de usuarios de ejemplo
        List<Usuario> usuarios = Arrays.asList(usuarioEjemplo,
                new Usuario(2L, "Maria", "Lopez", "maria.lopez@example.com", "pass456", "+56987654321", "Av. Principal 456", LocalDate.of(2024, 7, 17), LocalDate.of(1992, 2, 2), "Femenino"));

        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve la lista de usuarios
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Llama al método del servicio
        List<Usuario> todosLosUsuarios = usuarioService.obtenerTodos();

        // Verifica que la lista no sea nula, no esté vacía y tenga el tamaño correcto
        assertNotNull(todosLosUsuarios, "La lista de usuarios no debería ser nula");
        assertFalse(todosLosUsuarios.isEmpty(), "La lista de usuarios no debería estar vacía");
        assertEquals(2, todosLosUsuarios.size(), "La lista debería contener dos usuarios");

        // Verifica que findAll() fue llamado una vez
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe eliminar un usuario por ID")
    void eliminarUsuario_debeEliminarExitosamente() {
        // No necesitamos que deleteById() devuelva nada, solo verificar que fue llamado
        doNothing().when(usuarioRepository).deleteById(usuarioEjemplo.getId());

        // Llama al método del servicio
        usuarioService.eliminar(usuarioEjemplo.getId());

        // Verifica que deleteById() fue llamado exactamente una vez con el ID correcto
        verify(usuarioRepository, times(1)).deleteById(usuarioEjemplo.getId());
    }
}
