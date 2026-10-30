package mx.edu.utez.biblioteca.exception.customException;

public class CustomNotFoundException extends RuntimeException {
    public CustomNotFoundException(String mensaje) {
        super(mensaje);
    }
}
