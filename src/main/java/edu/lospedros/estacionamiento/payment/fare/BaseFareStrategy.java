package edu.lospedros.estacionamiento.payment.fare;

import edu.lospedros.estacionamiento.data.Vehicle;
import edu.lospedros.estacionamiento.data.VehicleSize;
import edu.lospedros.estacionamiento.process.Ticket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class BaseFareStrategy implements FareStrategy {

    private final Map<VehicleSize, BigDecimal> hourlyRates;
    private final Map<VehicleSize, BigDecimal> minimumCharges;

    public BaseFareStrategy(Map<VehicleSize, BigDecimal> hourlyRates,
                            Map<VehicleSize, BigDecimal> minimumCharges) {
        this.hourlyRates = new EnumMap<>(Objects.requireNonNull(hourlyRates, "hourlyRates"));
        this.hourlyRates.putAll(hourlyRates);
        this.minimumCharges = new EnumMap<>(Objects.requireNonNull(minimumCharges, "minimumCharges"));
        this.minimumCharges.putAll(minimumCharges);
    }

    /** Configuración por defecto (tus valores): motos=20, promedio=50, grandes=30 */
    public static BaseFareStrategy withDefaultRates() {
        Map<VehicleSize, BigDecimal> hourly = new EnumMap<>(VehicleSize.class);
        hourly.put(VehicleSize.SMALL, new BigDecimal("20"));
        hourly.put(VehicleSize.MEDIUM, new BigDecimal("50"));
        hourly.put(VehicleSize.LARGE, new BigDecimal("30"));

        Map<VehicleSize, BigDecimal> min = new EnumMap<>(VehicleSize.class);
        min.put(VehicleSize.SMALL, new BigDecimal("20"));
        min.put(VehicleSize.MEDIUM, new BigDecimal("50"));
        min.put(VehicleSize.LARGE, new BigDecimal("30"));

        return new BaseFareStrategy(hourly, min);
    }

    @Override
    public BigDecimal calculateFare(Ticket ticket, BigDecimal inputFare) {
        Objects.requireNonNull(ticket, "ticket");
        Objects.requireNonNull(inputFare, "inputFare");

        Vehicle vehicle = Objects.requireNonNull(ticket.getVehicle(), "ticket.vehicle");
        VehicleSize size = Objects.requireNonNull(vehicle.getSize(), "vehicle.size");

        BigDecimal hourlyRate = hourlyRates.get(size);
        if (hourlyRate == null) throw new IllegalStateException("No hourlyRate configured for size: " + size);

        Duration d = ticket.calculateParkingDuration();
        long minutes = d.toMinutes();

        // Siempre mínimo 1 hora; fracciones redondean hacia arriba
        long billableHours = Math.max(1, (minutes + 59) / 60);

        BigDecimal base = hourlyRate.multiply(BigDecimal.valueOf(billableHours));

        BigDecimal min = minimumCharges.getOrDefault(size, BigDecimal.ZERO);
        if (base.compareTo(min) < 0) base = min;

        return inputFare.add(base).setScale(2, RoundingMode.HALF_UP);
    }
}