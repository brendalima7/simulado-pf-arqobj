package simulado.pf.arqobjet.departamento;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import simulado.pf.arqobjet.projeto.Projeto;

import java.util.List;

@RestController
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping("/departamentos")
    public List<Departamento> list() {
        return departamentoService.list();
    }

    @GetMapping("/departamentos/{id}")
    public Departamento buscar(@PathVariable int id) {
        return departamentoService.buscarPorId(id);
    }

    @GetMapping("/departamentos/{id}/projetos")
    public List<Projeto> listarProjetos(@PathVariable int id) {
        return departamentoService.listarProjetos(id);
    }

    @PostMapping("/departamentos")
    @ResponseStatus(HttpStatus.CREATED)
    public Departamento salvar(@Valid @RequestBody Departamento departamento) {
        return departamentoService.salvar(departamento);
    }

    @PutMapping("/departamentos/{id}")
    public Departamento atualizar(@PathVariable int id, @Valid @RequestBody Departamento departamento) {
        return departamentoService.atualizar(id, departamento);
    }

    @DeleteMapping("/departamentos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable int id) {
        departamentoService.deletar(id);
    }
}
