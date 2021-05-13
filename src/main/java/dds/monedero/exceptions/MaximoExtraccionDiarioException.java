package dds.monedero.exceptions;

public class MaximoExtraccionDiarioException extends RuntimeException {
  public MaximoExtraccionDiarioException(double limiteExtraccion, double limite) {
    super("No puede extraer mas de $ " + limiteExtraccion + " diarios, límite: " + limite);
  }
}