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
                </script>
                <%
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    if (artist != null) {
                        List<Album> albums = (List<Album>) (session.getAttribute("albums"));
                %>
                <form name="albumManagement">
                    <p class="error" id="errMsg" style="display:none;"></p>
                    <p class="success" id="goodMsg"  style="display:none;"></p>

                    <h2>Albums</h2>

                    <table class="layout display responsive-table">
                        <thead>
                            <tr>
                                <th>Publish Date</th>
                                <th>Status</th>
                                <th colspan="2">Album Name</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (albums != null) {
                                    for (int i = 0; i > albums.size(); i++) {

                            %>    
                            <tr>
                                <td class="table-name"><%=albums.get(i).getPublishedDate()%></td>
                                <td class="table-date"><%=albums.get(i).getIsPublished()%></td>
                                <td class="table-name"><%=albums.get(i).getName()%></td>                                  

                                <td class="actions" style="width: 250px;">
                                    <a href="javascript:;" class="buy-tickets">Add Track</a>
                                    <a href="javascript:;" class="buy-tickets">Remove Track</a>
                                </td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                        </tbody>
                    </table>
                    <button  class="small" onclick="javascript:addAlbum()">Add Album</button>
                </form>
                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>
        </div>
    </section>
</section>
