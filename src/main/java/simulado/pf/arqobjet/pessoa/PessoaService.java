package simulado.pf.arqobjet.pessoa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simulado.pf.arqobjet.common.NaoEncontradoException;
import simulado.pf.arqobjet.common.RegraNegocioException;
import simulado.pf.arqobjet.projeto.Projeto;

import java.util.ArrayList;
import java.util.List;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa salvar(Pessoa pessoa) {
        if (pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new RegraNegocioException("Já existe uma pessoa com este CPF");
        }
        pessoa.setId(0); // garante que é criação
        return pessoaRepository.save(pessoa);
    }

    public List<Pessoa> list() {
        return pessoaRepository.findAll();
    }

    public Pessoa buscarPorId(int id) {
        return pessoaRepository
                .findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Pessoa não encontrada"));
    }

    @Transactional
    public Pessoa atualizar(int id, Pessoa dados) {
        Pessoa pessoa = buscarPorId(id);

        if (pessoaRepository.existsByCpfAndIdNot(dados.getCpf(), id)) {
            throw new RegraNegocioException("Já existe uma pessoa com este CPF");
        }

        pessoa.setNome(dados.getNome());
        pessoa.setCpf(dados.getCpf());
        return pessoaRepository.save(pessoa);
    }

    @Transactional
    public void deletar(int id) {
        Pessoa pessoa = buscarPorId(id);

        // Remove a pessoa de todos os projetos antes (Projeto é o dono da relação)
        for (Projeto projeto : new ArrayList<>(pessoa.getProjetos())) {
            projeto.getPessoas().remove(pessoa);
        }
        pessoa.getProjetos().clear();

        pessoaRepository.delete(pessoa);
    }

    @Transactional(readOnly = true)
    public List<Projeto> listarProjetos(int id) {
        return List.copyOf(buscarPorId(id).getProjetos());
    }
}
