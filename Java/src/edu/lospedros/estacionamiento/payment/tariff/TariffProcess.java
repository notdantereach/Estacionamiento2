package edu.lospedros.estacionamiento.payment.tariff;

import edu.lospedros.estacionamiento.data.Ticket;

import java.math.BigDecimal;

public interface TariffProcess {
    BigDecimal calculateFare(Ticket ticket, BigDecimal inputFare);
}