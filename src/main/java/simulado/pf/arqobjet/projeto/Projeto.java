package simulado.pf.arqobjet.projeto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import simulado.pf.arqobjet.departamento.Departamento;
import simulado.pf.arqobjet.pessoa.Pessoa;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    private String descricao;

    // Todo projeto pertence a um departamento (obrigatório)
    @NotNull(message = "Departamento é obrigatório")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_departamento", nullable = false)
    private Departamento departamento;

    // Projeto é o dono da relação N:N com Pessoa
    @ManyToMany
    @JoinTable(
            name = "projeto_pessoa",
            joinColumns = @JoinColumn(name = "id_projeto"),
            inverseJoinColumns = @JoinColumn(name = "id_pessoa")
    )
    private List<Pessoa> pessoas = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public List<Pessoa> getPessoas() {
        return pessoas;
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }
}
