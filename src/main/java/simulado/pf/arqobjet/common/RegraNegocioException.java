package simulado.pf.arqobjet.common;

// Lançada quando uma regra de negócio é violada (ex: CPF duplicado) -> 400
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String message) {
        super(message);
    }
}
