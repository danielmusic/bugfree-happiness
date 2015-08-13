<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Sounds.sg | Explore">
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
                            <%for (ExploreHelper eh : exploreHelpers) {%>
                            <option value="<%=eh.getGenre().getName()%>"><%=eh.getGenre().getName()%></option>
                            <%}%>
                        </select>
                    </div>


                    <div id="musicListing" class="masonry clearfix">

                        <%
                            for (int i = 0; i < exploreHelpers.size(); i++) {
                                ExploreHelper eh = exploreHelpers.get(i);
                                for (int j = 0; j < eh.getArtists().size(); j++) {
                                    Artist artist = eh.getArtists().get(j);
                                    Music artistFeaturedMusic = eh.getFeaturedMusic().get(j);
                        %>

                        <div class="col-1-1 item tracklist" data-genres="<%=eh.getGenre().getName()%>">
                            <%if (artistFeaturedMusic != null) {%>

                            <div class="track-details">

                                <a class="track sp-play-track" onclick="javascript:loadAjaxExplore(<%=artist.getId()%>)" data-cover="http://sounds.sg.storage.googleapis.com/<%=artistFeaturedMusic.getAlbum().getImageLocation()%>"
                                   data-artist="<%=artistFeaturedMusic.getArtistName()%>"
                                   data-artist_url="http://artist.com/madoff-freak" 
                                   data-artist_target="_self"
                                   data-shop_url="#!/cart" 
                                   data-shop_target="_blank"
                                   >

                                    <img class="track-cover" alt="Track Cover" src="http://sounds.sg.storage.googleapis.com/<%=artistFeaturedMusic.getAlbum().getImageLocation()%>" style="top: 2px;">
                                    <span class="track-title"><%=artistFeaturedMusic.getName()%></span>
                                    <span class="track-artists"><%=artist.getName()%></span>
                                </a>

                                <div class="track-buttons">
                                    <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/<%=artistFeaturedMusic.getFileLocation128()%>" data-cover="http://sounds.sg.storage.googleapis.com/<%=artistFeaturedMusic.getAlbum().getImageLocation()%>"
                                       data-artist="<%=artistFeaturedMusic.getArtistName()%>"
                                       data-artist_url="http://artist.com/madoff-freak" 
                                       data-artist_target="_self"
                                       data-shop_url="#!/cart" 
                                       data-shop_target="_blank"
                                       >
                                        <i class="icon icon-plus"><span style='display: none;'><%=artistFeaturedMusic.getName()%></span></i>
                                    </a>

                                    <a class="track sp-play-track" href="http://sounds.sg.storage.googleapis.com/<%=artistFeaturedMusic.getFileLocation128()%>" data-cover="http://sounds.sg.storage.googleapis.com/<%=artistFeaturedMusic.getAlbum().getImageLocation()%>"
                                       data-artist="<%=artistFeaturedMusic.getArtistName()%>"
                                       data-artist_url="http://artist.com/madoff-freak" 
                                       data-artist_target="_self"
                                       data-shop_url="#!/cart" 
                                       data-shop_target="_blank"
                                       style="margin-left: 0px;"
                                       >
                                        <i class="icon icon-play2"><span style='display: none;'><%=artistFeaturedMusic.getName()%></span></i>
                                    </a>
                                </div>
                            </div>

                            <%}%>

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

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
