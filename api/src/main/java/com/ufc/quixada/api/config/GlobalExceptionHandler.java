package com.ufc.quixada.api.config;

import com.ufc.quixada.api.application.exceptions.BusinessException;
import com.ufc.quixada.api.application.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleNotFoundException(NotFoundException ex) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            ex.getMessage()
        );
        problemDetail.setTitle("Recurso não encontrado");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleBusinessException(BusinessException ex) {
        log.warn("Erro de negócio: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.getMessage()
        );
        problemDetail.setTitle("Erro de validação de negócio");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Erro de validação: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();

            if (errorMessage != null && errorMessage.contains("Failed to convert")) {
                if (fieldName.equals("skillsIds")) {
                    errorMessage = "Formato inválido. Envie uma lista de IDs numéricos, ex: [1, 2, 3]";
                } else if (fieldName.equals("files")) {
                    errorMessage = "Use multipart/form-data para enviar arquivos, ou deixe vazio se não houver arquivos";
                } else {
                    errorMessage = "Formato de dados inválido para este campo";
                }
            }

            errors.put(fieldName, errorMessage);
        });

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Erro de validação nos campos enviados"
        );
        problemDetail.setTitle("Erro de validação");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleTypeMismatchException(TypeMismatchException ex) {
        log.warn("Erro de conversão de tipo: {}", ex.getMessage());

        String fieldName = ex.getPropertyName() != null ? ex.getPropertyName() : "campo";
        String message = String.format("Tipo de dado inválido para o campo '%s'. Esperado: %s",
            fieldName,
            ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido");

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            message
        );
        problemDetail.setTitle("Erro de tipo de dado");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("field", fieldName);

        return problemDetail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("Erro ao ler mensagem HTTP: {}", ex.getMessage());

        String message = "Não foi possível ler o corpo da requisição. Verifique se o JSON está bem formatado.";

        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("multipart/form-data")) {
                message = "Este endpoint espera multipart/form-data para upload de arquivos. " +
                         "Se não houver arquivos, envie os dados como JSON sem o campo 'files'.";
            } else if (ex.getMessage().contains("JSON")) {
                message = "Formato JSON inválido. Verifique vírgulas, chaves e tipos de dados.";
            }
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            message
        );
        problemDetail.setTitle("Erro de formato de dados");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleGenericException(Exception ex) {
        log.error("Erro não tratado: ", ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde."
        );
        problemDetail.setTitle("Erro interno");
        problemDetail.setProperty("timestamp", Instant.now());


        return problemDetail;
    }
}

