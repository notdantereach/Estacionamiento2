package edu.lospedros.estacionamiento.payment;

import java.math.BigDecimal;
import java.util.List;

public class FareCalculator {
    private final edu.lospedros.estacionamiento.payment.fare.FareCalculator impl;

    public FareCalculator(List<edu.lospedros.estacionamiento.payment.fare.FareStrategy> strategies) {
        this.impl = new edu.lospedros.estacionamiento.payment.fare.FareCalculator(strategies);
    }

    public BigDecimal calculateFare(edu.lospedros.estacionamiento.data.Ticket ticket) {
        return impl.calculateFare(ticket);
    }

    public java.util.List<edu.lospedros.estacionamiento.payment.fare.FareStrategy> getStrategies() {
        return impl.getStrategies();
    }
}