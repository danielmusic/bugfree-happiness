package Client;

import EntityManager.Artist;
import EntityManager.ExploreHelper;
import EntityManager.ReturnHelper;
import EntityManager.SearchHelper;
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
import org.json.JSONObject;

public class MusicController extends HttpServlet {

    @EJB
    private AdminManagementBeanLocal adminManagementBean;

    @EJB
    private MusicManagementBeanLocal musicManagementBean;

    HttpSession session;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String target = request.getParameter("target");
        String id = request.getParameter("id");
        ReturnHelper returnHelper;

        session = request.getSession();
        JSONObject jsObj = new JSONObject();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Artist artist = null;

        try {
            switch (target) {
                case "ListGenreArtist":
                    List<ExploreHelper> genres = musicManagementBean.listAllGenreArtist();
                    session.setAttribute("genres", genres);
                    jsObj.put("result", true);
                    response.getWriter().write(jsObj.toString());
                    return;

                case "GetArtistByID":
                    artist = adminManagementBean.getArtist(Long.parseLong(id));
                    session.setAttribute("artistDetails", artist);
                    jsObj.put("result", true);
                    response.getWriter().write(jsObj.toString());
                    return;
                case "Search":
                    String searchText = request.getParameter("text");
                    SearchHelper result = musicManagementBean.search(searchText);
                    session.setAttribute("searchResult", result);
                    jsObj.put("result", true);
                    response.getWriter().write(jsObj.toString());
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
