package edu.lospedros.estacionamiento.payment;

import edu.lospedros.estacionamiento.process.Ticket;

import java.math.BigDecimal;

public interface TarifaProceso {
    BigDecimal CalculaTarifa(Ticket ticket, BigDecimal inputFare);
}