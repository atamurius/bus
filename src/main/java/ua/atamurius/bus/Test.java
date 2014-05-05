package ua.atamurius.bus;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.atamurius.bus.data.GpsTrackerApi;

/**
 * Servlet implementation class Test
 */
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println("Server time is "+ new Date());
		String id = "17";
		GpsTrackerApi api = new GpsTrackerApi();
		out.println("\nPath: "+ api.getRoute(id));
		out.println("\nStops: "+ api.getStops(id));
		out.println("\nCars: "+ api.getCars(id));
	}

}
