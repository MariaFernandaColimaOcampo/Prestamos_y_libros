package mx.edu.utez.biblioteca.exception.customException;

public class CustomBadRequestException extends RuntimeException {
    public CustomBadRequestException(String mensaje) {
        super(mensaje);
    }
}
