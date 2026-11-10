package simulado.pf.arqobjet.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, int id);
}
