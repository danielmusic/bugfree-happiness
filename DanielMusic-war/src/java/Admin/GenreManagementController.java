package Admin;

import EntityManager.Admin;
import EntityManager.Genre;
import EntityManager.ReturnHelper;
import SessionBean.AdminManagement.AdminManagementBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GenreManagementController extends HttpServlet {

    @EJB
    private AdminManagementBeanLocal adminManagementBean;

    String nextPage = "", goodMsg = "", errMsg = "";
    HttpSession session;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String target = request.getParameter("target");
        String name = request.getParameter("name");
        String id = request.getParameter("id");

        session = request.getSession();
        ReturnHelper returnHelper;
        List<Genre> genres = null;

        try {
            if (checkLogin(response)) {
                switch (target) {
                    case "AddGenre":
                        returnHelper = adminManagementBean.createGenre(name);
                        if (returnHelper.getResult()) {
                            genres = adminManagementBean.listAllGenres();
                            if (genres == null) {
                                nextPage = "admin/error500.html";
                            } else {
                                session.setAttribute("genres", genres);
                                nextPage = "admin/GenreManagement/GenreManagement.jsp?goodMsg=" + returnHelper.getDescription();
                            }
                        }
                        break;

                    case "ListAllGenre":
                        genres = adminManagementBean.listAllGenres();
                        if (genres == null) {
                            nextPage = "admin/error500.html";
                        } else {
                            session.setAttribute("genres", genres);
                            nextPage = "admin/GenreManagement/GenreManagement.jsp";
                        }
                        break;

                    case "DeleteGenre":
                        returnHelper = adminManagementBean.deleteGenre(Long.parseLong(id));
                        if (returnHelper.getResult()) {
                            genres = adminManagementBean.listAllGenres();
                            if (genres == null) {
                                nextPage = "admin/error500.html";
                            } else {
                                session.setAttribute("genres", genres);
                                nextPage = "admin/GenreManagement/GenreManagement.jsp?goodMsg=" + returnHelper.getDescription();
                            }
                        }
                        break;
                }
            }

            if (nextPage.equals("")) {
                response.sendRedirect("admin/login.jsp?errMsg=Session Expired.");
                return;
            } else {
                response.sendRedirect(nextPage);
                return;
            }

        } catch (Exception ex) {
            response.sendRedirect("admin/error500.html");
            ex.printStackTrace();
            return;
        }
    }

    public boolean checkLogin(HttpServletResponse response) {
        try {
            Admin admin = (Admin) (session.getAttribute("admin"));
            if (admin == null) {
                response.sendRedirect("admin/login.jsp?errMsg=Session Expired.");
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
