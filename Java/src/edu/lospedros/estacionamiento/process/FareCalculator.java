package edu.lospedros.estacionamiento.process;

import edu.lospedros.estacionamiento.data.Ticket;

public interface FareCalculator {
    double calculateFare(Ticket ticket);
}