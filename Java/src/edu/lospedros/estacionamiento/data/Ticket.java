package edu.lospedros.estacionamiento.data;

import java.time.Duration;
import java.time.LocalDateTime;

public class Ticket {
    private String id;
    private Vehicle vehicle;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public Ticket(String id, Vehicle vehicle, LocalDateTime entryTime) {
        this.id = id;
        this.vehicle = vehicle;
        this.entryTime = entryTime;
    }

    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    public Vehicle getVehicle() { return vehicle; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }

    // Devuelve la duración de estacionamiento; si exitTime es null, usa el tiempo actual.
    public Duration calculateParkingDuration() {
        LocalDateTime end = (exitTime != null) ? exitTime : LocalDateTime.now();
        return Duration.between(entryTime, end);
    }
}