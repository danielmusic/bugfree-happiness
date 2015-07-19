<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Tracks">
    <section class="content section">
        <div class="container">
            <article>
                <%@page import="EntityManager.Music"%>
                <%@page import="EntityManager.Artist"%>
                <%@page import="EntityManager.Album"%>
                <%@page import="java.util.List"%>
                <script>
                    function addTrack() {
                        window.location.href = "#!/artist/add_track";
                    }

                    function viewTrack(id) {
                        window.location.href = "MusicManagementController?target=ListTrackByID&id=" + id;
                    }

                    function editTrackPrice(id) {
                        window.location.href = "MusicManagementController?target=ListTrackByID&source=editMusicPrice&id=" + id;
                    }

                    function back() {
                        window.location.href = "#!/artist/albums";
                    }
                </script>
                <%
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    Album album = (Album) (session.getAttribute("album"));
                    if (artist != null && album != null) {
                        List<Music> tracks = (List<Music>) (session.getAttribute("tracks"));
                %>
                <form name="trackManagement">
                    <jsp:include page="../jspIncludePages/displayMessage.jsp" />

                    <div class="entry-content">
                        <div class="entry-meta" style="float: right;">
                            <span class="entry-cat"><a href="#!/artist/albums">Albums</a></span>
                            <span class="entry-comments">Tracks</span>
                        </div>                   
                    </div>

                    <h2><%=album.getName()%> - Tracks</h2>

                    <table class="layout display responsive-table">
                        <thead>
                            <tr>
                                <th>Track No</th>
                                <th>Track Title</th>
                                <th>Total Downloads</th>
                                <th>Total Sold</th>
                                <th colspan="2"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (tracks != null) {
                                    for (int i = 0; i < tracks.size(); i++) {
                            %>    
                            <tr>
                                <td class="table-date"><%=tracks.get(i).getTrackNumber()%></td>
                                <td class="table-name"><%=tracks.get(i).getName()%></td>   
                                <td class="table-date"><%=tracks.get(i).getNumDownloaded()%></td>
                                <td class="table-date"><%=tracks.get(i).getNumPurchase()%></td>            
                                <td class="actions" style="width: 350px;">
                                    <a href="javascript:viewTrack(<%=tracks.get(i).getId()%>);" class="buy-tickets">View Track</a>
                                    <a href="javascript:editTrackPrice(<%=tracks.get(i).getId()%>);" class="buy-tickets">Edit Price</a>
                                    <%if (!album.getIsPublished()) {%>
                                    <a class="md-trigger buy-tickets" data-modal="modal-name">Delete Track</a>
                                    <div class="md-modal md-effect-1" id="modal-delete">
                                        <div class="md-content">
                                            <h3>Are you sure?</h3>
                                            <div style="text-align:center;">
                                                <p>Are you sure?</p>
                                                <button type="button" onclick="javascript:deleteSingle('<%=tracks.get(i).getId()%>')">Confirm</button>
                                                <button class="md-close" type="button">Cancel</button>
                                            </div>
                                        </div>
                                    </div>
                                    <%}%>
                                </td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                        </tbody>
                    </table>
                    <button type="button" class="small" onclick="javascript:back();" style="margin-right: 10px;">Back</button>
                    <%if (!album.getIsPublished()) {%>
                    <button type="button" class="small" onclick="javascript:addTrack()">Add Track</button>
                    <%}%>
                </form>
                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>
        </div>
    </section>
</section>
