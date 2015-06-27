package Client;

import EntityManager.Account;
import EntityManager.Artist;
import EntityManager.Band;
import EntityManager.Member;
import EntityManager.ReturnHelper;
import SessionBean.AccountManagement.AccountManagementBeanLocal;
import SessionBean.AdminManagement.AdminManagementBeanLocal;
import SessionBean.MusicManagement.MusicManagementBeanLocal;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.json.JSONObject;

@MultipartConfig
public class ClientAccountManagementController extends HttpServlet {

    @EJB
    private AdminManagementBeanLocal adminManagementBean;

    @EJB
    private AccountManagementBeanLocal accountManagementBean;

    @EJB
    private MusicManagementBeanLocal musicManagementBean;

    String nextPage = "", goodMsg = "", errMsg = "";
    HttpSession session;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Welcome to client account managment controller");
        String target = request.getParameter("target");
        String source = request.getParameter("source");
        String id = request.getParameter("id");

        //profile parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String paypalEmail = request.getParameter("paypalEmail");
        String genreID = request.getParameter("genre");
        String bio = request.getParameter("bio");
        String influences = request.getParameter("influences");
        String facebookURL = request.getParameter("facebookURL");
        String instagramURL = request.getParameter("instagramURL");
        String twitterURL = request.getParameter("twitterURL");
        String websiteURL = request.getParameter("websiteURL");

        String oldpassword = request.getParameter("oldpassword");
        String password = request.getParameter("password");

        //signup parameters
        String chkAgree = request.getParameter("chkAgree");
        String grecaptcharesponse = request.getParameter("g-recaptcha-response");

        session = request.getSession();
        session.removeAttribute("goodMsg");
        session.removeAttribute("errMsg");
        ReturnHelper returnHelper;

        JSONObject jsObj = new JSONObject();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            switch (target) {
                case "AccountLogin":
                    returnHelper = accountManagementBean.loginAccount(email, password);

                    if (returnHelper.getResult()) {
                        Account account = accountManagementBean.getAccount(email);
                        if (account instanceof Artist) {
                            session.setAttribute("artist", (Artist) account);
                            session.setAttribute("albums", musicManagementBean.ListAllAlbumByArtistorBandID(account.getId(), true, true));
                            session.setAttribute("genres", adminManagementBean.listAllGenres());
                        } else if (account instanceof Band) {
                            session.setAttribute("band", (Band) account);
                            session.setAttribute("albums", musicManagementBean.ListAllAlbumByArtistorBandID(account.getId(), true, true));
                            session.setAttribute("genres", adminManagementBean.listAllGenres());
                        } else if (account instanceof Member) {
                            session.setAttribute("fan", (Member) account);
                        }
                        nextPage = "#!/artist/profile";
                    } else {
                        session.setAttribute("errMsg", returnHelper.getDescription());
                        nextPage = "#!/login";
                    }
                    break;

                case "AccountSignup":
                    System.out.println("Controller: AccountSignup");
                    if (chkAgree == null) {
                        jsObj.put("result", false);
                        jsObj.put("message", "Sorry. You have not agreed to the terms");
                        response.getWriter().write(jsObj.toString());
                        return;
                    } else if (grecaptcharesponse == null || !VerifyRecaptcha.verify(grecaptcharesponse)) {
                        jsObj.put("result", false);
                        jsObj.put("message", "Please verify the captcha again.");
                        response.getWriter().write(jsObj.toString());
                        return;
                    } else if (source == null) {
                        jsObj.put("result", false);
                        jsObj.put("message", "Please indicate if you are signing up as an artist or band.");
                        response.getWriter().write(jsObj.toString());
                        return;
                    }

                    if (source.equals("BandSignup")) {
                        returnHelper = accountManagementBean.registerAccount(name, email, password, false, false, true);
                    } else {//normal artist
                        returnHelper = accountManagementBean.registerAccount(name, email, password, false, true, false);
                    }

                    if (returnHelper.getResult()) {
                        session.setAttribute("goodMsg", returnHelper.getDescription());
                    }

                    jsObj.put("result", returnHelper.getResult());
                    jsObj.put("message", returnHelper.getDescription());
                    response.getWriter().write(jsObj.toString());
                    return;

                case "ArtistProfileUpdate":
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    if (artist != null) {
                        //check need to update password
                        if (oldpassword != null && !oldpassword.isEmpty() && password != null && !password.isEmpty()) {
                            returnHelper = accountManagementBean.updateAccountPassword(artist.getId(), oldpassword, password);
                            if (returnHelper.getResult()) {
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                            }
                        }

                        Part picture = request.getPart("picture");

                        returnHelper = accountManagementBean.updateArtistProfile(artist.getId(), Long.parseLong(genreID), bio, influences, email, paypalEmail, facebookURL, instagramURL, twitterURL, websiteURL, picture);
                        if (returnHelper.getResult()) {
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                        }

                        nextPage = "#!/artist/profile";
                    }
                    break;

                case "AccountLogout":
                    session.removeAttribute("errMsg");
                    session.removeAttribute("artist");
                    session.removeAttribute("band");
                    session.removeAttribute("fan");
                    session.setAttribute("goodMsg", "Logout Successful");
                    nextPage = "#!/login";
                    break;
            }

            if (nextPage.equals("")) {
                session.setAttribute("errMsg", "Ops. Session expired. Please try again.");
                response.sendRedirect("#!/login");
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
