/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author godgiven
 */
public class result extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String toCountry = request.getParameter("toCountry"), fromCountry = request.getParameter("fromCountry"),
                capital = request.getParameter("capital"), toCity = request.getParameter("toCity");

        double c;

        try {

            c = Double.parseDouble(capital);
            if (c < 0) {
                out.println("<a>Positive Capital Please</a><br>");
                //out.println("<a href=\"fromCountry\">Try Again</a>");
                out.println("<a href=\"javascript:history.back()\">Try Again</a>");
                return;
            }

        } catch (NumberFormatException nfe) {
            out.println("<a>Capital must be a number!</a><br>");
            //out.println("<a href=\"fromCountry\">Try Again</a>");
            out.println("<a href=\"javascript:history.back()\">Try Again</a>");
            return;
        }


        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Result</title>");
            out.println("</head>");
            out.println("<body>");
            out.println(SOAPCalls.GetWeatherAndCurrency(fromCountry, toCountry, capital, toCity));
            out.println(SOAPCalls.GetCurrency(toCountry));
            out.println("<br><a href=\"toCountry\">Try Another Travel</a>");
            out.println("<br>\n</body>");
            out.println("</html>");
        } catch (Exception e) {
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
