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
                    <h3><%=totalAmtOfResults%> matching results found</h3>
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
                                <%for (Artist artist : result.getListOfArtists()) {%>
                                <p><%=artist.getName()%></p>
                                <%}%>
                            </div>
                            <!-- /tab content -->
                            <!-- tab content -->
                            <div id="tab-album" class="tab-content">
                                <%for (Album album : result.getListOfAlbums()) {%>
                                <p><%=album.getName()%></p>
                                <%}%>
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