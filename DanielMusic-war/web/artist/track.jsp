<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Track">
    <%@page import="java.text.NumberFormat"%>
    <%@page import="EntityManager.Album"%>
    <%@page import="EntityManager.Music"%>
    <%@page import="EntityManager.Artist"%>
    <section class="content section">
        <div class="container">
            <article>
                <script>
                    function back() {
                        window.location.href = "#!/artist/tracks";
                    }
                </script>
                <%
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    Album album = (Album) (session.getAttribute("album"));
                    Music music = (Music) (session.getAttribute("track"));
                    String URL_128 = (String) (session.getAttribute("URL_128"));
                    String URL_320 = (String) (session.getAttribute("URL_320"));
                    String URL_Wav = (String) (session.getAttribute("URL_Wav"));

                    if (artist != null && album != null && music != null) {
                %>

                <div class="entry-content">
                    <div class="entry-meta">
                        <span class="entry-cat"><a href="#!/artist/tracks">Tracks</a></span>
                        <span class="entry-comments">View Track</span>
                    </div>                   
                </div>
                <br>

                <div class="sidebar main-left main-medium">
                    <div class="widget details-widget">
                        <a style="cursor: default;" class="thumb-glitch">
                            <span class="img">
                                <%
                                    String albumArt = album.getImageLocation();
                                    if (albumArt == null || albumArt.isEmpty()) {
                                        albumArt = "/img/cover.png";
                                    } else {
                                        albumArt = "http://danielmusictest.storage.googleapis.com/" + albumArt;
                                    }
                                %>

                                <%if (album.getImageLocation() != null && !album.getImageLocation().isEmpty()) {%>
                                <img src="<%=albumArt%>" />
                                <%}%>
                            </span>
                        </a>
                        <div class="details-meta">
                            <ul class="details-list">
                                <li>
                                    <span class="label">Title</span>
                                    <div class="data"><b><%=music.getName()%></b></div>
                                </li>

                                <li>
                                    <span class="label">Track No</span>
                                    <div class="data"><b><%=music.getTrackNumber()%></b></div>
                                </li>
                                <li>
                                    <span class="label">Price</span>
                                    <div class="data"><%       if (music.getPrice() == 0.0) {
                                            out.print("Free");
                                        } else {
                                            NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                            out.print(formatter.format(music.getPrice()));
                                        }%></div>
                                </li>
                                <li>
                                    <span class="label">Year Released</span>
                                    <div class="data"><b><%=music.getYearReleased()%></b></div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

                <div id="main" class="release main-left main-medium">
                    <article>
                        <h2 id="album_<%=music.getId()%>">Track TItle: <%=music.getName()%></h2>


                        <a href="<%=URL_128%>" target="_blank">Click here to download 128 kbps</a><br>
                        <a href="<%=URL_320%>" target="_blank">Click here to download 320 kbps</a><br>
                        <a href="<%=URL_Wav%>" target="_blank">Click here to download Wav</a>

                        <br><br>

                        <p>
                            <strong>Lyrics:</strong><br>
                            <%
                                if (music.getLyrics() != null && !music.getLyrics().isEmpty()) {
                                    String repl = music.getLyrics().replaceAll("\\r", "<br>");
                                    out.print(repl);
                                } else {
                                    out.print("-");
                                }
                            %>
                        </p>

                        <p>
                            <strong>Credits:</strong><br>
                            <%
                                if (music.getCredits() != null && !music.getCredits().isEmpty()) {
                                    String repl = music.getCredits().replaceAll("\\r", "<br>");
                                    out.print(repl);
                                } else {
                                    out.print("-");
                                }
                            %>
                        </p>

                        <br>
                        <button type="button" class="small invert" onclick="javascript:back();">Back</button>

                    </article>
                </div>


                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>
        </div>
    </section>
</section>
