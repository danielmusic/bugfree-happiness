package Admin;

import EntityManager.Account;
import EntityManager.Admin;
import EntityManager.Artist;
import EntityManager.Member;
import EntityManager.ReturnHelper;
import SessionBean.AccountManagement.AccountManagementBeanLocal;
import SessionBean.AdminManagement.AdminManagementBeanLocal;
import SessionBean.MusicManagement.MusicManagementBeanLocal;
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
    private MusicManagementBeanLocal musicManagementBean;

    @EJB
    private AdminManagementBeanLocal adminManagementBean;

    @EJB
    private AccountManagementBeanLocal accountManagementBean;

    String nextPage = "", goodMsg = "", errMsg = "";
    HttpSession session;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Welcome to AccountManagementController");
        String target = request.getParameter("target");
        String source = request.getParameter("source");
        String id = request.getParameter("id");
        String email = request.getParameter("email");
        String password = request.getParameter("pwd");

        System.out.println("target " + target);

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

                case "ListAllBand":
                    if (checkLogin(response)) {
                        List<Artist> bands = adminManagementBean.listAllBands(true);
                        if (bands == null) {
                            nextPage = "admin/error500.html";
                        } else {
                            session.setAttribute("bands", bands);
                            nextPage = "admin/AccountManagement/bandManagement.jsp";
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

                case "ApproveArtist":
                    if (checkLogin(response)) {
                        returnHelper = adminManagementBean.approveArtistOrBand(Long.parseLong(id));
                        if (returnHelper.getResult()) {
                            if (source.equals("band")) {
                                nextPage = "admin/AccountManagement/band.jsp?goodMsg=" + returnHelper.getDescription();
                            } else if (source.equals("artist")){
                                nextPage = "admin/AccountManagement/artist.jsp?goodMsg=" + returnHelper.getDescription();
                            }
                        } else {
                            if (source.equals("band")) { 
                                nextPage = "admin/AccountManagement/band.jsp?goodMsg=" + returnHelper.getDescription();
                            } else if (source.equals("artist")){
                                nextPage = "admin/AccountManagement/artist.jsp?errMsg=" + returnHelper.getDescription();
                            }
                        }
                    }
                    break;

                case "RejectArtist":
                    if (checkLogin(response)) {
                        returnHelper = adminManagementBean.rejectArtistOrBand(Long.parseLong(id));
                        if (returnHelper.getResult()) {
                            if (source.equals("band")) {
                                nextPage = "admin/AccountManagement/band.jsp?goodMsg=" + returnHelper.getDescription();
                            } else {
                                nextPage = "admin/AccountManagement/artist.jsp?goodMsg=" + returnHelper.getDescription();
                            }
                        } else {
                            if (source.equals("band")) {
                                nextPage = "admin/AccountManagement/band.jsp?goodMsg=" + returnHelper.getDescription();
                            } else {
                                nextPage = "admin/AccountManagement/artist.jsp?errMsg=" + returnHelper.getDescription();
                            }
                        }
                    }
                    break;

                case "ListArtistbyID":
                    if (checkLogin(response)) {
                        if (id != null) {
                            Artist artist = adminManagementBean.getArtist(Long.parseLong(id));
                            session.setAttribute("artist", artist);
                            nextPage = "admin/AccountManagement/artist.jsp";
                        }
                    }
                    break;

                case "ListBandbyID":
                    if (checkLogin(response)) {
                        if (id != null) {
                            Artist band = adminManagementBean.getArtist(Long.parseLong(id));
                            session.setAttribute("band", band);
                            nextPage = "admin/AccountManagement/band.jsp";
                        }
                    }
                    break;

                case "DisableAccount":
                    if (checkLogin(response)) {
                        returnHelper = accountManagementBean.disableAccount(Long.parseLong(id));
                        if (returnHelper.getResult()) {
                            if (source != null && source.equals("artistManagement")) {
                                List<Artist> artists = adminManagementBean.listAllArtists(true);
                                if (artists == null) {
                                    nextPage = "admin/error500.html";
                                } else {
                                    session.setAttribute("artists", artists);
                                    nextPage = "admin/AccountManagement/artistManagement.jsp";
                                }
                            } else if (source != null && source.equals("bandManagement")) {
                                List<Artist> bands = adminManagementBean.listAllBands(true);
                                if (bands == null) {
                                    nextPage = "admin/error500.html";
                                } else {
                                    session.setAttribute("bands", bands);
                                    nextPage = "admin/AccountManagement/bandManagement.jsp";
                                }
                            } else if (source != null && source.equals("fanManagement")) {
                                List<Member> fans = adminManagementBean.listAllMembers(true);
                                if (fans == null) {
                                    nextPage = "admin/error500.html";
                                } else {
                                    session.setAttribute("fans", fans);
                                    nextPage = "admin/AccountManagement/fanManagement.jsp";
                                }
                            }
                        }
                    }
                    break;

                case "GetDownloadLink":
                    if (checkLogin(response)) {
                        String url = musicManagementBean.generateDownloadLink(Long.parseLong(id), "wav", false, 300L);
                        System.out.println(url);
                        nextPage = url;
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
