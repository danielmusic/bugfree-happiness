<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Search Results">
    <!-- ############################# Intro ############################# -->
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <h1 class="heading-l">Search <span class="color">Results</span></h1>
        <!-- Overlay -->
        <span class="overlay dots"></span>
    </section>
    <!-- /intro -->

    <!-- ############################# Content ############################# -->
    <section class="content section">
        <!-- container -->
        <div class="container">
            <%@page import="EntityManager.SearchHelper"%>
            <%@page import="EntityManager.Music"%>
            <%@page import="EntityManager.Album"%>
            <%@page import="EntityManager.Artist"%>
            <%
                SearchHelper result = (SearchHelper) session.getAttribute("searchResult");
                if (result != null) {
                    int totalAmtOfResults = result.getListOfAlbums().size() + result.getListOfArtists().size() + result.getListOfMusics().size();
            %>
            <!-- ############################# About US ############################# -->

            <!-- Article -->
            <article>
                <div class="row">
                    <h3><%=totalAmtOfResults%> results found</h3>
                    <!-- column -->
                    <div class="col-1-1 last">
                        <!-- tabs -->
                        <div class="tabs-wrap">
                            <!-- tabs navigation -->
                            <ul class="tabs">
                                <li><a href="#tab-artist" class="active-tab">Artists & Bands</a></li>
                                <li><a href="#tab-album">Albums</a></li>
                                <li><a href="#tab-music">Musics</a></li>
                            </ul>
                            <!-- /tabs navigation -->
                            <!-- tab content -->
                            <div id="tab-artist" class="tab-content">
                                <div id="artists" class="masonry clearfix">
                                    <%for (Artist artist : result.getListOfArtists()) {
                                            String genre = "";
                                            if (artist.getGenre() != null) {
                                                genre = artist.getGenre().getName();
                                            }
                                    %>
                                    <div class="col-1-4 item" data-genres="">
                                        <a href="#!/pages/artist-single" class="thumb-glitch artist" data-thumbicon="plus">
                                            <span class="hoverlayer"></span>
                                            <span class="img">
                                                <%if (artist.getImageURL() != null && !artist.getImageURL().isEmpty()) {%>
                                                <img src="http://danielmusictest.storage.googleapis.com/<%=artist.getImageURL()%>" />
                                                <%} else {%>
                                                <img src="placeholders/artist01.jpg" />
                                                <%}%>
                                            </span>
                                        </a>
                                        <div class="artist-footer">
                                            <h2 class="artist-title"><a href="#!/pages/artist-single"><%=artist.getName()%></a></h2>
                                            <span class="artist-genres"><%=genre%></span>
                                        </div>
                                        <div class="artist-social">
                                            <!--TODO (copy from artists page)-->
                                            <a href="javascript:;" class="facebook-share"><i class="icon icon-facebook"></i></a>
                                            <a href="javascript:;" class="twitter-share"><i class="icon icon-twitter"></i></a>
                                            <a href="javascript:;" class="googleplus-share"><i class="icon icon-googleplus"></i></a>
                                        </div>
                                    </div>
                                    <%}%>
                                </div>
                                <!-- /tab content -->
                                <!-- tab content -->
                                <div id="tab-album" class="tab-content">
                                    <div id="releases" class="masonry clearfix">
                                        <%for (Album album : result.getListOfAlbums()) {
                                                String albumArt = album.getImageLocation();
                                                if (albumArt == null || albumArt.isEmpty()) {
                                                    albumArt = "/img/cover.png";
                                                }%>
                                        <a href="#!/pages/release-single" class="thumb-glitch release tip" data-thumbicon="plus">
                                            <span class="hoverlayer"></span>
                                            <span class="release-badge">new?</span>
                                            <span class="img">
                                                <img src="http://danielmusictest.storage.googleapis.com/<%=albumArt%>" alt="Album Cover Image" />
                                            </span>
                                            <div class="tip-content hidden">
                                                <span>Load Details</span>
                                                Load and open release page.
                                            </div>
                                        </a>
                                        <div class="release-footer">
                                            <h2 class="release-title"><a href="#!/pages/release-single"><%=album.getName()%></a></h2>
                                            <span class="release-artists"><%=album.getArtistName()%></span>
                                        </div>
                                        <div class="release-social">
                                            <a href="javascript:;" class="facebook-share"><i class="icon icon-facebook"></i></a>
                                            <a href="javascript:;" class="twitter-share"><i class="icon icon-twitter"></i></a>
                                            <a href="javascript:;" class="googleplus-share"><i class="icon icon-googleplus"></i></a>
                                            <a href="javascript:;" class="googleplus-share floatright"><i class="icon icon-download"></i></a>
                                        </div>
                                        <!-- /release social -->
                                        <%}%>
                                    </div>
                                </div>
                                <!-- /tab content -->
                                <!-- tab content -->
                                <div id="tab-music" class="tab-content">
                                    <%for (Music music : result.getListOfMusics()) {%>
                                    <p><%=music.getName()%></p>
                                    <%}%>
                                </div>
                                <!-- /tab content -->
                            </div>
                            <!-- /tabs -->
                        </div>
                    </div>


            </article>
            <%} else {%>
            <article>
                Use the search bar above to start searching...
            </article>
            <%}%>
            <!-- /article -->

        </div>
        <!-- /container -->
    </section>
    <!-- /Content -->

</section>
<!-- /page -->