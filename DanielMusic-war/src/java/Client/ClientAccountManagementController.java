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
import javax.servlet.jsp.PageContext;
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Welcome to client account managment controller");
        String target = request.getParameter("target");
        String source = request.getParameter("source");

        //profile parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String newEmail = request.getParameter("newEmail");
        String contactEmail = request.getParameter("contactEmail");
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

        //Password reset parameters
        String resetPasswordCode = request.getParameter("resetPasswordCode");

        //Email change parameters
        String verifyEmailCode = request.getParameter("verifyEmailCode");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        Artist artist = (Artist) (session.getAttribute("artist"));
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
                        account = accountManagementBean.getAccount(email);
                        session.setAttribute("account", account);
                        if (account instanceof Artist) {
                            session.setAttribute("artist", (Artist) account);
                            session.setAttribute("albums", musicManagementBean.ListAllAlbumByArtistorBandID(account.getId(), true, true));
                            session.setAttribute("genres", adminManagementBean.listAllGenres());
                            nextPage = "#!/artist/profile";
                        } else if (account instanceof Band) {
                            session.setAttribute("band", (Band) account);
                            session.setAttribute("albums", musicManagementBean.ListAllAlbumByArtistorBandID(account.getId(), true, true));
                            session.setAttribute("genres", adminManagementBean.listAllGenres());
                            nextPage = "#!/band/profile";
                        } else if (account instanceof Member) {
                            session.setAttribute("fan", (Member) account);
                            nextPage = "#!/fan/profile";
                        }
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
                    if (artist != null) {
                        //check need to update password
                        if (oldpassword != null && !oldpassword.isEmpty() && password != null && !password.isEmpty()) {
                            returnHelper = accountManagementBean.updateAccountPassword(artist.getId(), oldpassword, password);
                            if (returnHelper.getResult()) {
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                            }
                        }

                        //Updates email only if the user enters a different mail
                        if (!account.getEmail().equalsIgnoreCase(email)) {
                            returnHelper = accountManagementBean.updateAccountEmail(account.getId(), email);
                            if (returnHelper.getResult()) {
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                                account = accountManagementBean.getAccount(account.getId());
                                session.setAttribute("account", account);
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                                nextPage = "#!/artist/profile";
                                break;
                            }
                        }

                        Part picture = request.getPart("picture");
                        if (picture.getSize() == 0) {
                            picture = null;
                        }
                        returnHelper = accountManagementBean.updateArtistProfile(artist.getId(), Long.parseLong(genreID), bio, influences, contactEmail, paypalEmail, facebookURL, instagramURL, twitterURL, websiteURL, picture);
                        if (returnHelper.getResult()) {
                            session.setAttribute("artist", (Artist) accountManagementBean.getAccount(artist.getEmail()));
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                        }

                        nextPage = "#!/artist/profile";
                    }
                    break;
                case "SendResetPasswordEmail":
                    returnHelper = accountManagementBean.generateAndSendForgetPasswordEmail(email);
                    if (returnHelper.getResult()) {
                        session.setAttribute("goodMsg", returnHelper.getDescription());
                        session.setAttribute("resetPasswordEmail", email);
                        nextPage = "#!/reset-password2";
                    } else {
                        session.setAttribute("errMsg", returnHelper.getDescription());
                        session.setAttribute("resetPasswordEmail", "");
                        nextPage = "#!/reset-password";
                    }
                    break;
                case "VerifyResetCode":
                    returnHelper = accountManagementBean.enterForgetPasswordCode(email, resetPasswordCode);
                    if (returnHelper.getResult()) {
                        Long accountID = returnHelper.getID();
                        returnHelper = accountManagementBean.updateAccountPassword(accountID, password);
                        if (returnHelper.getResult()) {
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                            nextPage = "#!/login";
                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                            nextPage = "#!/reset-password2";
                        }
                    } else {
                        session.setAttribute("errMsg", returnHelper.getDescription());
                        nextPage = "#!/reset-password2";
                    }
                    break;
                case "SendEmailVerification":
                    if (account != null) {
                        returnHelper = accountManagementBean.generateAndSendVerificationEmail(account.getId(), account.getEmail(), false);
                        if (returnHelper.getResult()) {
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                        }
                        nextPage = "#!/verify-email";
                    }
                    break;
                case "SendNewEmailVerification":
                    if (account != null) {
                        returnHelper = accountManagementBean.generateAndSendVerificationEmail(account.getId(), account.getEmail(), true);
                        if (returnHelper.getResult()) {
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                        }
                        nextPage = "#!/change-email";
                    }
                    break;
                case "VerifyEmail":
                    if (account != null) {
                        returnHelper = accountManagementBean.enterEmailVerificationCode(account.getEmail(), verifyEmailCode);
                        if (returnHelper.getResult()) {
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                            //Refresh account
                            account = accountManagementBean.getAccount(account.getId());
                            session.setAttribute("account", account);
                            nextPage = "#!/verify-email";
                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                            nextPage = "#!/verify-email";
                        }
                    }
                    break;
                case "CancelUpdateEmail":
                    if (account != null) {
                        returnHelper = accountManagementBean.cancelUpdateAccountEmail(account.getId());
                        if (returnHelper.getResult()) {
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                            account = accountManagementBean.getAccount(account.getId());
                            session.setAttribute("account", account);
                            nextPage = "#!/change-email";
                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                            nextPage = "#!/change-email";
                        }
                    }
                    break;
                case "VerifyNewEmail":
                    if (account != null) {
                        returnHelper = accountManagementBean.enterNewEmailVerificationCode(account.getNewEmail(), verifyEmailCode);
                        if (returnHelper.getResult()) {
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                            //Refresh account
                            account = accountManagementBean.getAccount(account.getId());
                            session.setAttribute("account", account);
                            nextPage = "#!/change-email";
                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                            nextPage = "#!/change-email";
                        }
                    }
                    break;
                case "AccountLogout":
//                    session.removeAttribute("errMsg");
//                    session.removeAttribute("artist");
//                    session.removeAttribute("band");
//                    session.removeAttribute("fan");
                    request.getSession(false).invalidate();
                    session = request.getSession();

                    session.setAttribute("goodMsg", "Logout Successful");
                    nextPage = "#!/login";
                    break;

                case "PageRedirect":
                    if (source != null) {
                        if (source.equals("albums")) {
                            nextPage = "#!/artist/albums";
                        } else if (source.equals("profile")) {
                            nextPage = "#!/artist/profile";
                        }
                    }
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
