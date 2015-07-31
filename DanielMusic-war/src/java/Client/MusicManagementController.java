package Client;

import EntityManager.Account;
import EntityManager.Album;
import EntityManager.Artist;
import EntityManager.CheckoutHelper;
import EntityManager.DownloadHelper;
import EntityManager.Music;
import EntityManager.Payment;
import EntityManager.ReturnHelper;
import EntityManager.ShoppingCart;
import SessionBean.AdminManagement.AdminManagementBeanLocal;
import SessionBean.ClientManagement.ClientManagementBeanLocal;
import SessionBean.MusicManagement.MusicManagementBeanLocal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
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
public class MusicManagementController extends HttpServlet {

    @EJB
    private ClientManagementBeanLocal clientManagementBean;

    @EJB
    private AdminManagementBeanLocal adminManagementBean;

    @EJB
    private MusicManagementBeanLocal musicManagementBean;

    String nextPage = "", goodMsg = "", errMsg = "";
    HttpSession session;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String target = request.getParameter("target");
        String source = request.getParameter("source");

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String lyrics = request.getParameter("lyrics");
        String yearReleased = request.getParameter("yearReleased");
        String trackNumber = request.getParameter("trackNumber");
        String credits = request.getParameter("credits");
        String price = request.getParameter("price");
        String genreID = request.getParameter("genre");
        Long albumID, trackID;
        ShoppingCart shoppingCart = null;
        int deleteCounter;

        int intTrackNumber = 0;
        if (trackNumber != null && !trackNumber.isEmpty()) {
            intTrackNumber = Integer.parseInt(trackNumber);
        }

        session = request.getSession();
        Artist artist = (Artist) session.getAttribute("artist");
        Account account = (Account) session.getAttribute("account");
        List<Music> tracks = null;
        Album album = null;
        Music track = null;

        ReturnHelper returnHelper = new ReturnHelper();

