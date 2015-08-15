<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Sounds.sg | Explore">
    <style>
        .track-details:before{
            display: none;
        }

        .track-details{
            border-left: none;
            margin-left: 0;
        }
    </style>
    <!-- ############################# Intro ############################# -->
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/blog-bg.jpg)">
        <h1 class="heading-l">Explore</h1>
        <h2 class="heading-m">explore  and <span class="color">Discover</span></h2>
    </section>
    <!-- /intro -->
    <section class="content section">
        <div class="container">
            <%@page import="EntityManager.Artist"%>
            <%@page import="EntityManager.ExploreHelper"%>
            <%@page import="EntityManager.Genre"%>
            <%@page import="java.util.List"%>
            <%@page import="EntityManager.Music"%>
            <script>
                function loadAjaxExplore(id) {
                    exploreForm.id.value = id;
                    url = "./MusicController?target=GetArtistByID";
                    $.ajax({
                        type: "GET",
                        async: false,
                        url: url,
                        data: {'id': id},
                        dataType: "text",
                        success: function (val) {
                            window.event.returnValue = true;
                            var json = JSON.parse(val);
                            if (json.result) {
                                window.event.returnValue = false;
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
            </script>
            <article>
                <%
                    List<ExploreHelper> exploreHelpers = (List<ExploreHelper>) (session.getAttribute("genres"));
                %>

                <form name="exploreForm">
                    <div class="row clearfix filters" data-id="musicListing">
                        <select class='nice-select filter' name="genres">
                            <option value="placeholder">All Genres</option>
                            <option value="*">All Genres</option>
                            <%for (ExploreHelper artists : exploreHelpers) {%>
                            <option value="<%=artists.getGenre().getName()%>"><%=artists.getGenre().getName()%></option>
                            <%}%>
                        </select>
                    </div>

                    <div id="musicListing" class="masonry clearfix">
                        <%
                            for (int i = 0; i < exploreHelpers.size(); i++) {
                                ExploreHelper artists = exploreHelpers.get(i);
                                for (int j = 0; j < artists.getArtists().size(); j++) {
                                    Artist artist = artists.getArtists().get(j);
                                    Music artistFeaturedMusic = artists.getFeaturedMusic().get(j);

                                    String profilePicURL;
                                    if (artist.getImageURL() != null && !artist.getImageURL().isEmpty()) {
                                        profilePicURL = "http://sounds.sg.storage.googleapis.com/" + artist.getImageURL();
                                    } else {
                                        profilePicURL = "placeholders/artist01.jpg";
                                    }
                        %>

                        <!-- Start looping out songs -->
                        <div class="col-1-1 tracklist" data-genres="<%=artists.getGenre().getName()%>">
                            <div class="track-details">
                                <!-- Hack -->
                                <a class="track" onclick="javascript:loadAjaxExplore(<%=artist.getId()%>)" >
                                    <img class="track-cover" title="<%=artist.getName()%>" data-lightbox="lightbox" alt="Track Cover" src="<%=profilePicURL%>" style="top: 2px;">
                                    <span class="track-title"><%=artist.getName()%></span>
                                    <span class="track-artists"><%=artists.getGenre().getName()%></span>
                                </a> 

                                <div class="track-buttons">
                                    <%if (artistFeaturedMusic != null) {%>
                                    <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/<%=artistFeaturedMusic.getFileLocation128()%>" data-cover="http://sounds.sg.storage.googleapis.com/<%=artistFeaturedMusic.getAlbum().getImageLocation()%>"
                                       data-artist="<%=artist.getName()%>"
                                       data-artist_url="http://artist.com/madoff-freak" 
                                       data-artist_target="_self"
                                       data-shop_url="#!/cart" 
                                       data-shop_target="_blank"
                                       >
                                        <i class="icon icon-plus">
                                            <span style='display: none;' class="track-title"><%=artistFeaturedMusic.getName()%></span>
                                            <span style='display: none;' class="track-artists"><%=artist.getName()%></span>
                                        </i>
                                    </a>

                                    <a class="track sp-play-track" href="http://sounds.sg.storage.googleapis.com/<%=artistFeaturedMusic.getFileLocation128()%>" data-cover="http://sounds.sg.storage.googleapis.com/<%=artistFeaturedMusic.getAlbum().getImageLocation()%>"
                                       data-artist="<%=artist.getName()%>"
                                       data-artist_url="http://artist.com/madoff-freak" 
                                       data-artist_target="_self"
                                       data-shop_url="#!/cart" 
                                       data-shop_target="_blank"
                                       style="margin-left: 0px;"
                                       >
                                        <i class="icon icon-play2">
                                            <span style='display: none;' class="track-title"><%=artistFeaturedMusic.getName()%></span>
                                            <span style='display: none;' class="track-artists"><%=artist.getName()%></span>
                                        </i>
                                    </a>
                                    <%}%>
                                </div>
                            </div>
                        </div>
                        <br/>
                        <%
                                }
                            }
                        %>
                    </div>
                </form>
            </article>
            <!-- /article -->
        </div>
        <!-- /container -->
    </section>
    <!-- /Content -->
</section>
<!-- /page -->

<script>
    //var new_url = $('#url').val();
    //window.history.pushState("object or string", "Title", "" + new_url);
    //window.history.replaceState("object or string", "Title", "Discover");
</script>
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
