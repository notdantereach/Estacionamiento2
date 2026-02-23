package edu.lospedros.estacionamiento.payment.fare;

import edu.lospedros.estacionamiento.process.Ticket;

import java.math.BigDecimal;

public interface FareStrategy {
    BigDecimal calculateFare(Ticket ticket, BigDecimal inputFare);
}