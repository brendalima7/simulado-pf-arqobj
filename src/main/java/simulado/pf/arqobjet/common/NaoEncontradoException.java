package simulado.pf.arqobjet.common;

// Lançada quando um recurso (pessoa, projeto, departamento...) não existe -> 404
public class NaoEncontradoException extends RuntimeException {

    public NaoEncontradoException(String message) {
        super(message);
    }
}
