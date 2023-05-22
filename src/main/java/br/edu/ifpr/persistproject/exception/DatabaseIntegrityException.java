package br.edu.ifpr.persistproject.exception;

public class DatabaseIntegrityException extends RuntimeException{
    public DatabaseIntegrityException(String mensagem){
        super(mensagem);
    }
}
