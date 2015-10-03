package ctrl;

import java.io.IOException;

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
@WebServlet(urlPatterns = {"/Start", "/Start/branch"})
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
		Mortgage model = new Mortgage();
		this.getServletContext().setAttribute("model", model);
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
		String branch = request.getServletPath();
		boolean isBranch = false;
		
		// if you are visiting 'Start/branch', show a different UI
		request.setAttribute("branch", false);
		if (branch.equals("/Start/branch")) {
			request.setAttribute("branch", true);
			isBranch = true;
		}
		
		if (request.getParameter("doit") == null || request.getParameter("restart") != null) {
			jsp = "UI.jspx";
		} else {
			// get the session attribute (will be null on fresh visit)
			p = (String) s.getAttribute("principle");
			a = (String) s.getAttribute("amortization");
			
			// if it's a fresh visit, get values from parameters
			if (p == null) {
				p = request.getParameter("principle");
			}
			if (a == null) {
				a = request.getParameter("amortization");
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
				// if interest is a range, compute range
				String[] range = r.split("-");
				if (range.length == 2 && isBranch) {
					request.setAttribute("range", true);
					request.setAttribute("monthly", m.computeRangePayment(p, a, r));
					
				} else {
					// if interest is not a range, compute interest
					request.setAttribute("range", false);
					request.setAttribute("interest", m.validateInterest(r));
					request.setAttribute("monthly", String.format("%.2f", m.computePayment(p, a, r)));
				}
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
