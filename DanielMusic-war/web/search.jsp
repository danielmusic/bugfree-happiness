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
            <%@page import="java.text.NumberFormat"%>
            <%
                SearchHelper result = (SearchHelper) session.getAttribute("searchResult");
                if (result != null) {
                    int totalAmtOfResults = result.getListOfAlbums().size() + result.getListOfArtists().size() + result.getListOfMusics().size();
            %>
            <!-- ############################# About US ############################# -->
            <script>
                function loadArtistFromSearch(id) {
                    url = "./MusicController?target=GetArtistByID";
                    $.ajax({
                        type: "GET",
                        async: false,
                        url: url,
                        data: {'id': id},
                        dataType: "text",
                        success: function (val) {
                            var json = JSON.parse(val);
                            if (json.result) {
                                window.location.href = "#!/artists";
                            }
                        },
                        error: function (xhr, status, error) {
                            document.getElementById("errMsg").style.display = "block";
                            document.getElementById('errMsg').innerHTML = error;
                            hideLoader();
                            ajaxResultsError(xhr, status, error);
                        }
                    });
                }
                function loadAlbum(id) {
                    url = "./MusicController?target=GetAlbumByID";
                    $.ajax({
                        type: "GET",
                        async: false,
                        url: url,
                        data: {'id': id},
                        dataType: "text",
                        success: function (val) {
                            var json = JSON.parse(val);
                            if (json.result) {
                                window.location.href = "#!/artists";
                            }
                        },
                        error: function (xhr, status, error) {
                            document.getElementById("errMsg").style.display = "block";
                            document.getElementById('errMsg').innerHTML = error;
                            hideLoader();
                            ajaxResultsError(xhr, status, error);
                        }
                    });
                }

                function addTrackToCartFromSearch(trackID) {
                    url = "./MusicManagementController?target=AddTrackToShoppingCart";
                    $.ajax({
                        type: "GET",
                        async: false,
                        url: url,
                        data: {'trackID': trackID},
                        success: function (val) {
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

                function switchToPauseButtonFromSearch() {
                    var spanLabel = "btnPause" + id;
                    document.getElementById(spanLabel).style.display = "inline";
                    spanLabel = "btnPlay" + id;
                    document.getElementById(spanLabel).style.display = "none";
                }

                function switchToPlayButtonFromSearch(id) {
                    var spanLabel = "btnPlay" + id;
                    document.getElementById(spanLabel).style.display = "inline";
                    spanLabel = "btnPause" + id;
                    document.getElementById(spanLabel).style.display = "none";
                }
            </script>
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
                                    <div class="col-1-4">
                                        <a style="cursor: pointer;" onclick="javascript:loadArtistFromSearch(<%=artist.getId()%>)" class="thumb-glitch">
                                            <span class="img">
                                                <%if (artist.getImageURL() != null && !artist.getImageURL().isEmpty()) {%>
                                                <img src="http://sounds.sg.storage.googleapis.com/<%=artist.getImageURL()%>" />
                                                <%} else {%>
                                                <img src="placeholders/artist01.jpg" />
                                                <%}%>
                                            </span>
                                        </a>
                                        <div class="artist-footer">
                                            <h2 class="artist-title"><a style="cursor: pointer;" onclick="javascript:loadArtistFromSearch(<%=artist.getId()%>)"><%=artist.getName()%></a></h2>
                                            <span class="artist-genres"><%=genre%></span>
                                        </div>
                                        <div class="artist-social">
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
                                    <%}%>
                                </div>
                            </div>
                            <!-- /tab content -->
                            <!-- tab content -->
                            <div id="tab-album" class="tab-content">
                                <div class="masonry clearfix">
                                    <%for (Album album : result.getListOfAlbums()) {
                                            String albumArt = album.getImageLocation();
                                            if (albumArt == null || albumArt.isEmpty()) {
                                                albumArt = "/img/cover.png";
                                            } else {
                                                albumArt = "http://sounds.sg.storage.googleapis.com/" + albumArt;
                                            }
                                    %>

                                    <div class="col-1-4 item">
                                        <!-- Thumbnail -->
                                        <a style="cursor: pointer;" onclick="javascript:loadAlbum(<%=album.getId()%>)" class="thumb-glitch release">
                                            <span class="hoverlayer"></span>
                                            <span class="img">
                                                <img src="<%=albumArt%>" alt="Album Cover Image" />
                                            </span>
                                            <!-- tooltip -->
                                            <div class="tip-content hidden">
                                                <span>Load Details</span>
                                                Load and open release page.
                                            </div>
                                            <!-- /tooltip -->
                                        </a>
                                        <!-- /Thumbnail -->
                                        <!-- Release footer -->
                                        <div class="release-footer">
                                            <h2 class="release-title"><a href="#!/pages/release-single"><%=album.getName()%></a></h2>
                                            <span class="release-artists"><%=album.getArtistName()%></span>
                                        </div>
                                        <!-- /release footer -->
                                    </div>
                                    <%}%>
                                </div>
                            </div>
                            <!-- /tab content -->
                            <!-- tab content -->
                            <div id="tab-music" class="tab-content">
                                <ul id="release-list" class="tracklist">
                                    <%for (Music music : result.getListOfMusics()) {
                                            String albumArt = music.getAlbum().getImageLocation();
                                            if (albumArt == null || albumArt.isEmpty()) {
                                                albumArt = "/img/cover.png";
                                            } else {
                                                albumArt = "http://sounds.sg.storage.googleapis.com/" + albumArt;
                                            }
                                    %>
                                    <li>
                                        <div class="track-details">

                                            <a class="track" href="<%=albumArt%>" title="<%=music.getName()%>" data-lightbox="lightbox" >
                                                <img class="track-cover" title="<%=music.getArtistName()%>" alt="Track Cover" src="<%=albumArt%>">
                                                <span>&nbsp;&nbsp;</span>
                                            </a>

                                            <a class="track" onclick="javascript:loadArtistFromSearch(<%=music.getAlbum().getArtist().getId()%>)" style="cursor: pointer;">
                                                <span class="track-title" style="margin-left: 40px;"><%=music.getName()%></span>
                                                <span class="track-artists" style="margin-left: 40px;"><%=music.getArtistName()%></span>
                                            </a> 


                                            <div class="track-buttons">
                                                <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/<%=music.getFileLocation128()%>" data-cover="<%=albumArt%>"
                                                   data-artist_target="_blank"
                                                   data-artist_url="javascript:loadArtistFromSearch(<%=music.getAlbum().getArtist().getId()%>);"
                                                   data-shop_target="_blank"
                                                   data-shop_url="javascript:addTrackToCartFromSearch(<%=music.getId()%>);"
                                                   data-shop_target="_blank"
                                                   >
                                                    <i class="icon icon-play2">
                                                        <span style="display: none;">
                                                            <span  class="track-title"><%=music.getName()%></span>
                                                            <span class="track-artists"><%=music.getAlbum().getArtist().getName()%></span>
                                                        </span>
                                                    </i>
                                                </a>

                                                <span id='btnPlay<%=music.getId()%>'>
                                                    <a class="track sp-play-track" onclick="javascript:switchToPauseButtonFromSearch(<%=music.getId()%>);" href="http://sounds.sg.storage.googleapis.com/<%=music.getFileLocation128()%>" data-cover="<%=albumArt%>"
                                                       data-artist_target="_blank"
                                                       data-artist_url="javascript:loadArtistFromSearch(<%=music.getAlbum().getArtist().getId()%>);"
                                                       data-shop_target="_blank"
                                                       data-shop_url="javascript:addTrackToCartFromSearch(<%=music.getId()%>);"
                                                       data-shop_target="_blank"
                                                       style="margin-left: 0px;"
                                                       >
                                                        <i class="icon icon-play2">
                                                            <span style="display: none;">
                                                                <span  class="track-title"><%=music.getName()%></span>
                                                                <span class="track-artists"><%=music.getAlbum().getArtist().getName()%></span>
                                                            </span>
                                                        </i>
                                                    </a>
                                                </span>

                                                <span id='btnPause<%=music.getId()%>' style="display: none;">
                                                    <a class="track sp-play-track" onclick="javascript:switchToPauseButtonFromSearch(<%=music.getId()%>);" href="http://sounds.sg.storage.googleapis.com/<%=music.getFileLocation128()%>" data-cover="<%=albumArt%>"
                                                       data-artist_target="_blank"
                                                       data-artist_url="javascript:loadArtistFromSearch(<%=music.getAlbum().getArtist().getId()%>);"
                                                       data-shop_target="_blank"
                                                       data-shop_url="javascript:addTrackToCartFromSearch(<%=music.getId()%>);"
                                                       data-shop_target="_blank"
                                                       >
                                                        <i class="icon icon-pause">
                                                            <span style="display: none;">
                                                                <span  class="track-title"><%=music.getName()%></span>
                                                                <span class="track-artists"><%=music.getAlbum().getArtist().getName()%></span>
                                                            </span>
                                                        </i>
                                                    </a>
                                                </span>

                                                <a style="cursor: pointer;" onclick="addTrackToCart(<%=music.getId()%>)">
                                                    <i class="icon icon-cart"></i>
                                                </a>

                                                <span style="margin-left: 6px;">
                                                    <%
                                                        if (music.getPrice() == 0.0) {
                                                            out.print("Free");
                                                        } else {
                                                            NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                                            out.print(formatter.format(music.getPrice()));
                                                        }
                                                    %>
                                                </span>
                                            </div>
                                        </div>
                                    </li>
                                    <%}%>
                                </ul>
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
<script type="text/javascript">

    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-66150326-1']);
    var d = document.location.pathname + document.location.search + document.location.hash;
    _gaq.push(['_trackPageview', d]);

    (function () {
        var ga = document.createElement('script');
        ga.type = 'text/javascript';
        ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(ga, s);
    })();

</script>
