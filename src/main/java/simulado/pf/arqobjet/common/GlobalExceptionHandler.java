package simulado.pf.arqobjet.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        ErrorResponseDTO erro = criar(HttpStatus.BAD_REQUEST, "Erro de validação", request);
        ex.getBindingResult().getFieldErrors().forEach(error ->
                erro.getErrors().add(error.getField() + ": " + error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleNaoEncontrado(
            NaoEncontradoException ex, HttpServletRequest request) {
        return resposta(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErrorResponseDTO> handleRegraNegocio(
            RegraNegocioException ex, HttpServletRequest request) {
        return resposta(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(
            BadCredentialsException ex, HttpServletRequest request) {
        return resposta(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrity(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        return resposta(HttpStatus.CONFLICT, "Violação de integridade dos dados (registro duplicado ou em uso)", request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleJsonInvalido(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        return resposta(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou mal formatado", request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleTipoInvalido(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return resposta(HttpStatus.BAD_REQUEST, "Parâmetro inválido: " + ex.getName(), request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRotaInexistente(
            NoResourceFoundException ex, HttpServletRequest request) {
        return resposta(HttpStatus.NOT_FOUND, "Recurso não encontrado", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenerico(
            Exception ex, HttpServletRequest request) {
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor", request);
    }

    private ResponseEntity<ErrorResponseDTO> resposta(HttpStatus status, String mensagem, HttpServletRequest request) {
        return ResponseEntity.status(status).body(criar(status, mensagem, request));
    }

    private ErrorResponseDTO criar(HttpStatus status, String mensagem, HttpServletRequest request) {
        return new ErrorResponseDTO(status.value(), status.getReasonPhrase(), mensagem, request.getRequestURI());
    }
}
