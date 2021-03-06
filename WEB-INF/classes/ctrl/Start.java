package ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Mortgage;

/**
 * Servlet implementation class Start
 */
@WebServlet(urlPatterns = {"/Start"})
public class Start extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Start() {
		super();
	}
	
	public void init() throws ServletException {
		super.init();
		try {
			Mortgage model = new Mortgage();
			this.getServletContext().setAttribute("model", model);
		} catch (Exception e) {
			throw new ServletException();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/plain");
		HttpSession s = request.getSession();
		Mortgage m = (Mortgage) this.getServletContext().getAttribute("model");
		String jsp, r, p, a;
		
		// get all the banks and display them
		try {
			List<String> allBanks = m.getBanks();
			request.setAttribute("banks", allBanks);
		} catch (Exception e) {
			throw new IOException("No offers available from this bank");
		}

		// if it's a fresh visit, show the UI
		if (request.getParameter("doit") == null || request.getParameter("restart") != null) {
			jsp = "UI.jspx";
		} 
		else {
			jsp = "Result.jspx";
			p = request.getParameter("principle");
			a = request.getParameter("amortization");
			r = request.getParameter("interest");
			
			// get values from session, if you are recomputing
			if (p == null) {
				p = (String) s.getAttribute("principle");
			}
			if (a == null) {
				a = (String) s.getAttribute("amortization");	
			}
			
			// set the request attributes
			request.setAttribute("principle", p);
			request.setAttribute("amortization", a);
			request.setAttribute("interest", r);
			
			// set the session attributes
			s.setAttribute("principle", p);
			s.setAttribute("amortization", a);
			
			try {
				
				// stupid hack to re-validate principle when using listeners
		    	boolean isValid = false;
		    	m.validatePrinciple(p);
		    	isValid = true;
 	
				// if there is no interest parameter, use the selected bank
				if (r == "" && isValid) {
					String bank = request.getParameter("bank").toString();
					double principle = Double.parseDouble(p);
					int amort = Integer.parseInt(a);
					double rate = m.getRate(principle, amort, bank);	
					request.setAttribute("rate", rate);
					request.setAttribute("bankName", bank);
					request.setAttribute("monthly", String.format("%.2f", m.computePayment(p, a, String.valueOf(rate))));	
				} 
				// if there is an interest, compute the payment normally
				else {
					request.setAttribute("interest", m.validateInterest(r));
					request.setAttribute("monthly", String.format("%.2f", m.computePayment(p, a, r)));
				}
			} catch (Exception e) {
				// display the correct error message
				request.setAttribute("error", e.getMessage());
				jsp = "UI.jspx";
			}
		}
		this.getServletContext().getRequestDispatcher("/" + jsp).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		this.doGet(request, response);
	}

}
