package simulado.pf.arqobjet.pessoa;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import simulado.pf.arqobjet.projeto.Projeto;

import java.util.List;

@RestController
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @GetMapping("/pessoas")
    public List<Pessoa> list() {
        return pessoaService.list();
    }

    @GetMapping("/pessoas/{id}")
    public Pessoa buscar(@PathVariable int id) {
        return pessoaService.buscarPorId(id);
    }

    @GetMapping("/pessoas/{id}/projetos")
    public List<Projeto> listarProjetos(@PathVariable int id) {
        return pessoaService.listarProjetos(id);
    }

    @PostMapping("/pessoas")
    @ResponseStatus(HttpStatus.CREATED)
    public Pessoa salvar(@Valid @RequestBody Pessoa pessoa) {
        return pessoaService.salvar(pessoa);
    }

    @PutMapping("/pessoas/{id}")
    public Pessoa atualizar(@PathVariable int id, @Valid @RequestBody Pessoa pessoa) {
        return pessoaService.atualizar(id, pessoa);
    }

    @DeleteMapping("/pessoas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable int id) {
        pessoaService.deletar(id);
    }
}
