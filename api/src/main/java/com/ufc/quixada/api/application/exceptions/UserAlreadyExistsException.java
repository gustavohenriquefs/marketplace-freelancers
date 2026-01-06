package com.ufc.quixada.api.application.exceptions;

public class UserAlreadyExistsException extends BusinessException {
    public UserAlreadyExistsException(String email) {
        super("Usuário com email " + email + " já existe");
    }
}