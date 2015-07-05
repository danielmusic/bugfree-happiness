<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Sound.sg">
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/artist-single-bg.jpg)">
        <h2 class="heading-l">Artist <span class="color">ZERO</span></h2>
        <br>
        <span class="overlay grids"></span>
    </section>
    <section class="content section">
        <div class="container">
            <%@page import="java.util.List"%>
            <%@page import="EntityManager.Album"%>
            <%@page import="EntityManager.Music"%>
            <%@page import="EntityManager.Artist"%>
            <%@page import="EntityManager.Account"%>
            <%@page import="java.text.NumberFormat"%>
            <%
                Account account = (Account) session.getAttribute("account");
                Artist artist = (Artist) session.getAttribute("artistDetails");
            %>

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
                                <div class="data"><%=artist.getGenre().getName()%></div>
                            </li>
                        </ul>
                    </div>
                    <!-- Details Share -->
                    <div class="details-social-box">
                        <%if (artist.getFacebookURL() != null && !artist.getFacebookURL().isEmpty()) {%>
                        <a href="<%=artist.getFacebookURL()%>" class="facebook-share"><i class="icon icon-facebook"></i></a>
                            <%}%>

                        <%if (artist.getTwitterURL() != null && !artist.getTwitterURL().isEmpty()) {%>
                        <a href="<%=artist.getTwitterURL()%>" class="twitter-share"><i class="icon icon-twitter"></i></a>
                            <%}%>

                        <%if (artist.getInstagramURL() != null && !artist.getInstagramURL().isEmpty()) {%>
                        <a href="<%=artist.getInstagramURL()%>" class="googleplus-share"><i class="icon icon-user"></i></a>
                            <%}%>

                        <%if (artist.getWebsiteURL() != null && !artist.getWebsiteURL().isEmpty()) {%>
                        <a href="<%=artist.getWebsiteURL()%>" class="googleplus-share"><i class="icon icon-IE"></i></a>
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
                                    String repl = artist.getBiography().replaceAll("\\r", "<br>");
                                    out.print(repl);
                                %>
                            </p>
                        </div>
                        <!-- /tab content -->
                        <!-- tab content -->
                        <div id="tab-releases" class="tab-content">

                            <%
                                for (int i = 0; i < artist.getListOfAlbums().size(); i++) {
                                    Album album = artist.getListOfAlbums().get(i);
                                    String albumArt = album.getImageLocation();
                                    if (albumArt == null || albumArt.isEmpty()) {
                                        albumArt = "/img/cover.png";
                                    }
                            %>
                            <h2><%=album.getName()%></h2>
                            <p>
                                <%
                                    repl = album.getDescription().replaceAll("\\r", "<br>");
                                    out.print(repl);
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
                            <%
                                List<Music> musics = album.getListOfMusics();
                            %>
                            <ul id="release-list" class="tracklist">
                                <%
                                    for (int j = 0; j < musics.size(); j++) {
                                        Music music = musics.get(j);
                                %>
                                <li>
                                    <div class="track-details">
                                        <a class="track sp-play-track" href="http://danielmusictest.storage.googleapis.com/<%=music.getFileLocation128()%>" data-cover="http://danielmusictest.storage.googleapis.com/<%=albumArt%>"
                                           data-artist="<%=music.getArtistName()%>"
                                           data-artist_url="http://artist.com/madoff-freak" 
                                           data-artist_target="_self"
                                           data-shop_url="#!/cart" 
                                           data-shop_target="_blank"
                                           >
                                            <!-- cover -->
                                            <img class="track-cover" src="http://danielmusictest.storage.googleapis.com/<%=albumArt%>">
                                            <!-- Title -->
                                            <span class="track-title" data-artist_url="artist_url"><%=music.getName()%></span>
                                            <!-- Artists -->
                                        </a>

                                        <div class="track-buttons">
                                            <a class="track sp-play-track" href="http://danielmusictest.storage.googleapis.com/<%=music.getFileLocation128()%>" data-cover="http://danielmusictest.storage.googleapis.com/<%=albumArt%>"
                                               data-artist="<%=music.getArtistName()%>"
                                               data-artist_url="http://artist.com/madoff-freak" 
                                               data-artist_target="_self"
                                               data-shop_url="#!/cart" 
                                               data-shop_target="_blank"
                                               >
                                                <i class="icon icon-play2"><span style='display: none;'><%=music.getName()%></span></i>
                                            </a>
                                            <a href="javascript:;"><i class="icon icon-cart"></i></a>
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
                                <%}%>                                    


                            </ul>
                            <%
                                }
                            %>




                            <p>
                                <a href="javascript:;" class="btn invert sp-play-list" data-id="release-list">Play All Tracks</a>
                                <a href="javascript:;" class="btn sp-add-list" data-id="release-list">Add All Tracks</a>
                            </p>

                        </div>
                        <!-- /tab content -->
                    </div>
                    <!-- /tabs -->


                </article>
                <!-- /article -->

            </div>
            <!-- /main -->
        </div>
        <!-- /container -->
    </section>
    <!-- /Content -->

</section>
<!-- /page -->