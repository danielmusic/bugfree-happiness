package Client;

import EntityManager.Album;
import EntityManager.Artist;
import EntityManager.ExploreHelper;
import EntityManager.Music;
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
        System.out.println("Welcome to MusicController");
        String target = request.getParameter("target");
        String id = request.getParameter("id");

        session = request.getSession();
        JSONObject jsObj = new JSONObject();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Artist artist = null;

        try {
            switch (target) {
                case "GrabASong":
                    if (true) {
                        System.out.println("GrabASong ");
                        List<ExploreHelper> genres = musicManagementBean.listAllGenreArtist();
                        session.setAttribute("genres", genres);
                        jsObj.put("result", true);
                        response.getWriter().write(jsObj.toString());
                    }
                    return;

                case "ListGenreArtist":
                    if (true) {
                        List<ExploreHelper> genres = musicManagementBean.listAllGenreArtist();
                        session.setAttribute("genres", genres);
                        jsObj.put("result", true);
                        response.getWriter().write(jsObj.toString());
                    }
                    return;

                case "GetArtistByID":
                    if (true) {
                        artist = adminManagementBean.getArtist(Long.parseLong(id));
                        session.setAttribute("artistDetails", artist);
                        List<Album> albums = musicManagementBean.listAllAlbumByArtistOrBandID(Long.parseLong(id), false, false);
                        session.setAttribute("artistAlbumDetails", albums);
                        jsObj.put("result", true);
                        response.getWriter().write(jsObj.toString());
                        return;
                    }

                case "GetAlbumByID":
                    if (true) {
                        Album album = musicManagementBean.getAlbum(Long.parseLong(id));
                        artist = album.getArtist();
                        session.setAttribute("artistDetails", artist);
                        List<Album> albums = musicManagementBean.listAllAlbumByArtistOrBandID(album.getArtist().getId(), false, false);
                        session.setAttribute("artistAlbumDetails", albums);
                        session.setAttribute("jumpToAlbumID", album.getId().toString());
                        jsObj.put("result", true);
                        response.getWriter().write(jsObj.toString());
                        return;
                    }

                case "Search":
                    if (true) {
                        String searchText = request.getParameter("text");
                        SearchHelper result = musicManagementBean.search(searchText);
                        session.setAttribute("searchResult", result);
                        session.setAttribute("redirectPage", "#!/search");
                        jsObj.put("result", true);
                        response.getWriter().write(jsObj.toString());
                        return;
                    }

                case "Lyrics":
                    if (true) {
                        Long musicID = Long.parseLong(request.getParameter("id"));
                        Music music = musicManagementBean.getMusic(musicID);
                        session.setAttribute("musicLyrics", music);
                        response.sendRedirect("#!/lyrics");
                        return;
                    }

                case "LinkToArtist":
                    if (true) {
                        String artistName = request.getParameter("artistName");
                        Long artistID = musicManagementBean.getArtistID(artistName);
                        if (artistID != null) {
                            artist = adminManagementBean.getArtist(artistID);
                            session.setAttribute("artistDetails", artist);
                            List<Album> albums = musicManagementBean.listAllAlbumByArtistOrBandID(artistID, false, false);
                            session.setAttribute("artistAlbumDetails", albums);
                            response.sendRedirect("#!/artists");
                        } else {
                            response.sendRedirect("#!/index");
                        }
                    }
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
