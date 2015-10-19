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
	public Start()
	{
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
			HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/plain");
		HttpSession s = request.getSession();
		Mortgage m = (Mortgage) this.getServletContext().getAttribute("model");
		String jsp, r, p, a;
		List<String> allBanks = m.getBanks();

		if (request.getParameter("doit") == null || request.getParameter("restart") != null){
			request.setAttribute("banks", allBanks);
			jsp = "UI.jspx";
		} else {
			
			p = request.getParameter("principle");
			a = request.getParameter("amortization");
			
			// get values from session, if you are recomputing
			if (p == null) {
				p = (String) s.getAttribute("principle");
			}
			if (a == null) {
				a = (String) s.getAttribute("amortization");	
			}
			
			// always get the interest from the request parameter
			r = request.getParameter("interest");
			
			// set the request attributes
			request.setAttribute("principle", p);
			request.setAttribute("amortization", a);
			request.setAttribute("interest", r);
			
			// set the session attributes
			s.setAttribute("principle", p);
			s.setAttribute("amortization", a);
			
			try {
				request.setAttribute("interest", m.validateInterest(r));
				request.setAttribute("monthly", String.format("%.2f", m.computePayment(p, a, r)));
				jsp = "Result.jspx";
			} catch (Throwable e) {
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
			HttpServletResponse response) throws ServletException, IOException
	{
		this.doGet(request, response);
	}

}
