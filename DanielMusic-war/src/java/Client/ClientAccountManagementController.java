package Client;

import EntityManager.Account;
import EntityManager.Artist;
import EntityManager.Band;
import EntityManager.Member;
import EntityManager.ReturnHelper;
import SessionBean.AccountManagement.AccountManagementBeanLocal;
import java.io.IOException;
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
        System.out.println("Welcome to client account managment controller");
        String target = request.getParameter("target");
        String source = request.getParameter("source");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String bio = request.getParameter("bio");
        String profilePicURL = request.getParameter("profilePicURL");
        String oldpassword = request.getParameter("oldpassword");
        String password = request.getParameter("pwd");
        String chkAgree = request.getParameter("chkAgree");
        String grecaptcharesponse = request.getParameter("g-recaptcha-response");

        session = request.getSession();
        session.removeAttribute("message");
        ReturnHelper returnHelper;

        try {
            switch (target) {
                case "ArtistSignup":
                    if (chkAgree == null) {
                        nextPage = "#!/artist/signup";
                        session.setAttribute("errMsg", "Sorry. You have not agreed to the terms");
                        break;
                    } else if (grecaptcharesponse == null) {
                        nextPage = "#!/artist/signup";
                        session.setAttribute("errMsg", "Please verify the captcha again.");
                        break;
                    } else if (!VerifyRecaptcha.verify(grecaptcharesponse)) {
                        nextPage = "#!/artist/signup";
                        session.setAttribute("errMsg", "Please verify the captcha again.");
                        break;
                    }

                    if (source.equals("BandSignup")) {
                        returnHelper = accountManagementBean.registerAccount(name, email, password, false, false, true);
                    } else {//normal artist
                        returnHelper = accountManagementBean.registerAccount(name, email, password, false, true, false);
                    }

                    if (returnHelper.getResult()) {
                        nextPage = "#!/login";
                        session.setAttribute("goodMsg", returnHelper.getDescription());
                    } else {
                        nextPage = "#!/artist/signup";
                        session.setAttribute("errMsg", returnHelper.getDescription());
                    }
                    break;

                case "ArtistProfileUpdate":
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    if (artist != null) {
                        if (oldpassword != null && !oldpassword.isEmpty() && password != null && !password.isEmpty()) {
                            returnHelper = accountManagementBean.updateAccountPassword(artist.getId(), oldpassword, password);
                            if (returnHelper.getResult()) {
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                            }
                        }
                        nextPage = "#!/artist/profile";
                    }
                    break;

                case "ArtistLogin":
                    returnHelper = accountManagementBean.loginAccount(email, password);
                    if (returnHelper.getResult()) {
                        Account account = accountManagementBean.getAccount(email);
                        if (account instanceof Artist) {
                            session.setAttribute("artist", (Artist) accountManagementBean.getAccount(email));
                            nextPage = "#!/artist/albums";
                        } else if (account instanceof Band) {
                            session.setAttribute("band", (Band) accountManagementBean.getAccount(email));
                            nextPage = "#!/band/albums";
                        }
                    } else {
                        nextPage = "#!/login";
                        session.setAttribute("errMsg", returnHelper.getDescription());
                    }
                    break;

                case "FanLogin":
                    returnHelper = accountManagementBean.loginAccount(email, password);
                    if (returnHelper.getResult()) {
                        session.setAttribute("fan", (Member) accountManagementBean.getAccount(email));
                        nextPage = "#!/artist/profile";
                    } else {
                        nextPage = "#!/login";
                        session.setAttribute("errMsg", returnHelper.getDescription());
                    }
                    break;

                case "AccountLogout":
                    session.removeAttribute("errMsg");
                    session.removeAttribute("artist");
                    session.removeAttribute("band");
                    session.removeAttribute("fan");
                    nextPage = "#!/login";
                    session.setAttribute("goodMsg", "Logout Successful");
                    break;
            }

            if (nextPage.equals("")) {
                response.sendRedirect("#!/home");
                return;
            } else {
                response.sendRedirect(nextPage);
                return;
            }

        } catch (Exception ex) {
            response.sendRedirect("error500.html");
            ex.printStackTrace();
            return;
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
