package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
//import jdk.vm.ci.meta.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo;
  private List<Movimiento> movimientos = new ArrayList<>();
  private double depositoMaximo = 3;
  private double limiteExtraccion = 1000;

  public double getLimiteExtraccion() {
    return limiteExtraccion;
  }

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public double getDepositoMaximo() {
    return depositoMaximo;
  }

  public void poner(double monto) {
    if (monto <= 0) {
      throw new MontoNegativoException(monto);
    }
    //Agrego el chequeo del día
    if (getMovimientos().stream().filter(movimiento -> movimiento.fueDepositado(LocalDate.now())).count() >= getDepositoMaximo()) {
      throw new MaximaCantidadDepositosException(getDepositoMaximo());
    }
    agregarMovimiento(LocalDate.now(), monto, true);
  }

  public void sacar(double monto) {
    if (monto <= 0) {
      throw new MontoNegativoException(monto);
    }
    if (getSaldo() - monto < 0) {
      throw new SaldoMenorException(getSaldo());
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = getLimiteExtraccion() - montoExtraidoHoy;
    if (monto > limite) {
      throw new MaximoExtraccionDiarioException(getLimiteExtraccion(), limite);
    }
    agregarMovimiento(LocalDate.now(), monto, true);
  }

  public void agregarMovimiento(LocalDate fecha, double monto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, monto, esDeposito);
    modificarSaldo(movimiento.calcularValor());
    movimientos.add(movimiento);
  }

  public void modificarSaldo(double monto) {
    this.saldo += monto;
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> movimiento.fueExtraido(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
