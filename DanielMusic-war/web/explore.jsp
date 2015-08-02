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
                <div class="col-1-1">
                    <form name="exploreForm">

                        <div class="row clearfix filters" data-id="events">
                            <select class='nice-select filter' name="genres">
                                <option value="placeholder">All Genres</option>
                                <option value="*">All Genres</option>

                                <%for (ExploreHelper eh : exploreHelpers) {%>
                                <option value="<%=eh.getGenre().getName()%>"><%=eh.getGenre().getName()%></option>
                                <%}%>
                            </select>
                        </div>

                        <%
                            for (ExploreHelper eh : exploreHelpers) {
                                for (Artist artist : eh.getArtists()) {
                        %>

                        <div id="events" class="masonry events-grid clearfix">
                            <div class="col-1-4 item event" data-genres="<%=eh.getGenre().getName()%>" data-artists="<%=artist.getName()%>">
                                <a onclick="javascript:loadAjaxExplore(<%=artist.getId()%>)">
                                    <span class="event-meta">
                                        <span class="event-date"><%=artist.getName()%></span>
                                        <span class="event-title"><%=eh.getGenre().getName()%></span>
                                    </span>
                                    <%if (artist.getImageURL() != null && !artist.getImageURL().isEmpty()) {%>
                                    <img src="http://sounds.sg.storage.googleapis.com/<%=artist.getImageURL()%>">
                                    <%} else {%>
                                    <img src="placeholders/event01.jpg">
                                    <%}%>
                                </a>

                                <div class="details-social-box">
                       
                                    <a class="track sp-play-track" href="http://sounds.sg.storage.googleapis.com/<%//music.getFileLocation128()%>" data-cover="<%//albumArt%>"
                                       data-artist="<%//music.getArtistName()%>"
                                       data-artist_url="http://artist.com/madoff-freak" 
                                       data-artist_target="_self"
                                       data-shop_url="#!/cart" 
                                       data-shop_target="_blank"
                                       >
                                        <i class="icon icon-play2"></i>
                                    </a>


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

                        <br/>
                        <%
                                }
                            }
                        %>
                        <input type="hidden" value="" name="id" id="id">
                    </form>
                </div>


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