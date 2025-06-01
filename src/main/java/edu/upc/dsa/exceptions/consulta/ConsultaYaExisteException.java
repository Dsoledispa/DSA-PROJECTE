package edu.upc.dsa.exceptions.consulta;

public class ConsultaYaExisteException extends RuntimeException {
    public ConsultaYaExisteException(String message) {
        super(message);
    }
}
