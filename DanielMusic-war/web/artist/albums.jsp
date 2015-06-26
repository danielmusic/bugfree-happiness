<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Albums">
    <section class="content section">
        <div class="container">
            <article>
                <%@page import="EntityManager.Artist"%>
                <%@page import="EntityManager.Album"%>
                <%@page import="java.util.List"%>
                <script>
                    function addAlbum() {
                        window.location.href = "#!/artist/add_album";
                    }

                    function viewAlbum(id) {
                        window.location.href = "MusicManagementController?source=edit_album&target=ListAlbumByID&id=" + id;
                    }

                    function viewTracks(id) {
                        window.location.href = "MusicManagementController?source=tracks&target=ListAllTracksByAlbumID&id=" + id;
                    }
                </script>
                <%
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    if (artist != null) {
                        List<Album> albums = (List<Album>) (session.getAttribute("albums"));
                %>
                <form name="albumManagement">
                    <jsp:include page="../jspIncludePages/displayMessage.jsp" />

                    <h2>Albums</h2>

                    <table class="layout display responsive-table">
                        <thead>
                            <tr>
                                <th>no</th>
                                <th>Album Title</th>
                                <th>Publish Date</th>
                                <th>Status</th>
                                <th colspan="2"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (albums != null) {
                                    for (int i = 0; i < albums.size(); i++) {
                            %>    
                            <tr>
                                <td class="table-date"><%=(i + 1)%></td>
                                <td class="table-name"><%=albums.get(i).getName()%></td>         
                                <td class="table-name">
                                    <%
                                        out.print(albums.get(i).getId() + " <<<id<<<");
                                        if (albums.get(i).getPublishedDate() != null) {
                                            out.print(albums.get(i).getPublishedDate());
                                        } else {
                                            out.print("-");
                                        }
                                    %>
                                </td>
                                <td class="table-date"><%=albums.get(i).getIsPublished()%></td>
                                <td class="actions" style="width: 250px;">
                                    <a href="javascript:viewAlbum(<%=albums.get(i).getId()%>);" class="buy-tickets">View album</a>
                                    <a href="javascript:viewTracks(<%=albums.get(i).getId()%>);" class="buy-tickets">View track</a>
                                </td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                        </tbody>
                    </table>
                    <button type="button" class="small" onclick="javascript:addAlbum()">Add Album</button>
                </form>
                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>
        </div>
    </section>
</section>
