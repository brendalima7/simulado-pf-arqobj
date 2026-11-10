package simulado.pf.arqobjet.departamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simulado.pf.arqobjet.common.NaoEncontradoException;
import simulado.pf.arqobjet.common.RegraNegocioException;
import simulado.pf.arqobjet.projeto.Projeto;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public Departamento salvar(Departamento departamento) {
        departamento.setId(0); // garante que é criação
        return departamentoRepository.save(departamento);
    }

    public List<Departamento> list() {
        return departamentoRepository.findAll();
    }

    public Departamento buscarPorId(int id) {
        Optional<Departamento> departamento = departamentoRepository.findById(id);

        if (departamento.isEmpty()) {
            throw new NaoEncontradoException("Departamento não encontrado");
        }

        return departamento.get();
    }

    @Transactional
    public Departamento atualizar(int id, Departamento dados) {
        Departamento departamento = buscarPorId(id);
        departamento.setNome(dados.getNome());
        return departamentoRepository.save(departamento);
    }

    @Transactional
    public void deletar(int id) {
        Departamento departamento = buscarPorId(id);
        if (!departamento.getProjetos().isEmpty()) {
            throw new RegraNegocioException("Departamento possui projetos e não pode ser removido");
        }
        departamentoRepository.delete(departamento);
    }

    public List<Projeto> listarProjetos(int id) {
        Departamento departamento = buscarPorId(id);
        return departamento.getProjetos();
    }
}
