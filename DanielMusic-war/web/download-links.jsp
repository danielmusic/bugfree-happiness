<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Sound.sg">
    <%@page import="java.util.List"%>
    <%@page import="java.util.ArrayList"%>
    <%@page import="EntityManager.Album"%>
    <%@page import="EntityManager.DownloadHelper"%>
    <%@page import="EntityManager.Music"%>
    <%@page import="EntityManager.Artist"%>
    <%@page import="EntityManager.Account"%>
    <%@page import="java.text.NumberFormat"%>
    <%
        DownloadHelper downloadHelper = (DownloadHelper) (session.getAttribute("downloadLinks"));
        if (downloadHelper == null) {
            out.print("<p class='warning'>Ops. No results found.</p>");
    %>

    <%
    } else {
    %>
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/artist-single-bg.jpg)">
        <h2 class="heading-l">Music <span class="color">Downloads</span></h2>
        <br>
        <span class="overlay grids"></span>
    </section>
    <section class="content section">
        <div class="container">
            <div id="main" class="release main-left main-medium">
                <article>
                    <%
                        List<Music> purchasedMusic = downloadHelper.getPurchasedMusics();
                        List<String> downloadLinks128 = downloadHelper.getDownloadLinks128();
                        List<String> downloadLinks320 = downloadHelper.getDownloadLinks320();
                        List<String> downloadLinksWav = downloadHelper.getDownloadLinksWav();
                        if (purchasedMusic == null) {
                            purchasedMusic = new ArrayList();
                        }
                        if (downloadLinks128 == null) {
                            downloadLinks128 = new ArrayList();
                        }
                        if (downloadLinks320 == null) {
                            downloadLinks320 = new ArrayList();
                        }
                        if (downloadLinksWav == null) {
                            downloadLinksWav = new ArrayList();
                        }

                    %>
                    <ul id="release-list" class="tracklist">
                        <% for (int i = 0; i < purchasedMusic.size(); i++) {
                                Music music = purchasedMusic.get(i);
                                Album album = music.getAlbum();
                                String albumArt = album.getImageLocation();
                                if (albumArt == null || albumArt.isEmpty()) {
                                    albumArt = "/img/cover.png";
                                } else {
                                    albumArt = "http://sounds.sg.storage.googleapis.com/" + albumArt;
                                }
                        %>

                        <div class="toggle">
                            <li>
                                <div class="track-details">
                                    <a class="track sp-play-track toggle-title">
                                        <!-- cover -->
                                        <img class="track-cover" src="<%=albumArt%>">
                                        <!-- Title -->
                                        <span class="track-title" data-artist_url="artist_url"><%=music.getName()%></span>
                                        <!-- Artists -->
                                    </a>
                                    <div class="track-buttons" style="margin-top: 5px; margin-bottom: 5px;">
                                        <a class="track sp-play-track toggle-title">
                                            <i class="icon icon-play2"><span style='display: none;'><%=music.getName()%></span></i>
                                        </a>
                                        <i style="cursor: pointer;" class="icon icon-menu2 toggle-title"><span style='display: none;'></span></i>
                                    </div>
                                </div>

                            </li>
                            <div class="toggle-content">
                                <a href="">Download .mp3 (128kbps)</a>
                                <a href="">Download .mp3 (320kbps)</a>
                                <a href="">Download .wav</a>
                                <br/><br/><a style="cursor: pointer" onclick="window.open('./MusicController?target=Lyrics&id=<%=music.getId()%>', '_blank', 'width=600,height=760')">Open in new window</a>
                            </div>

                        </div>


                        <%}%>                                    
                    </ul>
                    <%}%>
                    <p>
                        <a href="javascript:;" class="btn invert sp-play-list" data-id="release-list">Play All Tracks</a>
                        <a href="javascript:;" class="btn sp-add-list" data-id="release-list">Add All Tracks</a>
                    </p>
                </article>
            </div>
        </div>

        <div class="md-overlay"></div>
        <script>
            function addTrackToCart(trackID) {
                url = "./MusicManagementController?target=AddTrackToShoppingCart";
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    data: {'trackID': trackID},
                    success: function (val) {
                        window.event.returnValue = false;
                        window.location.href = "#!/cart";
                    },
                    error: function (xhr, status, error) {
                        document.getElementById("errMsg").style.display = "block";
                        document.getElementById('errMsg').innerHTML = error;
                        hideLoader();
                        ajaxResultsError(xhr, status, error);
                    }
                });
            }

            function addAlbumToCart(albumID) {
                url = "./MusicManagementController?target=AddAlbumToShoppingCart";
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    data: {'albumID': albumID},
                    success: function (val) {
                        window.event.returnValue = false;
                        window.location.href = "#!/cart";
                    },
                    error: function (xhr, status, error) {
                        document.getElementById("errMsg").style.display = "block";
                        document.getElementById('errMsg').innerHTML = error;
                        hideLoader();
                        ajaxResultsError(xhr, status, error);
                    }
                });
            }
        </script>
        <script src="js/classie.js"></script>
        <script src="js/modalEffects.js"></script>
        <script>var polyfilter_scriptpath = '/DanielMusic-war/js/';</script> 
        <script src="js/cssParser.js"></script>
        <script src="js/css-filters-polyfill.js"></script>
    </section>
    <%}%>
</section>
