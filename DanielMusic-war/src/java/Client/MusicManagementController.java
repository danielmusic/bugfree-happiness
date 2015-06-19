package Client;

import EntityManager.Artist;
import EntityManager.Band;
import EntityManager.ReturnHelper;
import SessionBean.MusicManagement.MusicManagementBeanLocal;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public class MusicManagementController extends HttpServlet {

    @EJB
    private MusicManagementBeanLocal musicManagementBean;

    String nextPage = "", goodMsg = "", errMsg = "";
    HttpSession session;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String target = request.getParameter("target");
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        session = request.getSession();
        session.removeAttribute("message");

        Artist artist = (Artist) (session.getAttribute("artist"));
        Band band = (Band) (session.getAttribute("band"));
        ReturnHelper returnHelper;

        try {
            switch (target) {
                case "ArtistAddAlbum":
                    if (checkArtistLogin(response)) {
                        Part picture = request.getPart("picture");

                        if (picture != null) {
                            returnHelper = musicManagementBean.createAlbum(picture, name, description, artist.getId());
                            if (returnHelper.getResult()) {
                                nextPage = "#!/artist/albums";
                            } else {
                                nextPage = "#!/login";
                                session.setAttribute("errMsg", returnHelper.getDescription());
                            }
                        }
                        break;
                    }
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

    public boolean checkArtistLogin(HttpServletResponse response) {
        try {
            Artist artist = (Artist) (session.getAttribute("artist"));
            if (artist == null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean checkBandLogin(HttpServletResponse response) {
        try {
            Band band = (Band) (session.getAttribute("band"));
            if (band == null) {
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
