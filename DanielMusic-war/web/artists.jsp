<!-- ############################# Ajax Page Container ############################# -->
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="EntityManager.Album"%>
<%@page import="EntityManager.Music"%>
<%@page import="EntityManager.Artist"%>
<%@page import="EntityManager.Account"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.net.URLEncoder"%>

<%
    Artist artist = (Artist) session.getAttribute("artistDetails");
    List<Album> albums = (List<Album>) session.getAttribute("artistAlbumDetails");
%>
<section id="page" data-title="sounds.sg | <%=artist.getName()%>">
    <style>
        .heading-l{
            font-size: 80px;
            margin-bottom: 0px;
        }

        .heading-m{
            font-size: 32px;
        }
    </style>
    <%
        if (artist == null || albums == null) {
            out.print("<p class='warning'>Ops. No results found.</p>");
        } else {
            String jumpToAlbumID = (String) session.getAttribute("jumpToAlbumID");
            if (jumpToAlbumID != null && !jumpToAlbumID.isEmpty()) {
    %>
    <input type="hidden" id="jumpToAlbum" value="<%=jumpToAlbumID%>"/>
    <%
            session.removeAttribute("jumpToAlbumID");
        }
    %>
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/artist-single-bg.jpg)">
        <h2 class="heading-l"><span class="color"><%=artist.getName()%></span></h2>
        <br>
        <span class="overlay grids"></span>
    </section>
    <section class="content section">
        <div class="container">
            <script>
                function loadArtist2(id) {
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

                function addTrackToCart(trackID) {
                    url = "./MusicManagementController?target=AddTrackToShoppingCart";
                    $.ajax({
                        type: "GET",
                        async: false,
                        url: url,
                        data: {'trackID': trackID},
                        success: function () {
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
                        success: function () {
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

                function copyArtistURL() {
                    var copyTextarea = document.querySelector('.artistURL');
                    copyTextarea.select();
                    document.execCommand('copy');
                }
            </script>
            <div class="sidebar main-left main-medium">
                <div class="widget details-widget">
                    <%if (artist.getImageURL() != null && !artist.getImageURL().isEmpty()) {%>
                    <a href="http://sounds.sg.storage.googleapis.com/<%=artist.getImageURL()%>" title="<%=artist.getName()%>" data-lightbox="lightbox">
                        <span class="img">
                            <img src="http://sounds.sg.storage.googleapis.com/<%=artist.getImageURL()%>" />
                        </span>
                    </a>
                    <%} else {%>
                    <a href="placeholders/artist01.jpg" title="<%=artist.getName()%>" class="thumb-glitch" data-lightbox="lightbox">
                        <span class="img">
                            <img src="placeholders/artist01.jpg" />
                        </span>
                    </a>
                    <%}%>

                    <div class="details-meta">
                        <ul class="details-list">
                            <li>
                                <span class="label">Name</span>
                                <div class="data"><b><%=artist.getName()%></b></div>
                            </li>
                            <li>
                                <span class="label">Genre</span>
                                <div class="data"><%if (artist.getGenre() != null) {
                                        out.print(artist.getGenre().getName());
                                    }%></div>
                            </li>
                            <% if (artist.getIsBand()) {%>
                            <li>
                                <span class="label">Date Formed</span>
                                <div class="data"><%if (artist.getBandDateFormed() != null) {
                                        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy");
                                        String date = DATE_FORMAT.format(artist.getBandDateFormed());
                                        out.print(date);
                                    }%></div>
                            </li>
                            <%}%>
                            <li>
                                <span class="label" style="margin-bottom: 5px;">URL</span>
                                <%
                                    String artistURL = "http://sounds.sg/music/" + URLEncoder.encode(artist.getName(), "UTF-8");
                                %>
                                <input type="text" class="artistURL" value="<%=artistURL%>" style="width:100%; height: 40px; font-size: 14px;">
                                <button class="" type="button" style="float: right; z-index: 2;position: relative;margin-right: 6px;margin-top: -40px;" onclick="javascript:copyArtistURL();">Copy URL</button>
                                </input>
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
                        <a href="<%=artist.getInstagramURL()%>"><i class="fa fa-instagram "></i></a>
                            <%}%>

                        <%if (artist.getWebsiteURL() != null && !artist.getWebsiteURL().isEmpty()) {%>
                        <a href="<%=artist.getWebsiteURL()%>"><i class="icon icon-IE"></i></a>
                            <%}%>
                    </div>
                </div>

                <p>
                    <a class="btn invert sp-play-list" data-id="release-list" style="width: 100%;">Play All Tracks</a>
                    <a class="btn invert sp-add-list" data-id="release-list" style="width: 100%;">Add All Tracks to Playlist</a>
                </p>
            </div>

            <div id="main" class="release main-left main-medium">
                <article>
                    <p class="error" id="errMsg" style="display:none;"></p>
                    <p class="success" id="goodMsg"  style="display:none;"></p>

                    <h1><%=artist.getName()%></h1>

                    <!-- tabs -->
                    <div id="tabs" class="tabs-wrap">
                        <!-- tabs navigation -->
                        <ul class="tabs">
                            <li><a href="#tab-bio" class="active-tab">Biography</a></li>
                            <li><a href="#tab-releases">Albums</a></li>
                        </ul>
                        <!-- /tabs navigation -->
                        <!-- tab content -->
                        <div id="tab-bio" class="tab-content">
                            <%
                                if (artist.getIsBand() && artist.getBandMembers() != null && !artist.getBandMembers().isEmpty()) {
                                    String repl = artist.getBandMembers().replaceAll("\\r", "<br>");
                            %>
                            <h2>Members</h2>
                            <p> <%out.print(repl);%></p>
                            <%}%>


                            <%
                                if (artist.getBiography() != null && !artist.getBiography().isEmpty()) {
                                    String repl = artist.getBiography().replaceAll("\\r", "<br>");
                            %>
                            <h2>About</h2>
                            <p> <%out.print(repl);%></p>
                            <%}%>

                            <%
                                if (artist.getInfluences() != null && !artist.getInfluences().isEmpty()) {
                                    String repl = artist.getInfluences().replaceAll("\\r", "<br>");
                            %>
                            <h2>Influences</h2>
                            <p> <%out.print(repl);%></p>
                            <%}%>

                            <%
                                if (artist.getContactEmail() != null && !artist.getContactEmail().isEmpty()) {
                                    String repl = artist.getContactEmail().replaceAll("\\r", "<br>");
                            %>
                            <h2>Contact Email</h2>
                            <p> <%out.print(repl);%></p>
                            <%}%>


                        </div>
                        <!-- /tab content -->
                        <!-- play all tracks content/add all tracks to playlist button-->
                        <ul id="release-list" class="tracklist" style="display:none;">
                            <%
                                for (int i = 0; i < albums.size(); i++) {
                                    Album album = albums.get(i);
                                    String albumArt = album.getImageLocation();
                                    if (albumArt == null || albumArt.isEmpty()) {
                                        albumArt = "img/cover.png";
                                    } else {
                                        albumArt = "http://sounds.sg.storage.googleapis.com/" + albumArt;
                                    }
                                    List<Music> musics = album.getListOfMusics();
                                    if (musics != null && !musics.isEmpty()) {
                                        for (int j = 0; j < musics.size(); j++) {
                                            Music music = musics.get(j);
                            %>
                            <li>
                                <div class="track-details">
                                    <div class="track-buttons">
                                        <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/<%=music.getFileLocation128()%>" data-cover="<%=albumArt%>"
                                           data-artist="<%=music.getArtistName()%>"
                                           data-artist_url="javascript:loadArtist2(<%=music.getAlbum().getArtist().getId()%>);"
                                           data-artist_target="_blank"
                                           data-shop_url="javascript:addTrackToCart(<%=music.getId()%>);"
                                           data-shop_target="_blank"
                                           >
                                            <i class="icon icon-plus">
                                                <span style="display: none;">
                                                    <span  class="track-title"><%=music.getName()%></span>
                                                    <span class="track-artists"><%=artist.getName()%></span>
                                                </span>
                                            </i>
                                        </a>
                                    </div>
                                </div>
                            </li>
                            <%
                                        }
                                    }
                                }
                            %>
                        </ul>
                        <!-- tab content -->
                        <div id="tab-releases" class="tab-content">
                            <%
                                for (int i = 0; i < albums.size(); i++) {
                                    Album album = albums.get(i);
                                    String albumArt = album.getImageLocation();
                                    if (albumArt == null || albumArt.isEmpty()) {
                                        albumArt = "img/cover.png";
                                    } else {
                                        albumArt = "http://sounds.sg.storage.googleapis.com/" + albumArt;
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
                                        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
                                        decimalFormatSymbols.setCurrencySymbol("");
                                        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);
                                        out.print(formatter.format(album.getPrice()));
                                    }
                                %>
                            </p>
                            <p>Year Released: <%=album.getYearReleased()%></p>
                            <%if (!album.getIsSingle()) {%>
                            <a class="btn sp-add-list invert" onclick="addAlbumToCart(<%=album.getId()%>)">Add Album to Cart</a>
                            <%} else {%>
                            <a class="btn sp-add-list invert" onclick="addAlbumToCart(<%=album.getId()%>)">Add Single to Cart</a>
                            <%
                                }
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
                                            <a class="track" href="<%=albumArt%>" title="<%=artist.getName()%>" data-lightbox="lightbox">
                                                <img class="track-cover" title="<%=artist.getName()%>" alt="Track Cover" src="<%=albumArt%>">
                                                <span>&nbsp;&nbsp;</span>
                                            </a>

                                            <span class="track"> 
                                             <!--     <img class="track-cover" src="<%=albumArt%>">-->
                                                <span style="display: block; margin-left: 40px;"><%=music.getName()%></span>
                                                <span class="track-artists" style="margin-left: 40px;"><%=album.getGenreName()%></span>
                                            </span>


                                            <div class="track-buttons">
                                                <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/<%=music.getFileLocation128()%>" data-cover="<%=albumArt%>"
                                                   data-artist="<%=music.getArtistName()%>"
                                                   data-artist_url="javascript:loadArtist2(<%=music.getAlbum().getArtist().getId()%>);"
                                                   data-artist_target="_blank"
                                                   data-shop_url="javascript:addTrackToCart(<%=music.getId()%>);"
                                                   data-shop_target="_blank">
                                                    <i class="icon icon-plus">
                                                        <span style="display: none;">
                                                            <span  class="track-title"><%=music.getName()%></span>
                                                            <span class="track-artists"><%=artist.getName()%></span>
                                                        </span>
                                                    </i>
                                                </a>

                                                <span id='btnPlay<%=music.getId()%>'>
                                                    <a class="track sp-play-track" style="margin-left: 6px;" href="http://sounds.sg.storage.googleapis.com/<%=music.getFileLocation128()%>" data-cover="<%=albumArt%>"
                                                       data-artist="<%=music.getArtistName()%>"
                                                       data-artist_url="javascript:loadArtist2(<%=music.getAlbum().getArtist().getId()%>);"
                                                       data-artist_target="_blank"
                                                       data-shop_url="javascript:addTrackToCart(<%=music.getId()%>);"
                                                       data-shop_target="_blank"
                                                       style="margin-left: 6px;margin-right: 6px;"
                                                       >
                                                        <i class="icon icon-play2">
                                                            <span style="display: none;">
                                                                <span  class="track-title"><%=music.getName()%></span>
                                                                <span class="track-artists"><%=artist.getName()%></span>
                                                            </span>
                                                        </i>
                                                    </a>
                                                </span>

                                                <i style="cursor: pointer;" class="icon icon-menu2 toggle-title">
                                                    <span style='display: none;'></span>
                                                </i>
                                                <%if (album.getListOfMusics().size() > 1) {%>
                                                <a style="cursor: pointer;" onclick="addTrackToCart(<%=music.getId()%>)">
                                                    <%} else {%>
                                                    <a style="cursor: pointer;" onclick="addAlbumToCart(<%=album.getId()%>)">
                                                        <%}%>
                                                        <i class="icon icon-cart"></i>
                                                    </a>

                                                    <span style="margin-left: 6px;">
                                                        <%
                                                            if (music.getPrice() == 0.0) {
                                                                out.print("Free");
                                                            } else {
                                                                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                                                DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
                                                                decimalFormatSymbols.setCurrencySymbol("");
                                                                ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);
                                                                out.print(formatter.format(music.getPrice()));
                                                            }
                                                        %>
                                                    </span>
                                            </div>
                                        </div>

                                        <div class="toggle-content">
                                            <%
                                                if (music.getLyrics() != null) {
                                                    String repl = music.getLyrics().replaceAll("\\r", "<br>");
                                                    out.print(repl);
                                                }
                                            %> 
                                            <br/><br/><a style="cursor: pointer" onclick="window.open('./MusicController?target=Lyrics&id=<%=music.getId()%>', '_blank', 'width=600,height=760')">Open in new window</a>
                                        </div>
                                    </li>
                                </div>

                                <%}%>            
                            </ul>

                            <!-- end of looping-->
                            <br><br>
                            <%}%>
                        </div>
                    </div>
                </article>
            </div>
        </div>

        <div class="md-overlay"></div>
    </section>
    <%}%>
</section>
<!--Friendly URL urlrewrite-->
<script>
    //window.history.pushState("", "", "music/<%=artist.getName()%>");
</script>
<!--Friendly URL urlrewrite-->
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
