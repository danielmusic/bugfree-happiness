<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Sound.sg">
    <%@page import="java.util.List"%>
    <%@page import="java.util.ArrayList"%>
    <%@page import="EntityManager.Album"%>
    <%@page import="EntityManager.Music"%>
    <%@page import="EntityManager.Artist"%>
    <%@page import="EntityManager.Account"%>
    <%@page import="java.text.NumberFormat"%>
    <%
        Artist artist = (Artist) session.getAttribute("artistDetails");
        List<Album> albums = (List<Album>) session.getAttribute("artistAlbumDetails");
        if (artist == null || albums == null) {
            out.print("<p class='warning'>Ops. No results found.</p>");
        } else {
            String jumpToAlbumID = (String) session.getAttribute("jumpToAlbumID");
            if (jumpToAlbumID != null && !jumpToAlbumID.isEmpty()) {
                System.out.println("INNN");
    %>
    <script>
        window.onload = function () {
            alert("WYVB");
            document.getElementById("album_<%=jumpToAlbumID%>").focus();
        }
    </script>
    <%
            session.removeAttribute("jumpToAlbumID");
        }
    %>
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/artist-single-bg.jpg)">
        <h2 class="heading-l">Artist <span class="color"><%=artist.getName()%></span></h2>
        <br>
        <span class="overlay grids"></span>
    </section>
    <section class="content section">
        <div class="container">

            <div class="sidebar main-left main-medium">
                <div class="widget details-widget">
                    <a style="cursor: default;" class="thumb-glitch">
                        <span class="img">
                            <%if (artist.getImageURL() != null && !artist.getImageURL().isEmpty()) {%>
                            <img src="http://danielmusictest.storage.googleapis.com/<%=artist.getImageURL()%>" />
                            <%} else {%>
                            <img src="placeholders/artist01.jpg" />
                            <%}%>
                        </span>
                    </a>
                    <div class="details-meta">
                        <ul class="details-list">
                            <li>
                                <span class="label">Name</span>
                                <div class="data"><b><%=artist.getName()%></b></div>
                            </li>
                            <li>
                                <span class="label">Genres</span>
                                <div class="data"><%if (artist.getGenre() != null) {
                                        out.print(artist.getGenre().getName());
                                    }%></div>
                            </li>
                        </ul>
                    </div>
                    <!-- Details Share -->
                    <div class="details-social-box">
                        <%if (artist.getFacebookURL() != null && !artist.getFacebookURL().isEmpty()) {%>
                        <a href="<%=artist.getFacebookURL()%>"><i class="icon icon-facebook"></i></a>
                            <%}%>

                        <%if (artist.getTwitterURL() != null && !artist.getTwitterURL().isEmpty()) {%>
                        <a href="<%=artist.getTwitterURL()%>"><i class="icon icon-twitter"></i></a>
                            <%}%>

                        <%if (artist.getInstagramURL() != null && !artist.getInstagramURL().isEmpty()) {%>
                        <a href="<%=artist.getInstagramURL()%>"><i class="icon icon-user"></i></a>
                            <%}%>

                        <%if (artist.getWebsiteURL() != null && !artist.getWebsiteURL().isEmpty()) {%>
                        <a href="<%=artist.getWebsiteURL()%>"><i class="icon icon-IE"></i></a>
                            <%}%>
                    </div>
                </div>
            </div>
            <div id="main" class="release main-left main-medium">
                <article>

                    <!-- tabs -->
                    <div class="tabs-wrap">
                        <!-- tabs navigation -->
                        <ul class="tabs">
                            <li><a href="#tab-bio" class="active-tab">Biography</a></li>
                            <li><a href="#tab-releases">Albums</a></li>
                        </ul>
                        <!-- /tabs navigation -->
                        <!-- tab content -->
                        <div id="tab-bio" class="tab-content">
                            <h2>Biography</h2>
                            <p>
                                <%
                                    if (artist.getBiography() != null && !artist.getBiography().isEmpty()) {
                                        String repl = artist.getBiography().replaceAll("\\r", "<br>");
                                        out.print(repl);
                                    }
                                %>
                            </p>
                        </div>
                        <!-- /tab content -->
                        <!-- tab content -->
                        <div id="tab-releases" class="tab-content">
                            <%
                                for (int i = 0; i < albums.size(); i++) {
                                    Album album = albums.get(i);
                                    String albumArt = album.getImageLocation();
                                    if (albumArt == null || albumArt.isEmpty()) {
                                        albumArt = "/img/cover.png";
                                    } else {
                                        albumArt = "http://danielmusictest.storage.googleapis.com/" + albumArt;
                                    }
                            %>
                            <h2 id="album_<%=album.getId()%>"><%=album.getName()%></h2>
                            <p>
                                <%
                                    if (album.getDescription() != null && !album.getDescription().isEmpty()) {
                                        String repl = album.getDescription().replaceAll("\\r", "<br>");
                                        out.print(repl);
                                    }
                                %>
                            </p>
                            <p>
                                Price: 
                                <%
                                    if (album.getPrice() == 0.0) {
                                        out.print("Free");
                                    } else {
                                        NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                        out.print(formatter.format(album.getPrice()));
                                    }
                                %>
                            </p>
                            <p>Year Released: <%=album.getYearReleased()%></p>
                            <%if (album.getListOfMusics().size() > 1) {%>
                            <a class="btn sp-add-list invert" onclick="addAlbumToCart(<%=album.getId()%>)">Add Album to Cart</a>
                            <%}%>
                            <%
                                List<Music> musics = album.getListOfMusics();
                                if (musics == null) {
                                    musics = new ArrayList();
                                }
                            %>
                            <ul id="release-list" class="tracklist">
                                <%
                                    for (int j = 0; j < musics.size(); j++) {
                                        Music music = musics.get(j);
                                %>


                                <div class="toggle">
                                    <li>

                                        <div class="track-details">
                                            <a class="track sp-play-track" href="http://danielmusictest.storage.googleapis.com/<%=music.getFileLocation128()%>" data-cover="<%=albumArt%>"
                                               data-artist="<%=music.getArtistName()%>"
                                               data-artist_url="http://artist.com/madoff-freak" 
                                               data-artist_target="_self"
                                               data-shop_url="#!/cart" 
                                               data-shop_target="_blank"
                                               >
                                                <!-- cover -->
                                                <img class="track-cover" src="<%=albumArt%>">
                                                <!-- Title -->
                                                <span class="track-title" data-artist_url="artist_url"><%=music.getName()%></span>
                                                <!-- Artists -->
                                            </a>
                                            <div class="track-buttons" style="margin-top: 5px; margin-bottom: 5px;">
                                                <a class="track sp-play-track" href="http://danielmusictest.storage.googleapis.com/<%=music.getFileLocation128()%>" data-cover="<%=albumArt%>"
                                                   data-artist="<%=music.getArtistName()%>"
                                                   data-artist_url="http://artist.com/madoff-freak" 
                                                   data-artist_target="_self"
                                                   data-shop_url="#!/cart" 
                                                   data-shop_target="_blank"
                                                   >
                                                    <i class="icon icon-play2"><span style='display: none;'><%=music.getName()%></span></i>
                                                </a>
                                                <i style="cursor: pointer;" class="icon icon-menu2 toggle-title"><span style='display: none;'></span></i>
                                                <a style="cursor: pointer;" onclick="addTrackToCart(<%=music.getId()%>)"><i class="icon icon-cart"></i></a>
                                                    <%
                                                        if (music.getPrice() == 0.0) {
                                                            out.print("Free");
                                                        } else {
                                                            NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                                            out.print(formatter.format(music.getPrice()));
                                                        }
                                                    %>
                                            </div>
                                        </div>

                                    </li>

                                    <div class="toggle-content">
                                        <%
                                            if (music.getLyrics() != null) {
                                                String repl = music.getLyrics().replaceAll("\\r", "<br>");
                                                out.print(repl);
                                            }
                                        %> 
                                        <br/><br/><a style="cursor: pointer" onclick="window.open('./MusicController?target=Lyrics&id=<%=music.getId()%>', '_blank', 'width=600,height=760')">Open in new window</a>
                                    </div>

                                </div>


                                <%}%>                                    
                            </ul>
                            <%}%>
                            <p>
                                <a href="javascript:;" class="btn invert sp-play-list" data-id="release-list">Play All Tracks</a>
                                <a href="javascript:;" class="btn sp-add-list" data-id="release-list">Add All Tracks to Playlist</a>
                            </p>
                        </div>
                    </div>
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
