/*
 * Created by: Matt Snyder
 */
package msnydera.swe645.web.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import msnydera.swe645.constant.Constants;
import msnydera.swe645.domain.Airplane;
import msnydera.swe645.exception.DataAccessException;
import msnydera.swe645.exception.ValidationException;
import msnydera.swe645.service.ejb.HeadquartersEjbRemote;
import msnydera.swe645.util.ResourceUtil;
import msnydera.swe645.util.StringUtils;

/**
 * Servlet handling the preparation for the AddAirplane page.
 */
public class PrepareAddAirplane extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PrepareAddAirplane() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response) This method will prepare the request with the existing
	 *      airplanes in the system (so the user can see what is currently
	 *      available).
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		RequestDispatcher dispatch = request.getRequestDispatcher("jsp/addAirplane.jsp");

		Collection<Airplane> airplanes;
		try {
			airplanes = getExistingAirplanes();

			request.setAttribute("airplanes", airplanes);

			request.setAttribute("addedAirplane", request.getAttribute("addedAirplane"));
			request.setAttribute("error", request.getAttribute("error"));

		} catch (ValidationException e) {
			String errorMessage = StringUtils.getFormattedMessages(e.getErrorMessages());
			request.setAttribute("error", errorMessage);
		} catch (Exception e) {
			dispatch = request.getRequestDispatcher("jsp/headquartersMenu.jsp");

			request.setAttribute("error", "An unkown error occured.");
		}

		dispatch.forward(request, response);
	}

	/**
	 * Gets all the airplanes from the system by communicating with the remote
	 * EJB.
	 * 
	 * @return Collection of Airplanes in the system.
	 * @throws ValidationException
	 *             Thrown if there is a problem with communicating with the
	 *             remote EJB.
	 * @throws ValidationException
	 *             Thrown to help with messages
	 * @throws Exception
	 *             Thrown if an error occurs with the connection to the DB with
	 *             the user.
	 */
	public Collection<Airplane> getExistingAirplanes() throws ValidationException, Exception {

		try {
			 HeadquartersEjbRemote ejbRef = (HeadquartersEjbRemote)
			 ResourceUtil.getInitialContext().lookup(Constants.EAR_FILE_NAME + "/HeadquartersEjb/remote");
//			HeadquartersEjbRemote ejbRef = (HeadquartersEjbRemote) ResourceUtil.getLoggedInContext(user).lookup(
//					Constants.EAR_FILE_NAME + "/HeadquartersEjb/remote");
			return ejbRef.getAllAirplanes();
		} catch (NamingException e) {
			e.printStackTrace();
			ValidationException ve = new ValidationException();
			ve.addErrorMessage("Server error occured during EJB lookup.");
			throw ve;
		} catch (DataAccessException e) {
			ValidationException ve = new ValidationException();
			ve.addErrorMessage("Server error occured while retrieving all the airplanes.");
			throw ve;
		}
	}
}
