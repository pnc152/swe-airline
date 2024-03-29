/*
 * Created by: Matt Snyder
 */
package msnydera.swe645.service.impl;

import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.persistence.EntityManager;

import msnydera.swe645.dao.AirlineHeadquartersJpaDao;
import msnydera.swe645.domain.Airplane;
import msnydera.swe645.domain.Customer;
import msnydera.swe645.domain.Flight;
import msnydera.swe645.domain.Reservation;
import msnydera.swe645.domain.SearchFilters;
import msnydera.swe645.exception.DataAccessException;
import msnydera.swe645.exception.ValidationException;
import msnydera.swe645.service.AirlineHeadquartersService;
import msnydera.swe645.util.DateUtil;

/**
 * Implementation class for the AirlineHeadquartersService. This class contains
 * the business logic, including validation, and connects to the DAO for basic
 * CRUD (create, retrieve, update, & delete) operations.
 * 
 */
public class AirlineHeadquartersServiceImpl implements AirlineHeadquartersService {
	private AirlineHeadquartersJpaDao dao;

	private EntityManager entityManager;

	public AirlineHeadquartersServiceImpl() {

	}

	/**
	 * Constructor to set the EntityManager to use in the DAO.
	 * 
	 * @param entityManager
	 *            EntityManager the DAO should use.
	 */
	public AirlineHeadquartersServiceImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gmu.swe.service.impl.AirlineHeadquartersService#getAllAirplanes()
	 */
	public Collection<Airplane> getAllAirplanes() throws DataAccessException {
		return this.getDao().getAllAirplanes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gmu.swe.service.impl.AirlineHeadquartersService#getallAirports()
	 */
	public Collection<String> getAllAirports() throws DataAccessException {
		return this.getDao().getAllAirports();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gmu.swe.service.AirlineHeadquartersService#getAllFlights()
	 */
	public Collection<Flight> getAllFlights() throws DataAccessException {
		return this.getDao().getAllFlights();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msnydera.swe645.service.AirlineHeadquartersService#getAllCustomers()
	 */
	public Collection<Customer> getAllCustomers() throws DataAccessException {
		return this.getDao().getAllCustomers();
	}

	/*
	 * (non-Javadoc)
	 * @see msnydera.swe645.service.AirlineHeadquartersService#getAllReservations()
	 */
	public Collection<Reservation> getAllReservations() throws DataAccessException {
		return this.getDao().getAllReservations();
	}
	
	/**
	 * @see gmu.swe.service.impl.AirlineHeadquartersService#search(msnydera.swe645.domain.SearchFilters)
	 * 
	 *      Fails validation if searchFilters is null or if all of the values in
	 *      searchFilters are null.
	 */
	public Collection<Flight> search(SearchFilters searchFilters) throws ValidationException, DataAccessException {
		validateSearchCriteria(searchFilters);

		return this.getDao().search(searchFilters);
	}

	/**
	 * @see gmu.swe.service.impl.AirlineHeadquartersService#createAirplane(int,
	 *      java.lang.String) <br>
	 * <br>
	 *      Fails validation if the numberOfSeats < 1, or if the airplaneType is
	 *      null or empty String "".
	 */
	public void createAirplane(int numberOfSeats, String airplaneType) throws ValidationException, DataAccessException {
		validateAirplane(numberOfSeats, airplaneType);

		this.getDao().createAirplane(numberOfSeats, airplaneType);
	}

	/**
	 * @see gmu.swe.service.impl.AirlineHeadquartersService#createAirport(java.lang
	 *      .String)<br>
	 * <br>
	 *      Fails validation if the airportCode is null, empty, or doesn't exist
	 *      in the system.
	 */
	public void createAirport(String airportCode) throws ValidationException, DataAccessException {
		validateAirport(airportCode);

		this.getDao().createAirport(airportCode);
	}

	/**
	 * @see gmu.swe.service.impl.AirlineHeadquartersService#createCustomer(Customer)<br>
	 * <br>
	 *      Fails validation if the customers name, address, and/or phone are
	 *      empty or null. Also fails if the provided customer has an ID set
	 *      that already exists in the system.
	 * 
	 */
	public Customer createCustomer(Customer customer) throws ValidationException, DataAccessException {
		validateCustomer(customer);

		return this.getDao().createCustomer(customer);
	}

	/**
	 * @see gmu.swe.service.impl.AirlineHeadquartersService#createFlight(gmu.swe.
	 *      domain.Flight)<br>
	 * <br>
	 *      Fails validation if:<br>
	 *      - The flight is null<br>
	 *      - The departure date is null or it is earlier than today<br>
	 *      - The departure airport code is null or doesn't exist in the system<br>
	 *      - The destination airport code is null or doesn't exist in the
	 *      system<br>
	 *      - The departure & destination airport codes are the same value<br>
	 *      - The cost of the flight !> 0<br>
	 *      - The airplaneId is not valid (i.e. not >= 0 or doesn't exist in the
	 *      system.)
	 */
	public Flight createFlight(Flight flight) throws ValidationException, DataAccessException {
		validateFlight(flight);

		return this.getDao().createFlight(flight);
	}

	/**
	 * @see gmu.swe.service.impl.AirlineHeadquartersService#createReservation(int,
	 *      int, int)<br>
	 * <br>
	 *      Fails validation if the provided flightId < 0 or does not exist in
	 *      the system. Also fails if the numSeats < 1 or if the flight doesn't
	 *      have enough available seats.
	 */
	public Reservation createReservation(int flightId, int customerId, int numSeats) throws ValidationException,
			DataAccessException {
		validateReservationData(flightId, customerId, numSeats);

		return this.getDao().createReservation(flightId, customerId, numSeats);
	}

	/**
	 * @see gmu.swe.service.impl.AirlineHeadquartersService#createReservation(int)<br>
	 * <br>
	 *      Fails validation if the provided reservationId < 0, if it doesn't
	 *      exist in the system, or if it is already canceled.
	 */
	public Reservation cancelReservation(int reservationId) throws ValidationException, DataAccessException {
		validateCancelReservationId(reservationId);

		return this.getDao().cancelReservation(reservationId);
	}

	/**
	 * @see gmu.swe.service.impl.AirlineHeadquartersService#getReservation(int)<br>
	 * <br>
	 *      Fails validation if the provided reservationId < 0 or if it doesn't
	 *      exist in the system.
	 */
	public Reservation getReservation(int reservationId) throws ValidationException, DataAccessException{
		validateReservationId(reservationId);
		
		return this.getDao().getReservation(reservationId);
	}
	
	/**
	 * Fails validation if the provided reservationId < 0, if it doesn't exist
	 * in the system, or if it is already canceled.
	 * 
	 * @param reservationId
	 *            Field to validate
	 * @throws ValidationException
	 *             Thrown if there are validation errors
	 * @throws DataAccessException
	 *             Thrown if there is an error when looking up values in the
	 *             system.
	 */
	private void validateCancelReservationId(int reservationId) throws ValidationException, DataAccessException {
		ValidationException validationException = new ValidationException();

		if (reservationId < 0) {
			validationException.addErrorMessage("An invalid reservation Id was provided, it must be >= 0");
		} else if (!this.getDao().doesReservationExist(reservationId)) {
			validationException.addErrorMessage("The provided reservation Id does not exist");
		}

		Reservation reservation = this.getDao().getReservation(reservationId);
		if (reservation.getStatus().equalsIgnoreCase("CANCELED")) {
			validationException.addErrorMessage("The reservation has already been canceled");
		}

		if (validationException.hasErrors()) {
			throw validationException;
		}
	}
	
	/**
	 * Fails validation if the provided reservationId < 0 or if it doesn't exist
	 * in the system.
	 * 
	 * @param reservationId
	 *            Field to validate
	 * @throws ValidationException
	 *             Thrown if there are validation errors
	 * @throws DataAccessException
	 *             Thrown if there is an error when looking up values in the
	 *             system.
	 */
	private void validateReservationId(int reservationId) throws ValidationException, DataAccessException {
		ValidationException validationException = new ValidationException();

		if (reservationId < 0) {
			validationException.addErrorMessage("An invalid reservation Id was provided, it must be >= 0");
		} else if (!this.getDao().doesReservationExist(reservationId)) {
			validationException.addErrorMessage("The provided reservation Id does not exist");
		}

		if (validationException.hasErrors()) {
			throw validationException;
		}
	}

	/**
	 * Fails validation if the provided flightId < 0 or does not exist in the
	 * system. Fails if the provided customerId < 0 or does not exist in the
	 * system. Also fails if the numSeats < 1 or if the flight doesn't have
	 * enough available seats.
	 * 
	 * @param flightId
	 *            Field to validate
	 * @param customerId
	 *            Field to validate
	 * @param numSeats
	 *            Field to validate
	 * @throws ValidationException
	 *             Thrown if there are validation errors
	 * @throws DataAccessException
	 *             Thrown if there is an error when looking up values in the
	 *             system.
	 */
	private void validateReservationData(int flightId, int customerId, int numSeats) throws ValidationException,
			DataAccessException {
		ValidationException validationException = new ValidationException();

		if (flightId < 0) {
			validationException.addErrorMessage("An invalid flight Id was provided, it must be >= 0");
		} else if (!this.getDao().doesFlightExist(flightId)) {
			validationException.addErrorMessage("The provided flight Id does not exist");
		}
		if (customerId < 0) {
			validationException.addErrorMessage("An invalid Customer Id was provided, it must be >= 0");
		} else if (!this.getDao().doesCustomerExist(customerId)) {
			validationException.addErrorMessage("The provided Customer Id does not exist");
		}
		if (numSeats < 1) {
			validationException.addErrorMessage("An invalid number of seats was provided, it must be >= 1");
		} else {
			int numAvailableSeats = this.getDao().getNumberOfAvailableSeats(flightId);
			if (numAvailableSeats < numSeats) {
				validationException.addErrorMessage("The flight does not have enough seats, it only has "
						+ numAvailableSeats + " seats available");
			}
		}

		if (validationException.hasErrors()) {
			throw validationException;
		}
	}

	/**
	 * Fails validation if the provided customer is null, or if the name,
	 * address, or phone are empty or null. Also fails if the provided customer
	 * has an ID set that already exists in the system.
	 * 
	 * @param customer
	 *            Customer to validate
	 * @throws ValidationException
	 *             Thrown if there are validation errors.
	 */
	private void validateCustomer(Customer customer) throws ValidationException {
		ValidationException validationException = new ValidationException();

		if (customer == null) {
			validationException.addErrorMessage("The provided Customer object was null.");
		} else {
			if (customer.getName() == null || customer.getName().trim().equals("")) {
				validationException.addErrorMessage("A name must be provided for the customer");
			}
			if (customer.getAddress() == null || customer.getAddress().trim().equals("")) {
				validationException.addErrorMessage("An address must be provided for the customer");
			}
			if (customer.getPhone() == null || customer.getPhone().trim().equals("")) {
				validationException.addErrorMessage("A phone number must be provided for the customer");
			}
		}

		if (validationException.hasErrors()) {
			throw validationException;
		}
	}

	/**
	 * Fails validation if searchFilters is null or if all of the values in
	 * searchFilters are null.
	 * 
	 * @param searchFilters
	 *            Filters to validate
	 * @throws ValidationException
	 *             Thrown if validation fails.
	 */
	private void validateSearchCriteria(SearchFilters searchFilters) throws ValidationException {
		ValidationException validationException = new ValidationException();

		if (searchFilters == null || searchFilters.isAllEmpty()) {
			validationException.addErrorMessage("No search filters were not provided");
		}

		if (validationException.hasErrors()) {
			throw validationException;
		}
	}

	/**
	 * Fails validation if the numberOfSeats < 1, or if the airplaneType is null
	 * or empty String "".
	 * 
	 * @param numberOfSeats
	 *            field to validate
	 * @param airplaneType
	 *            field to validate
	 * @throws ValidationException
	 *             Thrown if there are validation errors.
	 */
	protected void validateAirplane(int numberOfSeats, String airplaneType) throws ValidationException {
		ValidationException validationException = new ValidationException();

		if (numberOfSeats < 1) {
			validationException.addErrorMessage("The number of seats on a plane may not be < 1");
		}
		if (airplaneType == null || airplaneType.trim().equals("")) {
			validationException.addErrorMessage("The airplane type was not provided");
		}

		if (validationException.hasErrors()) {
			throw validationException;
		}
	}

	/**
	 * Fails validation if the airportCode is null, empty, or doesn't exist in
	 * the system.
	 * 
	 * @param airportCode
	 *            Fied to validate
	 * @throws ValidationException
	 *             Thrown if validation fails
	 * @throws DataAccessException
	 *             Thrown if there is a problem with looking up the airport
	 *             code.
	 */
	protected void validateAirport(String airportCode) throws ValidationException, DataAccessException {
		ValidationException validationException = new ValidationException();

		if (airportCode == null || airportCode.trim().equals("")) {
			validationException.addErrorMessage("The airport code was not provided");
		} else if (this.getDao().doesAirportExist(airportCode)) {
			validationException.addErrorMessage("The airport code provided already exists");
		}

		if (validationException.hasErrors()) {
			throw validationException;
		}
	}

	/**
	 * Fails validation if:<br>
	 * - The flight is null<br>
	 * - The departure date is null or it is earlier than today<br>
	 * - The departure airport code is null or doesn't exist in the system<br>
	 * - The destination airport code is null or doesn't exist in the system<br>
	 * - The departure & destination airport codes are the same value<br>
	 * - The cost of the flight !> 0<br>
	 * - The airplaneId is not valid (i.e. not >= 0 or doesn't exist in the
	 * system.)
	 * 
	 * @param flight
	 *            Field to validate
	 * @throws ValidationException
	 *             Thrown if there are validation errors
	 * @throws DataAccessException
	 *             Thrown if there is a problem with looking up information in
	 *             the system.
	 */
	protected void validateFlight(Flight flight) throws ValidationException, DataAccessException {
		ValidationException validationException = new ValidationException();

		if (flight == null) {
			validationException.addErrorMessage("No Flight information was provided");
		} else {
			if (flight.getDepartureDate() == null) {
				validationException.addErrorMessage("No departure date was provided");
			} else if (!DateUtil.isTodayOrLater(flight.getDepartureDate())) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				validationException
						.addErrorMessage("The departure date must be no earlier than tomorrow.  The provided date was "
								+ sdf.format(flight.getDepartureDate()) + ".");
			}

			if (flight.getDepartureAirport() == null) {
				validationException.addErrorMessage("No departing airport code was provided");
			} else if (!this.getDao().doesAirportExist(flight.getDepartureAirport().getAirportCode())) {
				validationException.addErrorMessage("The provided departing airport code does not exist");
			}

			if (flight.getDestinationAirport() == null) {
				validationException.addErrorMessage("No destination airport code was provided");
			} else if (!this.getDao().doesAirportExist(flight.getDestinationAirport().getAirportCode())) {
				validationException.addErrorMessage("The provided destination airport code does not exist");
			}

			if (flight.getDepartureAirport() != null
					&& flight.getDestinationAirport() != null
					&& flight.getDepartureAirport().getAirportCode().equalsIgnoreCase(
							flight.getDestinationAirport().getAirportCode())) {
				validationException.addErrorMessage("The destination and departure codes may not be the same");
			}

			if (flight.getCost() < 0.0) {
				validationException.addErrorMessage("The flight cost must be >= $0");
			}

			if (flight.getAirplane().getId() < 0) {
				validationException.addErrorMessage("The provided airplane Id is invalid.  The Id must be > 0");
			} else if (!this.getDao().doesAirplaneExist(flight.getAirplane().getId())) {
				validationException.addErrorMessage("The provided airplane Id does not exist.");
			}
		}

		if (validationException.hasErrors()) {
			throw validationException;
		}
	}

	/**
	 * This method is used to get the correct DAO implementation. This method
	 * makes this class loosely coupled in that someone could set a different
	 * implementation of a DAO by calling the setDao() method. If no DAO is
	 * explicitly set, then this method will instantiate a known implementation.
	 * 
	 * @return DAO to use.
	 */
	public AirlineHeadquartersJpaDao getDao() {
		// if (this.dao == null) {
		this.dao = new AirlineHeadquartersJpaDao(this.entityManager);
		// }
		return this.dao;
	}

	/**
	 * Used to set an implementation of a DAO.
	 * 
	 * @param dao
	 *            DAO to set.
	 */
	public void setDao(AirlineHeadquartersJpaDao dao) {
		this.dao = dao;
	}
}
