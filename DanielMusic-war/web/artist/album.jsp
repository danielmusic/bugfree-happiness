<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="sounds.sg | album">
    <%@page import="java.text.NumberFormat"%>
    <%@page import="EntityManager.Album"%>
    <%@page import="EntityManager.Music"%>
    <%@page import="EntityManager.Artist"%>
    <%@page import="java.text.DecimalFormatSymbols"%>
    <%@page import="java.text.DecimalFormat"%>
    <section class="content section">
        <div class="container">
            <article>
                <script>
                    function back() {
                        window.location.href = "#!/artist/albums";
                    }
                    function deleteAlbum(id) {
                        window.location.href = "MusicManagementController?target=DeleteAlbum&id=" + id;
                    }
                </script>
                <%
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    Album album = (Album) (session.getAttribute("album"));

                    if (artist != null && album != null) {
                %>
                <div class="md-modal md-effect-1" id="modal-delete">
                    <div class="md-content">
                        <h3>Are you sure?</h3>
                        <div style="text-align:center;">
                            <button type="button" onclick="javascript:deleteAlbum('<%=album.getId()%>')">Confirm</button>
                            <button class="md-close" type="button">Cancel</button>
                        </div>
                    </div>
                </div>
                <div class="entry-content">
                    <div class="entry-meta">
                        <span class="entry-cat"><a href="#!/artist/albums">Albums</a></span>
                        <span class="entry-comments">View Album</span>
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
                                        albumArt = "http://sounds.sg.storage.googleapis.com/" + albumArt;
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
                                    <div class="data"><b><%=album.getName()%></b></div>
                                </li>
                                <li>
                                    <span class="label">Price</span>
                                    <div class="data">
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
                                    </div>
                                </li>
                                <li>
                                    <span class="label">Year Released</span>
                                    <div class="data"><b><%=album.getYearReleased()%></b></div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

                <div id="main" class="release main-left main-medium">
                    <article>
                        <h2 id="album_<%=album.getId()%>">Album Title: <%=album.getName()%></h2>
                        <p>
                            <strong>Description:</strong><br>
                            <%
                                if (album.getDescription() != null && !album.getDescription().isEmpty()) {
                                    String repl = album.getDescription().replaceAll("\\r", "<br>");
                                    out.print(repl);
                                } else {
                                    out.print("-");
                                }
                            %>
                        </p>

                        <p>
                            <strong>Credits:</strong><br>
                            <%
                                if (album.getCredits() != null && !album.getCredits().isEmpty()) {
                                    String repl = album.getCredits().replaceAll("\\r", "<br>");
                                    out.print(repl);
                                } else {
                                    out.print("-");
                                }
                            %>
                        </p>

                        <br>
                        <button type="button" class="small invert" onclick="javascript:back();" style="margin-right: 10px;">Back</button>
                        <button type="button" class="small invert md-trigger" style="margin-right: 10px;" data-modal="modal-delete">Delete Album</button>
                    </article>
                </div>
                <%} else {%>
                <p class="warning" id="errMsg">Your Session has timed out. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
                <div class="md-overlay"></div>
                <script src="js/classie.js"></script>
                <script src="js/modalEffects.js"></script>
            </article>
        </div>
    </section>
</section>
