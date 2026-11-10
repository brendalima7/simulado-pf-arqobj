package simulado.pf.arqobjet.projeto;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import simulado.pf.arqobjet.pessoa.Pessoa;

import java.util.List;

@RestController
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @GetMapping("/projetos")
    public List<Projeto> list() {
        return projetoService.list();
    }

    @GetMapping("/projetos/{id}")
    public Projeto buscar(@PathVariable int id) {
        return projetoService.buscarPorId(id);
    }

    @GetMapping("/projetos/{id}/pessoas")
    public List<Pessoa> listarPessoas(@PathVariable int id) {
        return projetoService.buscarPorId(id).getPessoas();
    }

    // Corpo: { "nome": "...", "descricao": "...", "departamento": { "id": 1 } }
    @PostMapping("/projetos")
    @ResponseStatus(HttpStatus.CREATED)
    public Projeto salvar(@Valid @RequestBody Projeto projeto) {
        return projetoService.salvar(projeto);
    }

    @PutMapping("/projetos/{id}")
    public Projeto atualizar(@PathVariable int id, @Valid @RequestBody Projeto projeto) {
        return projetoService.atualizar(id, projeto);
    }

    @DeleteMapping("/projetos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable int id) {
        projetoService.deletar(id);
    }

    @PostMapping("/projetos/{idProjeto}/pessoas/{idPessoa}")
    public Projeto adicionarPessoa(@PathVariable int idProjeto, @PathVariable int idPessoa) {
        return projetoService.adicionarPessoa(idProjeto, idPessoa);
    }

    @DeleteMapping("/projetos/{idProjeto}/pessoas/{idPessoa}")
    public Projeto removerPessoa(@PathVariable int idProjeto, @PathVariable int idPessoa) {
        return projetoService.removerPessoa(idProjeto, idPessoa);
    }
}
