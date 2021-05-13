package dds.monedero.exceptions;

public class MaximaCantidadDepositosException extends RuntimeException {

  public MaximaCantidadDepositosException(double depositoMaximo) {
    super("Ya excedio los " + depositoMaximo + " depositos diarios");
  }

}