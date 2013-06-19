/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author godgiven
 */
@WebServlet(name = "fromCountry", urlPatterns = {"/fromCountry"})
public class fromCountry extends HttpServlet {

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

        String toCountry = request.getParameter("toCountry");
        String fromCountry = request.getParameter("fromCountry");
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("\n"
                    + "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "    <head>\n"
                    + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                    + "        <title>City-Capital</title>\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    + "           <form name=\"fromCountry\" action=\"result\" method=\"post\">\n"
                    + "City to           <select name=\"toCity\">\n"
                    + SOAPCalls.GetCities(toCountry)
                    + "           </select><br>\n"
                    + "Capital           <input  type=\"text\" name=\"capital\"><br>\n"
                    + "           <input  type=\"hidden\" name=\"toCountry\" value=\"" + toCountry + "\"><br>\n"
                    + "           <input  type=\"hidden\" name=\"fromCountry\" value=\"" + fromCountry + "\"><br>\n"
                    + "           <button type=\"submit\">Next</button>\n"
                    + "           </form>\n"
                    + "    </body>\n"
                    + "</html>");
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
