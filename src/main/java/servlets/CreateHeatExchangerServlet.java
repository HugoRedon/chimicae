package servlets;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import refinery.RefineryBean;

/**
 * Servlet implementation class CreateHeatExchangerServlet
 */
@WebServlet("/CreateHeatExchangerServlet")
public class CreateHeatExchangerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	@Inject RefineryBean refineryBean;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request,response);
	}

	
	protected void process(HttpServletRequest request, HttpServletResponse response){
		int id = Integer.valueOf(request.getParameter("fromequipmentid"));
		double newTemperature = Double.valueOf(request.getParameter("newTemperature"));
		refineryBean.createHeatExchanger(id,newTemperature);
		
		
	}
}
