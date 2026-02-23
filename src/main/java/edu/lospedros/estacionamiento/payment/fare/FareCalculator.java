package edu.lospedros.estacionamiento.payment.fare;

import edu.lospedros.estacionamiento.process.Ticket;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FareCalculator {

    private final List<FareStrategy> strategies;

    public FareCalculator(List<FareStrategy> strategies) {
        Objects.requireNonNull(strategies, "strategies");
        this.strategies = new ArrayList<>(strategies);
    }

    public List<FareStrategy> getStrategies() {
        return Collections.unmodifiableList(strategies);
    }

    public BigDecimal calculateFare(Ticket ticket) {
        Objects.requireNonNull(ticket, "ticket");

        BigDecimal fare = BigDecimal.ZERO;
        for (FareStrategy strategy : strategies) {
            fare = strategy.calculateFare(ticket, fare);
        }
        return fare;
    }
}