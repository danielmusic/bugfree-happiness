package Client;

import EntityManager.ReturnHelper;
import SessionBean.AccountManagement.AccountManagementBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ClientAccountManagementController extends HttpServlet {

    @EJB
    private AccountManagementBeanLocal accountManagementBean;

    String nextPage = "", goodMsg = "", errMsg = "";
    HttpSession session;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String target = request.getParameter("target");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("pwd");
        String chkAgree = request.getParameter("chkAgree");

        session = request.getSession();
        ReturnHelper returnHelper;

        try {
            switch (target) {
                case "ArtistSignup":
                    if (chkAgree != null) {
                        returnHelper = accountManagementBean.registerAccount(name, email, password, false, true);
                        if (returnHelper.isCompletedSuccesfully()) {
                            nextPage = "artist/signup/?goodMsg=" + returnHelper.getDescription();
                        } else {
                            nextPage = "artist/signup/?errMsg=" + returnHelper.getDescription();
                        }
                        break;
                    }

            }
        } catch (Exception ex) {
            response.sendRedirect("error500.html");
            ex.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