        JSONObject jsObj = new JSONObject();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            switch (target) {
                case "ListAlbumByID":
                    if (artist != null && id != null) {
                        session.setAttribute("album", musicManagementBean.getAlbum(Long.parseLong(id)));
                        nextPage = "#!/artist/edit_album";
                    }
                    break;

                case "RetrieveSingle":
                    if (artist != null && id != null) {
                        album = musicManagementBean.getAlbum(Long.parseLong(id));
                        session.setAttribute("album", album);
                        if (source != null && source.equals("editSinglePrice")) {
                            nextPage = "#!/artist/edit_singles";
                        } else {
                            session.setAttribute("URL_128", musicManagementBean.generateDownloadLink(album.getListOfMusics().get(0).getId(), "128", false, 300L));
                            session.setAttribute("URL_320", musicManagementBean.generateDownloadLink(album.getListOfMusics().get(0).getId(), "320", false, 300L));
                            session.setAttribute("URL_Wav", musicManagementBean.generateDownloadLink(album.getListOfMusics().get(0).getId(), "wav", false, 300L));
                            nextPage = "#!/artist/singles";
                        }
                    }
                    break;

                case "AddAlbum":
                    if (artist != null && yearReleased != null && price != null) {
                        Part picture = request.getPart("picture");
                        if (picture.getSize() == 0) {
                            picture = null;
                        }

                        returnHelper = musicManagementBean.createAlbum(false, picture, name, Long.parseLong(genreID), description, artist.getId(), Integer.parseInt(yearReleased), credits, Double.parseDouble(price));

                        if (returnHelper.getResult()) {
                            session.setAttribute("albums", musicManagementBean.listAllAlbumByArtistOrBandID(artist.getId(), true, true));
                            session.setAttribute("goodMsg", returnHelper.getDescription());
                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                        }
                        nextPage = "#!/artist/albums";
                    }
                    break;

                case "AddSingles":
                    if (artist != null && yearReleased != null && price != null) {
                        Part picture = request.getPart("picture");
                        if (picture.getSize() == 0) {
                            picture = null;
                        }

                        Part music = request.getPart("music");
                        if (music.getSize() == 0) {
                            music = null;
                        }

                        returnHelper = musicManagementBean.createAlbum(true, picture, name, Long.parseLong(genreID), description, artist.getId(), Integer.parseInt(yearReleased), credits, Double.parseDouble(price));

                        if (returnHelper.getResult()) {
                            musicManagementBean.createMusic(music, returnHelper.getID(), null, name, Double.parseDouble(price), lyrics, Integer.parseInt(yearReleased));
                            if (returnHelper.getResult()) {
                                session.setAttribute("albums", musicManagementBean.listAllAlbumByArtistOrBandID(artist.getId(), true, true));
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                            }
                        } else {
                            session.setAttribute("errMsg", returnHelper.getDescription());
                        }
                        nextPage = "#!/artist/albums";
                    }
                    break;

                case "UpdateAlbum":
                    if (artist != null && yearReleased != null && price != null) {
                        if (!musicManagementBean.checkIfAlbumBelongsToArtist(artist.getId(), Long.parseLong(id))) {
                            session.setAttribute("errMsg", "Unauthorized action");
                        } else {
                            Part picture = request.getPart("picture");

                            if (picture.getSize() == 0) {
                                picture = null;
                            }

                            returnHelper = musicManagementBean.editAlbum(Long.parseLong(id), picture, name, Long.parseLong(genreID), description, Integer.parseInt(yearReleased), credits, Double.parseDouble(price));

                            if (returnHelper.getResult()) {
                                session.setAttribute("albums", musicManagementBean.listAllAlbumByArtistOrBandID(artist.getId(), true, true));
                                session.setAttribute("album", musicManagementBean.getAlbum(Long.parseLong(id)));
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                            }
                        }
                        nextPage = "#!/artist/edit_album";
                    }
                    break;

                case "EditSinglePrice":
                    if (artist != null && price != null) {
                        album = (Album) (session.getAttribute("album"));
                        if (!musicManagementBean.checkIfAlbumBelongsToArtist(artist.getId(), album.getId())) {
                            session.setAttribute("errMsg", "Unauthorized action");
                        } else {

                            returnHelper = musicManagementBean.editAlbumPrice(album.getId(), Double.parseDouble(price));

                            if (returnHelper.getResult()) {
                                session.setAttribute("albums", musicManagementBean.listAllAlbumByArtistOrBandID(artist.getId(), true, true));
                                session.setAttribute("album", musicManagementBean.getAlbum(album.getId()));
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                            }
                        }
                        nextPage = "#!/artist/edit_singles";
                    }
                    break;

                case "DeleteAlbum":
                    if (artist != null) {
                        if (!musicManagementBean.checkIfAlbumBelongsToArtist(artist.getId(), Long.parseLong(id))) {
                            session.setAttribute("errMsg", "Unauthorized action");
                        } else {
                            returnHelper = musicManagementBean.deleteAlbum(Long.parseLong(id));

                            if (returnHelper.getResult()) {
                                session.setAttribute("albums", musicManagementBean.listAllAlbumByArtistOrBandID(artist.getId(), true, true));
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                            }
                            nextPage = "#!/artist/albums";
                        }
                    }
                    break;

                case "PublishAlbum":
                    if (artist != null) {
                        if (!musicManagementBean.checkIfAlbumBelongsToArtist(artist.getId(), Long.parseLong(id))) {
                            session.setAttribute("errMsg", "Unauthorized action");
                        } else {
                            returnHelper = musicManagementBean.publishAlbum(Long.parseLong(id));
                            if (returnHelper.getResult()) {
                                session.setAttribute("albums", musicManagementBean.listAllAlbumByArtistOrBandID(artist.getId(), true, true));
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                            }
                            nextPage = "#!/artist/albums";
                        }
                    }
                    break;

                case "ListAllTracksByAlbumID":
                    tracks = musicManagementBean.listAllTracksByAlbumID(Long.parseLong(id));
                    if (tracks == null) {
                        nextPage = "error500.html";
                    } else {
                        session.setAttribute("album", musicManagementBean.getAlbum(Long.parseLong(id)));
                        session.setAttribute("tracks", tracks);
                        if (artist != null) {
                            nextPage = "#!/artist/tracks";
                        }
                    }

                    break;

                case "ListTrackByID":
                    if (artist != null && id != null) {
                        track = musicManagementBean.getMusic(Long.parseLong(id));
                        session.setAttribute("track", track);

                        if (source != null && source.equals("editMusicPrice")) {
                            nextPage = "#!/artist/edit_track";
                        } else {
                            session.setAttribute("URL_128", musicManagementBean.generateDownloadLink(Long.parseLong(id), "128", false, 300L));
                            session.setAttribute("URL_320", musicManagementBean.generateDownloadLink(Long.parseLong(id), "320", false, 300L));
                            session.setAttribute("URL_Wav", musicManagementBean.generateDownloadLink(Long.parseLong(id), "wav", false, 300L));
                            nextPage = "#!/artist/track";
                        }
                    }
                    break;

                case "EditTrackPrice":
                    if (artist != null && price != null) {
                        track = (Music) (session.getAttribute("track"));
                        if (!musicManagementBean.checkIfMusicBelongsToArtist(artist.getId(), track.getId())) {
                            session.setAttribute("errMsg", "Unauthorized action");
                        } else {

                            returnHelper = musicManagementBean.editMusicPrice(track.getId(), Double.parseDouble(price));

                            if (returnHelper.getResult()) {
                                session.setAttribute("albums", musicManagementBean.listAllAlbumByArtistOrBandID(artist.getId(), true, true));
                                session.setAttribute("track", musicManagementBean.getMusic(track.getId()));
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                            }
                        }
                        nextPage = "#!/artist/edit_track";
                    }
                    break;

                case "AddTrack":
                    album = (Album) (session.getAttribute("album"));
                    if (artist != null && yearReleased != null && album != null && price != null) {
                        if (!musicManagementBean.checkIfAlbumBelongsToArtist(artist.getId(), album.getId())) {
                            session.setAttribute("errMsg", "Unauthorized action");
                        } else {
                            Part music = request.getPart("music");
                            if (music.getSize() == 0) {
                                music = null;
                            }

                            returnHelper = musicManagementBean.createMusic(music, album.getId(), intTrackNumber, name, Double.parseDouble(price), lyrics, Integer.parseInt(yearReleased));
                            if (returnHelper.getResult()) {
                                tracks = musicManagementBean.listAllTracksByAlbumID(album.getId());
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
                    }
                    break;

                case "DeleteTrack":
                    if (artist != null) {
                        if (!musicManagementBean.checkIfMusicBelongsToArtist(artist.getId(), Long.parseLong(id))) {
                            session.setAttribute("errMsg", "Unauthorized action");
                        } else {
                            album = (Album) (session.getAttribute("album"));
                            returnHelper = musicManagementBean.deleteMusic(Long.parseLong(id));

                            if (returnHelper.getResult()) {
                                session.setAttribute("albums", musicManagementBean.listAllAlbumByArtistOrBandID(artist.getId(), true, true));
                                session.setAttribute("tracks", musicManagementBean.listAllTracksByAlbumID(album.getId()));
                                session.setAttribute("goodMsg", returnHelper.getDescription());
                            } else {
                                session.setAttribute("errMsg", returnHelper.getDescription());
                            }
                            nextPage = "#!/artist/tracks";
                        }
                    }
                    break;

                case "GetShoppingCart":
                    account = (Account) session.getAttribute("account");
                    shoppingCart = (ShoppingCart) session.getAttribute("ShoppingCart");
                    if (account != null) {
                        //retrieve from server
                        shoppingCart = clientManagementBean.getShoppingCart(account.getId());
                    }
                    jsObj.put("result", true);
                    jsObj.put("goodMsg", "Cart");
                    response.getWriter().write(jsObj.toString());
                    session.setAttribute("ShoppingCart", shoppingCart);
                    return;

                case "RemoveAlbumFromShoppingCart":
                    account = (Account) session.getAttribute("account");
                    String strAlbumIDs = request.getParameter("deleteAlbum");
                    Scanner sc = new Scanner(strAlbumIDs);
                    ArrayList<String> albumIDs = new ArrayList<>();
                    while (sc.hasNext()) {
                        albumIDs.add(sc.next());
                    }

                    deleteCounter = 0;
                    if (account != null) {
                        for (String s : albumIDs) {
                            returnHelper = clientManagementBean.removeItemFromShoppingCart(account.getId(), Long.parseLong(s), false);
                            if (returnHelper.getResult()) {
                                deleteCounter++;
                            }
                        }
                        shoppingCart = clientManagementBean.getShoppingCart(account.getId());
                    } else {
                        shoppingCart = (ShoppingCart) session.getAttribute("ShoppingCart");
                        for (String s : albumIDs) {
                            album = new Album();
                            album.setId(Long.parseLong(s));
                            Boolean result = shoppingCart.getListOfAlbums().remove(album);
                            if (result) {
                                deleteCounter++;
                            }
                        }
                    }

                    if (deleteCounter > 0) {
                        jsObj.put("result", true);
                        jsObj.put("goodMsg", "Deleted " + deleteCounter + " records successfully.");
                        response.getWriter().write(jsObj.toString());
                    } else {
                        jsObj.put("result", false);
                        jsObj.put("errMsg", "No records were deleted.");
                        response.getWriter().write(jsObj.toString());
                    }
                    session.setAttribute("redirectPage", "#!/cart");
                    session.setAttribute("ShoppingCart", shoppingCart);
                    return;

                case "RemoveTrackFromShoppingCart":
                    account = (Account) session.getAttribute("account");
                    String strTrackIDs = request.getParameter("deleteTrack");
                    Scanner sc2 = new Scanner(strTrackIDs);
                    ArrayList<String> trackIDs = new ArrayList<>();
                    while (sc2.hasNext()) {
                        trackIDs.add(sc2.next());
                    }

                    deleteCounter = 0;
                    if (account != null) {
                        for (String s : trackIDs) {
                            returnHelper = clientManagementBean.removeItemFromShoppingCart(account.getId(), Long.parseLong(s), true);
                            if (returnHelper.getResult()) {
                                deleteCounter++;
                            }
                        }
                        shoppingCart = clientManagementBean.getShoppingCart(account.getId());
                    } else {
                        shoppingCart = (ShoppingCart) session.getAttribute("ShoppingCart");
                        for (String s : trackIDs) {
                            track = new Music();
                            track.setId(Long.parseLong(s));
                            Boolean result = shoppingCart.getListOfMusics().remove(track);
                            if (result) {
                                deleteCounter++;
                            }
                        }
                    }

                    if (deleteCounter > 0) {
                        jsObj.put("result", true);
                        jsObj.put("goodMsg", "Deleted " + deleteCounter + " records successfully.");
                        response.getWriter().write(jsObj.toString());
                    } else {
                        jsObj.put("result", false);
                        jsObj.put("errMsg", "No records were deleted.");
                        response.getWriter().write(jsObj.toString());
                    }

                    session.setAttribute("redirectPage", "#!/cart");
                    session.setAttribute("ShoppingCart", shoppingCart);
                    return;

                case "AddAlbumToShoppingCart":
                    account = (Account) session.getAttribute("account");
                    albumID = Long.parseLong(request.getParameter("albumID"));
                    if (clientManagementBean.checkArtistPayPalEmailExists(albumID, false)) {
                        if (account != null) {
                            returnHelper = clientManagementBean.addItemToShoppingCart(account.getId(), albumID, false);
                            if (returnHelper.getResult()) {
                                jsObj.put("result", true);
                                jsObj.put("goodMsg", returnHelper.getDescription());
                                response.getWriter().write(jsObj.toString());
                            } else {
                                jsObj.put("result", false);
                                jsObj.put("errMsg", returnHelper.getDescription());
                                response.getWriter().write(jsObj.toString());
                            }

                            shoppingCart = clientManagementBean.getShoppingCart(account.getId());
                        } else {
                            shoppingCart = (ShoppingCart) session.getAttribute("ShoppingCart");
                            album = musicManagementBean.getAlbum(albumID);
                            Boolean result;
                            if (shoppingCart == null) {
                                shoppingCart = new ShoppingCart();

                                HashSet<Album> albumSet = new HashSet<Album>();
                                result = albumSet.add(album);
                                HashSet<Music> musicSet = new HashSet<Music>();

                                shoppingCart.setListOfAlbums(albumSet);
                                shoppingCart.setListOfMusics(musicSet);
                            } else {
                                Set set = shoppingCart.getListOfAlbums();
                                result = set.add(album);
                                shoppingCart.setListOfAlbums(set);
                            }

                            if (result) {
                                jsObj.put("result", true);
                                jsObj.put("goodMsg", "Item has been added to cart successfully.");
                                response.getWriter().write(jsObj.toString());
                            } else {
                                jsObj.put("result", false);
                                jsObj.put("errMsg", "Failed to add item to cart, please try again. Note that duplicate item cannot be added.");
                                response.getWriter().write(jsObj.toString());
                            }
                        }
                    } else {
                        jsObj.put("result", false);
                        jsObj.put("errMsg", "Sorry, the track is currently unavailable.");
                        response.getWriter().write(jsObj.toString());
                    }
                    session.setAttribute("ShoppingCart", shoppingCart);
                    return;

                case "AddTrackToShoppingCart":
                    account = (Account) session.getAttribute("account");
                    trackID = Long.parseLong(request.getParameter("trackID"));
                    if (clientManagementBean.checkArtistPayPalEmailExists(trackID, true)) {
                        if (account != null) {
                            returnHelper = clientManagementBean.addItemToShoppingCart(account.getId(), trackID, true);
                            if (returnHelper.getResult()) {
                                jsObj.put("result", true);
                                jsObj.put("goodMsg", returnHelper.getDescription());
                                response.getWriter().write(jsObj.toString());
                            } else {
                                jsObj.put("result", false);
                                jsObj.put("errMsg", returnHelper.getDescription());
                                response.getWriter().write(jsObj.toString());
                            }

                            shoppingCart = clientManagementBean.getShoppingCart(account.getId());
                        } else {
                            shoppingCart = (ShoppingCart) session.getAttribute("ShoppingCart");
                            track = musicManagementBean.getMusic(trackID);
                            Boolean result;
                            if (shoppingCart == null) {
                                shoppingCart = new ShoppingCart();

                                HashSet<Album> albumSet = new HashSet<Album>();
                                HashSet<Music> musicSet = new HashSet<Music>();
                                result = musicSet.add(track);

                                shoppingCart.setListOfAlbums(albumSet);
                                shoppingCart.setListOfMusics(musicSet);
                            } else {
                                Set set = shoppingCart.getListOfMusics();
                                result = set.add(track);
                                shoppingCart.setListOfMusics(set);
                            }

                            if (result) {
                                jsObj.put("result", true);
                                jsObj.put("goodMsg", "Item has been added to cart successfully.");
                                response.getWriter().write(jsObj.toString());
                            } else {
                                jsObj.put("result", false);
                                jsObj.put("errMsg", "Failed to add item to cart, please try again. Note that duplicate item cannot be added.");
                                response.getWriter().write(jsObj.toString());
                            }
                        }
                    } else {
                        jsObj.put("result", false);
                        jsObj.put("errMsg", "Sorry, the track is currently unavailable.");
                        response.getWriter().write(jsObj.toString());
                    }
                    session.setAttribute("ShoppingCart", shoppingCart);
                    return;

                case "GetArtistByID":
                    if (artist != null) {
                        artist = adminManagementBean.getArtist(Long.parseLong(id));
                        session.setAttribute("artistDetails", artist);
                        List<Album> albums = musicManagementBean.listAllAlbumByArtistOrBandID(Long.parseLong(id), false, false);
                        session.setAttribute("artistAlbumDetails", albums);
                        nextPage = "#!/artists";
                    }
                    break;

                case "Checkout":
                    shoppingCart = (ShoppingCart) session.getAttribute("ShoppingCart");
                    account = (Account) session.getAttribute("account");
                    CheckoutHelper checkoutHelper = null;

                    if (account != null) {
                        shoppingCart = clientManagementBean.getShoppingCart(account.getId());
                        Set<Music> musicSet = shoppingCart.getListOfMusics();
                        Set<Album> albumSet = shoppingCart.getListOfAlbums();
                        checkoutHelper = clientManagementBean.getPayKey(account.getId(), null, musicSet, albumSet);
                    } else {
                        Set<Music> musicSet = shoppingCart.getListOfMusics();
                        Set<Album> albumSet = shoppingCart.getListOfAlbums();
                        String email = request.getParameter("email");
                        checkoutHelper = clientManagementBean.getPayKey(null, email, musicSet, albumSet);
                    }
                    if (checkoutHelper == null) {
                        session.setAttribute("errMsg", "Internal server error.");
                        jsObj.put("result", false);
                        jsObj.put("errMsg", "Internal server error.");
                        response.getWriter().write(jsObj.toString());
                        return;
                    }
                    session.setAttribute("checkoutHelper", checkoutHelper);
                    if (checkoutHelper.getPayKey() == null) {
                        session.setAttribute("errMsg", "Your cart contains items that are ineligible for checkout as one or more of the artist has not verified their PayPal account yet, please try again later.");
                        jsObj.put("result", false);
                        jsObj.put("errMsg", "Your cart contains items that are ineligible for checkout as one or more of the artist has not verified their PayPal account yet, please try again later.");
                        response.getWriter().write(jsObj.toString());
                        return;
                    }
                    switch (checkoutHelper.getPayKey()) {
                        case "NO_PAYMENT_REQUIRED_PAYMENT_COMPLETE":
                            //todo this one should redirect after going to checkout.jsp
                            session.setAttribute("goodMsg", "Thanks.");
                            jsObj.put("result", true);
                            jsObj.put("goodMsg", "Checkout completed, please wait, redirecting...");
                            return;
                        case "NO_PAYMENT_REQUIRED_FAILED":
                            session.setAttribute("errMsg", "There was an error completing the checkout request, please try again later.");
                            jsObj.put("result", false);
                            jsObj.put("errMsg", "There was an error completing the checkout request, please try again later.");
                            response.getWriter().write(jsObj.toString());
                            return;
                    }
                    jsObj.put("result", true);
                    jsObj.put("goodMsg", "Please verify the following payment details.");
                    response.getWriter().write(jsObj.toString());
                    //goto checkuot.jsp
                    return;
                case "CompletePayment":
                    String paymentID = (String) request.getParameter("paymentID");
                    String UUID = (String) request.getParameter("UUID");
                    Long paymentIDlong = Long.parseLong(paymentID);
                    ReturnHelper result = clientManagementBean.completePayment(paymentIDlong, UUID);
                    if (!result.getResult()) {//fail to complete payment
                        session.setAttribute("errMsg", result.getDescription());
                        session.setAttribute("redirectPage", "#!/checkout");
                        nextPage = "redirect2.jsp";
                    } else {
                        Payment payment = clientManagementBean.getPayment(result.getID());
                        if (payment.getAccount() != null) {
                            //Logged in user will view list of purchaserd music
                            session.setAttribute("redirectPage", "#!/fan/profile");
                            nextPage = "redirect2.jsp";
                        } else {
                            //Payment not tied to account will access download page directly
//                            DownloadHelper downloadHelper = clientManagementBean.getPurchaseDownloadLinks(payment.getId());
//                            session.setAttribute("downloadLinks", downloadHelper);
                            session.setAttribute("redirectPage", "#!/download-links");
                            nextPage = "redirect2.jsp";
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
