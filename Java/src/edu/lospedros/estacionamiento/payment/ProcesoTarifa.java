package edu.lospedros.estacionamiento.payment;

import edu.lospedros.estacionamiento.data.Ticket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class ProcesoTarifa {
    public static class FareStrategy implements TarifaProceso {

        private final Map<Vehiculosize, BigDecimal> TarifaHoraria;
        private final Map<Vehiculosize, BigDecimal> CargoMinimo;

        public <Vehiculosize> FareStrategy(Map<Vehiculosize, BigDecimal> hourlyRates,
                                           Map<Vehiculosize, BigDecimal> CargoMinimo) {
            this.TarifaHoraria = new EnumMap<>(Objects.requireNonNull(hourlyRates, "TarifaHoraria"));
            this.TarifaHoraria.putAll(hourlyRates);
            this.CargoMinimo = new EnumMap<>(Objects.requireNonNull(CargoMinimo, "CargoMinimo"));
            this.CargoMinimo.putAll(CargoMinimo);
        }

        /** motos=20, promedio=50, grandes=30 */
        public static FareStrategy ConTarifasBase() {
            Map<Vehiculosize, BigDecimal> hourly = new EnumMap<>(Vehiculosize.class);
            hourly.put(Vehiculosize.PEQUENIO, new BigDecimal("20"));
            hourly.put(Vehiculosize.MEDIANO, new BigDecimal("50"));
            hourly.put(Vehiculosize.LARGO, new BigDecimal("30"));

            Map<Vehiculosize, BigDecimal> min = new EnumMap<>(Vehiculosize.class);
            min.put(Vehiculosize.PEQUENIO, new BigDecimal("20"));
            min.put(Vehiculosize.MEDIANO, new BigDecimal("50"));
            min.put(Vehiculosize.LARGO, new BigDecimal("30"));

            return new FareStrategy(hourly, min);
        }

        @Override
        public BigDecimal CalculaTarifa(Ticket ticket, BigDecimal Tarifa) {
            Objects.requireNonNull(ticket, "ticket");
            Objects.requireNonNull(Tarifa, "Tarifa");

            Vehiculo Vehiculo = Objects.requireNonNull(ticket.getVehiculo(), "ticket.Vehiculo");
            Vehiculosize size = Objects.requireNonNull(Vehiculo.getSize(), "Vehiculo.size");

            BigDecimal TarifaHoraria = this.TarifaHoraria.get(size);
            if (TarifaHoraria == null) throw new IllegalStateException("No TarifaHoraria configured for size: " + size);

            Duration d = ticket.calculateParkingDuration();
            long minutes = d.toMinutes();

            // Siempre mínimo 1 hora; fracciones redondean hacia arriba
            long billableHours = Math.max(1, (minutes + 59) / 60);

            BigDecimal base = TarifaHoraria.multiply(BigDecimal.valueOf(billableHours));

            BigDecimal min = CargoMinimo.getOrDefault(size, BigDecimal.ZERO);
            if (base.compareTo(min) < 0) base = min;

            return Tarifa.add(base).setScale(2, RoundingMode.HALF_UP);
        }
    }
}
