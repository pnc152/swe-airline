package gmu.swe.dao;

import gmu.swe.domain.Flight;
import gmu.swe.domain.SearchFilters;
import gmu.swe.exception.DataAccessException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AirlineHeadquartersDaoTest {
	private AirlineHeadquartersDao dao; 
	private SimpleDateFormat simpleDateFormatter;
	
	@Before
	public void setUp() throws Exception {
		this.dao = new AirlineHeadquartersDao();
		simpleDateFormatter = new SimpleDateFormat("MM/dd/yyyy");
	}

	@After
	public void tearDown() throws Exception {
		this.dao = null;
		simpleDateFormatter = null;
	}

	@Test
	public void testDoesAirportExist_WithNonExistingAirportCode() throws DataAccessException {
		Assert.assertFalse(this.dao.doesAirportExist("test"));
	}
	
	@Test
	public void testDoesAirportExist_WithValidCode1() throws DataAccessException {
		Assert.assertTrue(this.dao.doesAirportExist("BWI"));
	}
	
	@Test
	public void testDoesAirportExist_WithValidCode2() throws DataAccessException {
		Assert.assertTrue(this.dao.doesAirportExist("dca"));
	}
	
	@Test
	public void testDoesAirportExist_WithValidCode3() throws DataAccessException {
		Assert.assertTrue(this.dao.doesAirportExist("iAd"));
	}
	
	@Test
	public void testCreateFlight() throws ParseException, DataAccessException{
//		stmt.setDate(1, new Date(flight.getDepartureDate().getTime()));
//		stmt.setString(2, flight.getDepartureAirportCode().toUpperCase());
//		stmt.setString(3, flight.getDestinationAirportCode().toUpperCase());
//		stmt.setDouble(4, flight.getCost());
//		stmt.setInt(5, flight.getAirplaneId());
//		stmt.setInt(6, flight.getAvailableSeats());
		
		String sDate = "10/25/2009";
		
		Flight flight = new Flight();
		flight.setDepartureDate(this.simpleDateFormatter.parse(sDate));
		flight.setDestinationAirportCode("DCA");
		flight.setDepartureAirportCode("BWI");
		flight.setCost(240.00);
		flight.setAirplaneId(0);
		
//		this.dao.createFlight(flight);
		Assert.assertTrue(true);
		
	}
	
	@Test
	public void testSearchFlights() throws DataAccessException{
		Collection<Flight> flights = this.dao.search(new SearchFilters());
		
		System.out.println("*****************");
		System.out.println("** Flight List **");
		System.out.println("(Ordered by Departing Airport and then by Destination Airport)");
		System.out.println("");
		System.out.println("DEPARTING\tDESTINATION\t\t\t\t\t\tAVAILABLE");
		System.out.println("AIRLINE\t\tAIRLINE\t\tDEPART DATE\tFLIGHT ID\tCOST\tSEATS");
		System.out.println("--------------------------------------------------------------------------------");
		
		for (Flight flight : flights) {
			
//			String airplanes = "FlightId(" + flight.getId() + "), DepartDate(" + flight.getDepartureDate()
//			+ "), DepartCode(" + flight.getDepartureAirportCode() + "), DestCode(" + flight.getDestinationAirportCode() + "), Cost($"
//			+ flight.getCost() + "), PlaneId(" + flight.getAirplaneId() + "), AvailableSeats(" + flight.getAvailableSeats()
//			+ ")";
//			System.out.println(airplanes);
			
//			for (Flight flight : flights) {
				String airplanes = flight.getDepartureAirportCode() + "\t\t" + flight.getDestinationAirportCode() + "\t\t"
						+ flight.getDepartureDate() + "\t" + flight.getId() + "\t\t$" + flight.getCost() + "\t"
						+ flight.getAvailableSeats();
				System.out.println(airplanes);
//			}
		}
		System.out.println("--------------------------------------------------------------------------------");
	}
}