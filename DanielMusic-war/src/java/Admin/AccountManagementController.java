package Admin;

import EntityManager.Account;
import EntityManager.Admin;
import EntityManager.Artist;
import EntityManager.Band;
import EntityManager.Genre;
import EntityManager.Member;
import EntityManager.ReturnHelper;
import SessionBean.AccountManagement.AccountManagementBeanLocal;
import SessionBean.AdminManagement.AdminManagementBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccountManagementController extends HttpServlet {

    @EJB
    private AdminManagementBeanLocal adminManagementBean;

    @EJB
    private AccountManagementBeanLocal accountManagementBean;

    String nextPage = "", goodMsg = "", errMsg = "";
    HttpSession session;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String target = request.getParameter("target");
        String id = request.getParameter("id");
        String email = request.getParameter("email");
        String password = request.getParameter("pwd");

        session = request.getSession();
        ReturnHelper returnHelper;

        try {
            switch (target) {
                case "Login":
                    returnHelper = accountManagementBean.loginAccount(email, password);
                    if (returnHelper.getResult()) {
                        Account account = accountManagementBean.getAccount(email);
                        if (account instanceof Admin) {
                            session.setAttribute("admin", (Admin) accountManagementBean.getAccount(email));
                            nextPage = "admin/workspace.jsp";
                        } else {
                            nextPage = "admin/login.jsp?errMsg=Access Denied";
                        }
                    } else {
                        nextPage = "admin/login.jsp?errMsg=" + returnHelper.getDescription();
                    }
                    break;

                case "Logout":
                    session.invalidate();
                    nextPage = "admin/login.jsp?goodMsg=Logout Successful";
                    break;

                case "ListAllArtist":
                    if (checkLogin(response)) {
                        List<Artist> artists = adminManagementBean.listAllArtists(true);
                        if (artists == null) {
                            nextPage = "admin/error500.html";
                        } else {
                            session.setAttribute("artists", artists);
                            nextPage = "admin/AccountManagement/artistManagement.jsp";
                        }
                    }
                    break;

                case "ListAllFan":
                    if (checkLogin(response)) {
                        List<Member> fans = adminManagementBean.listAllMembers(true);
                        if (fans == null) {
                            nextPage = "admin/error500.html";
                        } else {
                            session.setAttribute("fans", fans);
                            nextPage = "admin/AccountManagement/fanManagement.jsp";
                        }
                    }
                    break;

                case "ListAllBand":
//                    if (checkLogin(response)) {
//                        List<Band> bands = adminManagementBean.listAllBandss(true);
//                        if (bands == null) {
//                            nextPage = "admin/error500.html";
//                        } else {
//                            session.setAttribute("bands", bands);
//                            nextPage = "admin/AccountManagement/bandManagement.jsp";
//                        }
//                    }
                    break;

                case "ListAllGenre":
                    if (checkLogin(response)) {
                        List<Genre> genres = adminManagementBean.listAllGenres(true);
                        if (genres == null) {
                            nextPage = "admin/error500.html";
                        } else {
                            session.setAttribute("genres", genres);
                            nextPage = "admin/GenreManagement/GenreManagement.jsp";
                        }
                    }
                    break;

                case "DisableArtist":
                    if (checkLogin(response)) {
                        returnHelper = accountManagementBean.disableAccount(Long.parseLong(id));
                        if (returnHelper.getResult()) {
                            List<Artist> artists = adminManagementBean.listAllArtists(true);
                            if (artists == null) {
                                nextPage = "admin/error500.html";
                            } else {
                                session.setAttribute("artists", artists);
                                nextPage = "admin/ArtistManagement/artistManagement.jsp";
                            }
                        }
                    }
                    break;
            }

            if (nextPage.equals("")) {
                response.sendRedirect("admin/login.jsp?errMsg=Session Expired.");
                return;
            } else {
                response.sendRedirect(nextPage);
                return;
            }

        } catch (Exception ex) {
            //ex.printStackTrace();
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
