package gmu.swe.rmi;

import gmu.swe.domain.Flight;
import gmu.swe.domain.Reservation;
import gmu.swe.domain.SearchFilters;
import gmu.swe.exception.DataAccessException;
import gmu.swe.exception.ValidationException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface AirlineTicketReserver extends Remote {
	public Collection<Flight> search(SearchFilters searchFilters) throws ValidationException, DataAccessException, RemoteException;

	public Reservation reserveFlight(String flightNumber, int numberOfSeats) throws ValidationException, DataAccessException, RemoteException;

	public void createAirplane(int numberOfSeats, String airplaneType) throws ValidationException, DataAccessException, RemoteException;

	public void createAirport(String airportCode) throws ValidationException, DataAccessException, RemoteException;

	public int createFlight(Flight flight) throws ValidationException, DataAccessException, RemoteException;
}