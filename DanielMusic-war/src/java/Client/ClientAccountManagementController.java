package Client;

import EntityManager.Artist;
import EntityManager.Member;
import EntityManager.ReturnHelper;
import SessionBean.AccountManagement.AccountManagementBeanLocal;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.validator.constraints.URL;

public class ClientAccountManagementController extends HttpServlet {

    @EJB
    private AccountManagementBeanLocal accountManagementBean;

    String nextPage = "", goodMsg = "", errMsg = "";
    HttpSession session;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Welcome to client account managment controller");
        String target = request.getParameter("target");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("pwd");
        String chkAgree = request.getParameter("chkAgree");
        String verify = request.getParameter("g-recaptcha-response");

        session = request.getSession();
        session.removeAttribute("message");
        ReturnHelper returnHelper;

        try {
            switch (target) {
                case "ArtistSignup":
//                    StringBuilder sb = new StringBuilder();
//                    sb.append("Hello there");
//
//                    URL url = new URL("theservlet's URL") {};
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setDoOutput(true);
//                    connection.setRequestMethod("POST");
//                    connection.setRequestProperty("Content-Length", "" + sb.length());
//
//                    OutputStreamWriter outputWriter = new OutputStreamWriter(connection.getOutputStream());
//                    outputWriter.write(sb.toString());
//                    outputWriter.flush();
//                    outputWriter.close();

                    if (chkAgree != null) {
                        returnHelper = accountManagementBean.registerAccount(name, email, password, false, true);
                        if (returnHelper.getResult()) {
                            nextPage = "#!/artist/signup/";
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                        } else {
                            nextPage = "#!/artist/signup/";
                            session.setAttribute("errMsg", returnHelper.getDescription());
                        }
                        break;
                    }

                case "ArtistLogin":
                    returnHelper = accountManagementBean.loginAccount(email, password);
                    if (returnHelper.getResult()) {
                        session.setAttribute("artist", (Artist) accountManagementBean.getAccount(email));
                        nextPage = "#!/artist/profile";
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
                    session.removeAttribute("fan");
                    nextPage = "#!/login";
                    session.setAttribute("goodMsg", "Logout Successful");
                    break;

            }

            if (nextPage.equals("")) {
                response.sendRedirect("#!/home");
            } else {
                response.sendRedirect(nextPage);
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
