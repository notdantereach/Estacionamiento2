package edu.lospedros.estacionamiento.payment;

import edu.lospedros.estacionamiento.payment.fare.BaseFareStrategy;
import edu.lospedros.estacionamiento.payment.fare.FareCalculator;
import edu.lospedros.estacionamiento.process.Ticket;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

/**
 * Servicio simple que envuelve un {@link FareCalculator} y expone una API para calcular
 * la tarifa de un {@link Ticket}.
 *
 * <p>Construcción:
 * - PaymentService() crea internamente un {@link FareCalculator} configurado con
 *   {@link BaseFareStrategy#withDefaultRates()} como única estrategia.
 * - PaymentService(FareCalculator) permite inyección del calculador (útil para tests).
 *
 * <p>Este servicio únicamente delega el cálculo de la tarifa; no realiza logging,
 * persistencia ni cambia el lifecycle del ticket.
 */
public class PaymentService {

    private final FareCalculator fareCalculator;

    /**
     * Crea un PaymentService con un FareCalculator configurado con la estrategia
     * BaseFareStrategy.withDefaultRates() como única estrategia.
     */
    public PaymentService() {
        this(new FareCalculator(Arrays.asList(BaseFareStrategy.withDefaultRates())));
    }

    /**
     * Crea un PaymentService usando el FareCalculator proporcionado.
     *
     * @param fareCalculator el calculador de tarifas a usar (no nulo)
     * @throws NullPointerException si fareCalculator es null
     */
    public PaymentService(FareCalculator fareCalculator) {
        this.fareCalculator = Objects.requireNonNull(fareCalculator, "fareCalculator");
    }

    /**
     * Calcula la tarifa para el ticket dado delegando en {@link FareCalculator#calculateFare(Ticket)}.
     *
     * @param ticket el ticket para el que calcular la tarifa (no nulo)
     * @return la tarifa calculada como BigDecimal
     * @throws NullPointerException si ticket es null
     */
    public BigDecimal computeFareFor(Ticket ticket) {
        Objects.requireNonNull(ticket, "ticket");
        return fareCalculator.calculateFare(ticket);
    }
}