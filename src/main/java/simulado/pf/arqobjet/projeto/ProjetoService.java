package simulado.pf.arqobjet.projeto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simulado.pf.arqobjet.common.NaoEncontradoException;
import simulado.pf.arqobjet.common.RegraNegocioException;
import simulado.pf.arqobjet.departamento.Departamento;
import simulado.pf.arqobjet.departamento.DepartamentoService;
import simulado.pf.arqobjet.pessoa.Pessoa;
import simulado.pf.arqobjet.pessoa.PessoaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private PessoaService pessoaService;

    @Transactional
    public Projeto salvar(Projeto projeto) {
        // Busca o departamento informado no corpo: { "departamento": { "id": 1 } }
        Departamento departamento = departamentoService.buscarPorId(projeto.getDepartamento().getId());

        projeto.setId(0); // garante que é criação
        projeto.setDepartamento(departamento);
        projeto.setPessoas(new ArrayList<>()); // pessoas são adicionadas pelo endpoint próprio

        return projetoRepository.save(projeto);
    }

    public List<Projeto> list() {
        return projetoRepository.findAll();
    }

    public Projeto buscarPorId(int id) {
        Optional<Projeto> projeto = projetoRepository.findById(id);

        if (projeto.isEmpty()) {
            throw new NaoEncontradoException("Projeto não encontrado");
        }

        return projeto.get();
    }

    @Transactional
    public Projeto atualizar(int id, Projeto dados) {
        Projeto projeto = buscarPorId(id);
        Departamento departamento = departamentoService.buscarPorId(dados.getDepartamento().getId());

        projeto.setNome(dados.getNome());
        projeto.setDescricao(dados.getDescricao());
        projeto.setDepartamento(departamento);

        return projetoRepository.save(projeto);
    }

    @Transactional
    public void deletar(int id) {
        Projeto projeto = buscarPorId(id);
        projetoRepository.delete(projeto); // remove também as linhas de projeto_pessoa
    }

    @Transactional
    public Projeto adicionarPessoa(int idProjeto, int idPessoa) {
        Projeto projeto = buscarPorId(idProjeto);
        Pessoa pessoa = pessoaService.buscarPorId(idPessoa);

        if (projeto.getPessoas().contains(pessoa)) {
            throw new RegraNegocioException("Pessoa já participa deste projeto");
        }

        projeto.getPessoas().add(pessoa);
        pessoa.getProjetos().add(projeto);

        return projetoRepository.save(projeto);
    }

    @Transactional
    public Projeto removerPessoa(int idProjeto, int idPessoa) {
        Projeto projeto = buscarPorId(idProjeto);
        Pessoa pessoa = pessoaService.buscarPorId(idPessoa);

        if (!projeto.getPessoas().remove(pessoa)) {
            throw new RegraNegocioException("Pessoa não participa deste projeto");
        }
        pessoa.getProjetos().remove(projeto);

        return projetoRepository.save(projeto);
    }
}
