package Admin;

import EntityManager.Account;
import EntityManager.ReturnHelper;
import SessionBean.AccountManagement.AccountManagementBeanLocal;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminAccountManagementController extends HttpServlet {

    @EJB
    private AccountManagementBeanLocal accountManagementBean;

    String nextPage = "", goodMsg = "", errMsg = "";
    HttpSession session;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String target = request.getParameter("target");
        String email = request.getParameter("email");
        String password = request.getParameter("pwd");

        session = request.getSession();
        ReturnHelper returnHelper;

        try {
            switch (target) {
                case "Login":
                    System.out.println("email " + email);
                    System.out.println("passw " + password);
                    returnHelper = accountManagementBean.loginAccount(email, password);
                    if (returnHelper.getResult()) {
                        session.setAttribute("staff", accountManagementBean.getAccount(email));
                        nextPage = "workspace.jsp";
                    } else {
                        nextPage = "admin/login.jsp?errMsg=" + returnHelper.getDescription();
                    }
                    break;
            }
            if (nextPage.equals("")) {
                response.sendRedirect("login.jsp?errMsg=Session Expired.");
            } else {
                response.sendRedirect(nextPage);
            }

        } catch (Exception ex) {
            System.out.println("EROOOOOOOOOOOOOOORR");
            response.sendRedirect("error500.html");
            ex.printStackTrace();
        }
    }

    public boolean checkLogin(HttpServletResponse response) {
        try {
            Account staff = (Account) (session.getAttribute("staff"));
            if (staff == null) {
                response.sendRedirect("login.jsp?errMsg=Session Expired.");
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            return false;
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
