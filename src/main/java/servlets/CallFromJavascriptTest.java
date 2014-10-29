package servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import refinery.RefineryBean;
import termo.component.Compound;
import termo.matter.Homogeneous;
import chimicae.AvailableCompounds;
import chimicae.HomogeneousBean;
import chimicae.SaturationBean;

/**
 * Servlet implementation class CallFromJavascriptTest
 */
@WebServlet("/CallFromJavascriptTest")
public class CallFromJavascriptTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Inject AvailableCompounds availableCompounds;
	@Inject HomogeneousBean homogeneousBean;
	@Inject RefineryBean refineryBean;
	@Inject SaturationBean saturationBean;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CallFromJavascriptTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request,response);
	}
	
	protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String test = (String)request.getParameter("test");
		
		
		if(test.equals("heterogeneousList")){
			System.out.println(test);
			StringBuilder sb = new StringBuilder("{[");
			//Iterator<Compound> it = availableCompounds.getCompounds().iterator();
			Iterator<Homogeneous> it = homogeneousBean.getHomogeneousList().iterator();
			while(it.hasNext()){
				Homogeneous com = it.next();
				sb.append("'"+com.toString()+"'");
				if(it.hasNext()){
					sb.append(",");
				}
			}
			sb.append("]}");
			response.getWriter().append(sb.toString());
		}else if(test.equals("createTank")){
			int selected = Integer.valueOf(request.getParameter("selected"));
			saturationBean.setSelectedHeterogeneous(saturationBean.getHeterogeneousList().get(selected));
			Double temperature = Double.valueOf(request.getParameter("temperature"));
			Double pressure = Double.valueOf(request.getParameter("pressure"));
			String report= refineryBean.createTank(temperature,pressure);
			System.out.println(report);
			response.getWriter().append(report);
					
		}
		
	}

}
