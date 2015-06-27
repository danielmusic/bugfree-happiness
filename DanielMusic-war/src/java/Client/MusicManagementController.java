package Client;

import EntityManager.Album;
import EntityManager.Artist;
import EntityManager.Band;
import EntityManager.Genre;
import EntityManager.Music;
import EntityManager.ReturnHelper;
import SessionBean.AdminManagement.AdminManagementBeanLocal;
import SessionBean.MusicManagement.MusicManagementBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@MultipartConfig
public class MusicManagementController extends HttpServlet {

    @EJB
    private AdminManagementBeanLocal adminManagementBean;

    @EJB
    private MusicManagementBeanLocal musicManagementBean;

    String nextPage = "", goodMsg = "", errMsg = "";
    HttpSession session;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Welcome to client Music Management controller");
        String target = request.getParameter("target");
        String source = request.getParameter("source");
        System.out.println("target " + target);
        System.out.println("source " + source);

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String yearReleased = request.getParameter("yearReleased");
        String trackNumber = request.getParameter("trackNumber");

        session = request.getSession();
        Artist artist = (Artist) (session.getAttribute("artist"));
        Band band = (Band) (session.getAttribute("band"));
        List<Genre> genres = null;
        List<Music> tracks = null;

        ReturnHelper returnHelper;

        try {
            switch (target) {
                case "AddAlbum":
                    if (source != null && source.equals("Artist") && yearReleased != null) {
                        if (artist != null) {
                            Part picture = request.getPart("picture");
                            System.out.println("picture>>>>>> " + picture);
                            returnHelper = musicManagementBean.createAlbum(picture, name, description, artist.getId(), Integer.parseInt(yearReleased));
                            if (returnHelper.getResult()) {
                                session.setAttribute("albums", musicManagementBean.ListAllAlbumByArtistorBandID(artist.getId(), true, true));
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                            }
                            nextPage = "#!/artist/albums";
                        }
                    } else if (source != null && source.equals("Band")) {

                    }
                    break;

                case "UpdateAlbum":
                    if (source != null && source.equals("Artist") && yearReleased != null) {
                        if (artist != null) {
                            Part picture = request.getPart("picture");
                            returnHelper = musicManagementBean.editAlbum(Long.parseLong(id), picture, name, description, Integer.parseInt(yearReleased));
                            if (returnHelper.getResult()) {
                                session.setAttribute("albums", musicManagementBean.ListAllAlbumByArtistorBandID(artist.getId(), true, true));
                                session.setAttribute("album", musicManagementBean.getAlbum(Long.parseLong(id)));
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                            }
                            nextPage = "#!/artist/edit_album";
                        }
                    } else if (source != null && source.equals("Band")) {

                    }
                    break;

                case "ListAllGenre":
                    genres = adminManagementBean.listAllGenres();
                    if (genres == null) {
                        nextPage = "error500.html";
                    } else {
                        session.setAttribute("genres", genres);
                        if (source != null) {
                            nextPage = source;
                        }
                    }
                    break;

                case "ListAllTracksByAlbumID":
                    tracks = musicManagementBean.ListAllTracksByAlbumID(Long.parseLong(id));
                    if (tracks == null) {
                        nextPage = "error500.html";
                    } else {
                        session.setAttribute("album", musicManagementBean.getAlbum(Long.parseLong(id)));
                        session.setAttribute("tracks", tracks);
                        if (source != null && source.equals("tracks")) {
                            nextPage = "#!/artist/tracks";
                        }
                    }
                    break;

                case "ListAlbumByID":
                    if (id != null && source != null) {
                        session.setAttribute("album", musicManagementBean.getAlbum(Long.parseLong(id)));
                        if (source.equals("edit_album")) {
                            nextPage = "#!/artist/edit_album";
                        }
                    }
                    break;

                case "AddTrack":
                    Album album = (Album) (session.getAttribute("album"));
                    if (source != null && source.equals("Artist") && yearReleased != null && album != null) {
                        if (artist != null) {
                            Part picture = request.getPart("picture");
                            System.out.println("picture>>>>>> " + picture);

                            int intTrackNumber = 0;
                            if (trackNumber == null) {
                                intTrackNumber = Integer.parseInt(trackNumber);
                            }

//                            returnHelper = musicManagementBean.createMusic(picture, album.getId(), intTrackNumber, name, description, artist.getId(), Integer.parseInt(yearReleased));
//                            if (returnHelper.getResult()) {
//                                session.setAttribute("albums", musicManagementBean.ListAllAlbumByArtistorBandID(artist.getId(), true, true));
//                                session.setAttribute("goodMsg", returnHelper.getDescription());
//                            } else {
//                                session.setAttribute("errMsg", returnHelper.getDescription());
//                            }
                            nextPage = "#!/artist/tracks";
                        }
                    } else if (source != null && source.equals("Band")) {

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
