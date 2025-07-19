package APISASA.API_sasa.Exceptions;

public class ExceptionClienteNoEncontrado extends RuntimeException {

  // Constructor con mensaje por defecto
  public ExceptionClienteNoEncontrado() {
    super("El cliente no fue encontrado en la base de datos.");
  }

  // Constructor con mensaje personalizado
  public ExceptionClienteNoEncontrado(String mensaje) {
    super(mensaje);
  }
}
