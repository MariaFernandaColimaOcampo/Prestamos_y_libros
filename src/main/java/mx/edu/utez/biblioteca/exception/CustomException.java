package mx.edu.utez.biblioteca.exception;

public class CustomException extends RuntimeException {
    public CustomException(String mensaje) {
        super(mensaje);
    }
}