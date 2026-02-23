package edu.lospedros.estacionamiento.payment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CalculadorTarifa {
    public static record FareCalculator(List<TarifaProceso> strategies) {

        public FareCalculator(List<TarifaProceso> strategies) {
            Objects.requireNonNull(strategies, "Estrategias");
            this.strategies = new ArrayList<>(strategies);
        }

        @Override
        public List<TarifaProceso> strategies() {
            return Collections.unmodifiableList(strategies);
        }

        public BigDecimal CalculaTarifa(Ticket ticket) {
            Objects.requireNonNull(ticket, "ticket");

            BigDecimal fare = BigDecimal.ZERO;
            for (TarifaProceso strategy : strategies) {
                fare = strategy.CalculaTarifa(ticket, fare);
            }
            return fare;
        }
    }
}
