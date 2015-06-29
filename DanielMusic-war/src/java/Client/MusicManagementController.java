package Client;

import EntityManager.Album;
import EntityManager.Artist;
import EntityManager.Band;
import EntityManager.Genre;
import EntityManager.Music;
import EntityManager.ReturnHelper;
import SessionBean.AdminManagement.AdminManagementBeanLocal;
import SessionBean.CommonInfrastructure.CommonInfrastructureBeanLocal;
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
    private CommonInfrastructureBeanLocal commonInfrastructureBean;

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
        String lyrics = request.getParameter("lyrics");
        String yearReleased = request.getParameter("yearReleased");
        String trackNumber = request.getParameter("trackNumber");
        String credits = request.getParameter("credits");
        String price = request.getParameter("price");

        int intTrackNumber = 0;
        if (trackNumber != null && !trackNumber.isEmpty()) {
            intTrackNumber = Integer.parseInt(trackNumber);
        }

        session = request.getSession();
        Artist artist = (Artist) (session.getAttribute("artist"));
        Band band = (Band) (session.getAttribute("band"));
        List<Music> tracks = null;
        Album album = null;

        ReturnHelper returnHelper;

        try {
            switch (target) {
                case "ListAlbumByID":
                    if (artist != null && id != null) {
                        session.setAttribute("album", musicManagementBean.getAlbum(Long.parseLong(id)));
                        nextPage = "#!/artist/edit_album";
                    }
                    break;

                case "AddAlbum":
                    if (artist != null && yearReleased != null && price != null) {
                        Part picture = request.getPart("picture");

                        if (picture.getSize() == 0) {
                            picture = null;
                        }

                        returnHelper = musicManagementBean.createAlbum(picture, name, description, artist.getId(), Integer.parseInt(yearReleased), credits, Double.parseDouble(price));

                        if (returnHelper.getResult()) {
                            session.setAttribute("albums", musicManagementBean.ListAllAlbumByArtistorBandID(artist.getId(), true, true));
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                        }
                        nextPage = "#!/artist/albums";
                    }
                    break;

                case "UpdateAlbum":
                    if (artist != null && yearReleased != null && price != null) {
                        Part picture = request.getPart("picture");

                        if (picture.getSize() == 0) {
                            picture = null;
                        }

                        returnHelper = musicManagementBean.editAlbum(Long.parseLong(id), picture, name, description, Integer.parseInt(yearReleased), credits, Double.parseDouble(price));

                        if (returnHelper.getResult()) {
                            session.setAttribute("albums", musicManagementBean.ListAllAlbumByArtistorBandID(artist.getId(), true, true));
                            session.setAttribute("album", musicManagementBean.getAlbum(Long.parseLong(id)));
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                        }
                        nextPage = "#!/artist/edit_album";
                    }
                    break;

                case "PublishAlbum":
                    if (artist != null) {
                        returnHelper = musicManagementBean.publishAlbum(Long.parseLong(id));
                        System.out.println("returnHelper.getResult() " + returnHelper.getResult());

                        if (returnHelper.getResult()) {
                            session.setAttribute("albums", musicManagementBean.ListAllAlbumByArtistorBandID(artist.getId(), true, true));
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                        }
                        nextPage = "#!/artist/albums";
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

                case "ListTrackByID":
                    if (artist != null && id != null) {
                        Music track = musicManagementBean.getMusic(Long.parseLong(id));
                        session.setAttribute("track", track);
                        session.setAttribute("URL_128", commonInfrastructureBean.getMusicFileURLFromGoogleCloudStorage(track.getFileLocation128()));
                        session.setAttribute("URL_320", commonInfrastructureBean.getMusicFileURLFromGoogleCloudStorage(track.getFileLocation320()));
                        session.setAttribute("URL_Wav", commonInfrastructureBean.getMusicFileURLFromGoogleCloudStorage(track.getFileLocationWAV()));
                        nextPage = "#!/artist/edit_track";
                    }
                    break;

                case "AddTrack":
                    album = (Album) (session.getAttribute("album"));
                    if (artist != null && yearReleased != null && album != null) {
                        Part music = request.getPart("music");
                        if (music.getSize() == 0) {
                            music = null;
                        }

                        returnHelper = musicManagementBean.createMusic(music, album.getId(), intTrackNumber, name, 0.0, lyrics, Integer.parseInt(yearReleased));
                        if (returnHelper.getResult()) {
                            tracks = musicManagementBean.ListAllTracksByAlbumID(album.getId());
                            if (tracks == null) {
                                nextPage = "error500.html";
                            } else {
                                session.setAttribute("tracks", tracks);
                            }
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                        }
                        nextPage = "#!/artist/tracks";
                    }
                    break;

                case "EditTrack":
                    album = (Album) (session.getAttribute("album"));
                    Music track = (Music) (session.getAttribute("track"));

                    Part music = request.getPart("music");
                    if (music.getSize() == 0) {
                        music = null;
                    }

                    returnHelper = musicManagementBean.editMusic(track.getId(), intTrackNumber, name, Double.parseDouble(price), lyrics, credits);
                    if (returnHelper.getResult()) {
                        tracks = musicManagementBean.ListAllTracksByAlbumID(album.getId());
                        if (tracks == null) {
                            nextPage = "error500.html";
                        } else {
                            session.setAttribute("tracks", tracks);
                            session.setAttribute("track", musicManagementBean.getMusic(track.getId()));
                        }
                        session.setAttribute("goodMsg", returnHelper.getDescription());
                    } else {
                        session.setAttribute("errMsg", returnHelper.getDescription());
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
