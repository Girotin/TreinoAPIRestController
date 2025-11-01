package com.example.demo; // Adapte ao seu pacote

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// 1. Define que esta classe é um Controller REST
@RestController
// 2. Define um prefixo para todos os endpoints da classe (ex: /api/usuarios)
@RequestMapping("/api/usuarios")
public class UsuarioController {

    // 3. Nosso "banco de dados" estático (em memória)
    private final List<Usuario> usuarios = new ArrayList<>();
    // 4. Um contador para gerar IDs únicos
    private final AtomicLong proximoId = new AtomicLong(1);

    /**
     * Requisito: Método POST que cria um novo recurso
     * HTTP POST /api/usuarios
     * Recebe um JSON no corpo da requisição
     */
    @PostMapping
    public Usuario criarUsuario(@RequestBody UsuarioDTO dto) {
        // Cria o novo usuário com um ID gerado
        Usuario novoUsuario = new Usuario(
                proximoId.getAndIncrement(), // Pega o ID atual e incrementa
                dto.nome(),
                dto.email()
        );
        // Adiciona na nossa lista estática
        usuarios.add(novoUsuario);
        // Retorna o usuário criado (Spring converte para JSON automaticamente [cite: 362])
        return novoUsuario;
    }

    /**
     * Requisito: Método GET que busca todos os dados
     * HTTP GET /api/usuarios
     */
    @GetMapping
    public List<Usuario> buscarTodos() {
        return usuarios;
    }

    /**
     * Requisito: Método GET que busca um recurso pelo ID
     * HTTP GET /api/usuarios/{id}
     * O {id} na URL é um "Path Variable"
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        // Usa a API de Streams do Java para buscar na lista
        return usuarios.stream()
                .filter(u -> u.id().equals(id))
                .findFirst()
                .map(usuario -> ResponseEntity.ok(usuario)) // Se achar, retorna 200 OK
                .orElse(ResponseEntity.notFound().build()); // Se não, retorna 404 Not Found
    }

    /**
     * Requisito: Método GET que realiza uma query (filtro)
     * HTTP GET /api/usuarios/filtrar?nome=...
     * Usa um "Request Parameter" (?nome=)
     */
    @GetMapping("/filtrar")
    public List<Usuario> filtrarPorNome(@RequestParam(name = "nome") String nome) {
        // Usa a API de Streams para filtrar a lista
        return usuarios.stream()
                .filter(u -> u.nome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Requisito: Método DELETE que apaga um recurso
     * HTTP DELETE /api/usuarios/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        // Tenta remover o usuário da lista pelo ID
        boolean removeu = usuarios.removeIf(u -> u.id().equals(id));

        if (removeu) {
            // Se removeu, retorna 204 No Content (sucesso, sem corpo)
            return ResponseEntity.noContent().build();
        } else {
            // Se não achou, retorna 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }
}