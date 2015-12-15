package Client;

import EntityManager.Account;
import EntityManager.Artist;
import EntityManager.Member;
import EntityManager.ReturnHelper;
import SessionBean.AccountManagement.AccountManagementBeanLocal;
import SessionBean.AdminManagement.AdminManagementBeanLocal;
import SessionBean.MusicManagement.MusicManagementBeanLocal;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String target = request.getParameter("target");
        String source = request.getParameter("source");

        //profile parameters
        HttpSession session = request.getSession();

        session.removeAttribute("goodMsg");
        session.removeAttribute("errMsg");
        ReturnHelper returnHelper = null;

        try {
            switch (target) {
                case "AccountLogin":
                    if (true) {
                        Account account = (Account) session.getAttribute("account");
                        String email = request.getParameter("email");
                        String password = request.getParameter("password");
                        returnHelper = accountManagementBean.loginAccount(email, password);

                        if (returnHelper.getResult()) {
                            account = accountManagementBean.getAccount(email);
                            session.setAttribute("account", account);
                            String accountType = "Unknown";
                            if (account instanceof Artist) {
                                session.setAttribute("artist", (Artist) account);
                                session.setAttribute("albums", musicManagementBean.listAllAlbumByArtistOrBandID(account.getId(), true, true));
                                session.setAttribute("listOfGenres", adminManagementBean.listAllGenres());
                                if (((Artist) account).getIsBand()) {
                                    accountType = "band";
                                } else {
                                    accountType = "artist";
                                }
                                nextPage = "/#!/artist/profile";
                            } else if (account instanceof Member) {
                                session.setAttribute("member", (Member) account);
                                accountType = "fan";
                                nextPage = "/#!/fan/profile";
                            }
                            session.setAttribute("ListOfPurchasedMusics", account.getListOfPurchasedMusics());
                            if (account.getName() != null && !account.getName().isEmpty()) {
                                session.setAttribute("goodMsg", "Welcome back " + account.getName() + "! You are logged in as " + accountType + ".");
                            } else {
                                session.setAttribute("goodMsg", "Welcome back! You are logged in as " + accountType + ".");
                            }

                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                            nextPage = "/#!/login";
                        }
                    }
                    break;

                case "AccountSignup":
                    if (true) {
                        String name = request.getParameter("name");
                        String email = request.getParameter("email");
                        String chkAgree = request.getParameter("chkAgree");
                        String grecaptcharesponse = request.getParameter("g-recaptcha-response");
                        String password = request.getParameter("password");

                        JSONObject jsObj = new JSONObject();
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        if (chkAgree == null) {
                            jsObj.put("result", false);
                            jsObj.put("message", "Sorry. You have not agreed to the terms");
                            response.getWriter().write(jsObj.toString());
                            return;
                        } else if (password == null) {
                            jsObj.put("result", false);
                            jsObj.put("message", "Sorry, Password can not be empty");
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
                        } else if (source.equals("ArtistSignup")) {
                            returnHelper = accountManagementBean.registerAccount(name, email, password, false, true, false);
                        } else if (source.equals("FanSignup")) {
                            returnHelper = accountManagementBean.registerAccount(name, email, password, false, false, false);
                        }

                        if (returnHelper != null && returnHelper.getResult() != null && returnHelper.getResult()) {
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                        }

                        jsObj.put("result", returnHelper.getResult());
                        jsObj.put("message", returnHelper.getDescription());
                        response.getWriter().write(jsObj.toString());
                        return;
                    }

                case "ArtistProfileUpdate":
                    if (true) {
                        Account account = (Account) session.getAttribute("account");
                        if (account != null) {
//                            String repassword = request.getParameter("repassword");
//                            String oldpassword = request.getParameter("oldpassword");
//                            String password = request.getParameter("password");
//                            //check need to update password
//                            if (oldpassword != null && !oldpassword.isEmpty() && password != null && !password.isEmpty() && repassword != null && !repassword.isEmpty()) {
//                                if (!password.equals(repassword)) {
//                                    session.setAttribute("errMsg", "New Password and Re-enter Password does not match. Please try again.");
//                                } else {
//                                    Artist artist = (Artist) (session.getAttribute("artist"));
//                                    if (artist != null) {
//                                        returnHelper = accountManagementBean.updateAccountPassword(artist.getId(), oldpassword, password);
//                                        if (returnHelper.getResult()) {
//                                            session.setAttribute("goodMsg", returnHelper.getDescription());
//                                        }
//                                    } else {
//                                        session.setAttribute("errMsg", "Ops an error has occured");
//                                        nextPage = "/#!/artist/profile";
//                                        break;
//                                    }
//                                }
//                            }

                            String email = request.getParameter("email");
                            //Updates email only if the user enters a different mail
                            if (!account.getEmail().equalsIgnoreCase(email)) {
                                returnHelper = accountManagementBean.updateAccountEmail(account.getId(), email);
                                if (returnHelper.getResult()) {
                                    session.setAttribute("goodMsg", returnHelper.getDescription());
                                    account = accountManagementBean.getAccount(account.getId());
                                    session.setAttribute("account", account);
                                } else {
                                    session.setAttribute("errMsg", returnHelper.getDescription());
                                    nextPage = "/#!/artist/profile";
                                    break;
                                }
                            }

                            Artist artist = (Artist) (session.getAttribute("artist"));
                            Part picture = request.getPart("picture");
                            String contactEmail = request.getParameter("contactEmail");
                            String paypalEmail = request.getParameter("paypalEmail");
                            String genreID = request.getParameter("genreID");
                            String bio = request.getParameter("bio");
                            String influences = request.getParameter("influences");
                            String facebookURL = request.getParameter("facebookURL");
                            String instagramURL = request.getParameter("instagramURL");
                            String twitterURL = request.getParameter("twitterURL");
                            String websiteURL = request.getParameter("websiteURL");
                            
                            if (artist != null) {
                                if (!artist.getIsBand()) {
                                    returnHelper = accountManagementBean.updateArtistProfile(artist.getId(), Long.parseLong(genreID), bio, influences, contactEmail, paypalEmail, facebookURL, instagramURL, twitterURL, websiteURL, picture);
                                } else {
                                    String bandMembers = request.getParameter("bandMembers");
                                    String dateFormed = request.getParameter("dateFormed");

                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = null;

                                    if (dateFormed != null && !dateFormed.isEmpty()) {
                                        date = formatter.parse(dateFormed);
                                    }
                                    returnHelper = accountManagementBean.updateBandProfile(artist.getId(), bandMembers, date, Long.parseLong(genreID), bio, influences, contactEmail, paypalEmail, facebookURL, instagramURL, twitterURL, websiteURL, picture);
                                }

                                if (returnHelper.getResult()) {
                                    session.setAttribute("artist", (Artist) accountManagementBean.getAccount(artist.getEmail()));
                                    session.setAttribute("goodMsg", returnHelper.getDescription());
                                } else {
                                    session.setAttribute("errMsg", returnHelper.getDescription());
                                }
                            }
                            session.setAttribute("redirectPage", "#!/artist/profile");
                        }
                    }
                    return;
                case "SendResetPasswordEmail":
                    if (true) {
                        String email = request.getParameter("email");
                        returnHelper = accountManagementBean.generateAndSendForgetPasswordEmail(email);
                        if (returnHelper.getResult()) {
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                            session.setAttribute("resetPasswordEmail", email);
                            nextPage = "/#!/reset-password2";
                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                            session.setAttribute("resetPasswordEmail", "");
                            nextPage = "/#!/reset-password";
                        }
                    }
                    break;

                case "VerifyResetCode":
                    if (true) {
                        String email = request.getParameter("email");
                        String password = request.getParameter("password");
                        String resetPasswordCode = request.getParameter("resetPasswordCode");
                        returnHelper = accountManagementBean.enterForgetPasswordCode(email, resetPasswordCode);
                        if (returnHelper.getResult()) {
                            Long accountID = returnHelper.getID();
                            returnHelper = accountManagementBean.updateAccountPassword(accountID, password);
                            if (returnHelper.getResult()) {
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                                nextPage = "/#!/login";
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                                nextPage = "/#!/reset-password2";
                            }
                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                            nextPage = "/#!/reset-password2";
                        }
                    }
                    break;

                case "SendEmailVerification":
                    if (true) {
                        Account account = (Account) session.getAttribute("account");
                        if (account != null) {
                            returnHelper = accountManagementBean.generateAndSendVerificationEmail(account.getId(), account.getEmail(), false);
                            if (returnHelper.getResult()) {
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                            }
                            nextPage = "/#!/verify-email";
                        }
                    }
                    break;

                case "SendNewEmailVerification":
                    if (true) {
                        Account account = (Account) session.getAttribute("account");
                        if (account != null) {
                            returnHelper = accountManagementBean.generateAndSendVerificationEmail(account.getId(), account.getEmail(), true);
                            if (returnHelper.getResult()) {
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                            }
                            nextPage = "/#!/change-email";
                        }
                    }
                    break;

                case "VerifyEmail":
                    if (true) {
                        Account account = (Account) session.getAttribute("account");
                        //Email change parameters
                        String verifyEmailCode = request.getParameter("verifyEmailCode");
                        if (account != null) {
                            returnHelper = accountManagementBean.enterEmailVerificationCode(account.getEmail(), verifyEmailCode);
                            if (returnHelper.getResult()) {
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                                //Refresh account
                                account = accountManagementBean.getAccount(account.getId());
                                session.setAttribute("account", account);
                                nextPage = "/#!/verify-email";
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                                nextPage = "/#!/verify-email";
                            }
                        }
                    }
                    break;

                case "CancelUpdateEmail":
                    if (true) {
                        Account account = (Account) session.getAttribute("account");
                        if (account != null) {
                            returnHelper = accountManagementBean.cancelUpdateAccountEmail(account.getId());
                            if (returnHelper.getResult()) {
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                                account = accountManagementBean.getAccount(account.getId());
                                session.setAttribute("account", account);
                                nextPage = "/#!/change-email";
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                                nextPage = "/#!/change-email";
                            }
                        }
                        break;
                    }

                case "VerifyNewEmail":
                    if (true) {
                        //Email change parameters
                        String verifyEmailCode = request.getParameter("verifyEmailCode");
                        Account account = (Account) session.getAttribute("account");
                        if (account != null) {
                            returnHelper = accountManagementBean.enterNewEmailVerificationCode(account.getNewEmail(), verifyEmailCode);
                            if (returnHelper.getResult()) {
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                                //Refresh account
                                account = accountManagementBean.getAccount(account.getId());
                                session.setAttribute("account", account);
                                nextPage = "/#!/change-email";
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                                nextPage = "/#!/change-email";
                            }
                        }
                    }
                    break;

                case "ChangeName":
                    if (true) {
                        Account account = (Account) session.getAttribute("account");
                        if (account != null) {
                            String name = request.getParameter("name");
                            returnHelper = accountManagementBean.updateArtistName(account.getId(), name);
                            if (returnHelper.getResult()) {
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                                //Refresh account
                                account = accountManagementBean.getAccount(account.getId());
                                session.setAttribute("account", account);
                                nextPage = "/#!/change-name";
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                                nextPage = "/#!/change-name";
                            }
                        }
                    }
                    break;

                case "AccountLogout":
                    request.getSession(false).invalidate();
                    session = request.getSession();
                    session.setAttribute("goodMsg", "Logout Successful");
                    nextPage = "/#!/login";
                    break;

                case "PageRedirect":
                    if (source != null) {
                        if (source.equals("albums")) {
                            nextPage = "/#!/artist/albums";
                        } else if (source.equals("profile")) {
                            nextPage = "/#!/artist/profile";
                        } else if (source.equals("transactionHistory")) {
                            nextPage = "/#!/fan/profile";
                        }
                    }
                    break;

                case "GetPastPurchases":
                    if (true) {
                        Account account = (Account) session.getAttribute("account");
                        account = accountManagementBean.getAccount(account.getId());
                        session.setAttribute("ListOfPurchasedMusics", account.getListOfPurchasedMusics());
                    }
                    break;

                case "ChangePassword":
                    if (true) {
                        Account account = (Account) session.getAttribute("account");
                        if (account != null) {
                            String repassword = request.getParameter("repassword");
                            String oldpassword = request.getParameter("oldpassword");
                            String password = request.getParameter("password");
                            //check need to update password
                            if (oldpassword != null && !oldpassword.isEmpty() && password != null && !password.isEmpty() && repassword != null && !repassword.isEmpty()) {
                                if (!password.equals(repassword)) {
                                    session.setAttribute("errMsg", "New Password and Re-enter Password does not match. Please try again.");
                                } else {
                                    returnHelper = accountManagementBean.updateAccountPassword(account.getId(), oldpassword, password);
                                    if (returnHelper.getResult()) {
                                        session.setAttribute("goodMsg", returnHelper.getDescription());
                                    } else {
                                        session.setAttribute("errMsg", returnHelper.getDescription());
                                    }
                                    nextPage = "/#!/change-password";
                                    break;
                                }
                            }
                        }
                    }
                    break;
            }

            if (nextPage.equals("")) {
                session.setAttribute("errMsg", "Ops. Session expired. Please try again.");
                response.sendRedirect("/#!/login");
                return;
            } else {
                response.sendRedirect(nextPage);
                return;
            }
        } catch (Exception ex) {
            response.sendRedirect("/error500.html");
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
