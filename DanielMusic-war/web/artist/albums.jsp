<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Albums">
    <%@page import="EntityManager.Account"%>
    <%@page import="EntityManager.Artist"%>
    <%@page import="EntityManager.Album"%>
    <%@page import="java.util.List"%>
    <script>
        function addAlbum() {
            window.location.href = "#!/artist/add_album";
        }

        function addSingles() {
            window.location.href = "#!/artist/add_singles";
        }

        function viewAlbum(id) {
            window.location.href = "MusicManagementController?target=ListAlbumByID&id=" + id;
        }

       function editSingles(id) {
            window.location.href = "MusicManagementController?target=ViewSingles&id=" + id;
        }
        
        function viewTracks(id) {
            window.location.href = "MusicManagementController?target=ListAllTracksByAlbumID&id=" + id;
        }

        function publishAlbum(id) {
            window.location.href = "MusicManagementController?source=artist&target=PublishAlbum&id=" + id;
        }
    </script>
    <section class="content section">
        <div class="container">
            <article>

                <%
                    Account account = (Account) (session.getAttribute("account"));
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    if (account != null) {
                        if (!account.getEmailIsVerified()) {
                            out.print("<p class='warning'>You will not be able to todo _____? until you verify your email. Click here to <a href='#!/verify-email'>resend verification code</a>.</p>");
                        } else if (artist.getIsApproved()==0) { //new
                            out.print("<p class='warning'>Your account will be subjected to an approval process when you publish your first album/single. Your profile, albums and tracks will not be searchable on the website until your account is approved.</p>");
                        } else if (artist.getIsApproved()==-1) { //rejected
                            out.print("<p class='warning'>Your account's published album(s) has been rejected, review your profile, albums and tracks and republish a new album for us to review your account again.</p>");
                        } else if (artist.getIsApproved()==-2) { //pending
                            out.print("<p class='warning'>Your account is pending approval by our administrators, your profile and published albums/tracks will not appear on our website until your account is approved.</p>");
                        }
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
                                <th>Status</th>
                                <th colspan="2"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (albums != null) {
                                    for (int i = 0; i < albums.size(); i++) {
                                        if (!albums.get(i).getIsSingle()) {
                            %>    
                            <tr>
                                <td class="table-date"><%=(i + 1)%></td>
                                <td class="table-name"><%=albums.get(i).getName()%></td>         
                                <td class="table-date">
                                    <%
                                        if (albums.get(i).getIsPublished()) {
                                            out.print("Published");
                                        } else {
                                            out.print("Not Published");
                                        }
                                    %>
                                </td>
                                <td class="actions" style="width: 300px;">
                                    <a href="javascript:viewAlbum(<%=albums.get(i).getId()%>);" class="buy-tickets">Edit album</a>
                                    <a href="javascript:viewTracks(<%=albums.get(i).getId()%>);" class="buy-tickets">View tracks</a>
                                    <%if (!albums.get(i).getIsPublished()) {%>
                                    <a href="javascript:publishAlbum(<%=albums.get(i).getId()%>);" class="buy-tickets">Publish</a>
                                    <%}%>
                                </td>
                            </tr>
                            <%
                                        }
                                    }
                                }
                            %>
                        </tbody>
                    </table>

                    <h2>Singles</h2>
                    <table class="layout display responsive-table">
                        <thead>
                            <tr>
                                <th>no</th>
                                <th>Singles Title</th>
                                <th>Status</th>
                                <th colspan="2"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (albums != null) {
                                    for (int i = 0; i < albums.size(); i++) {
                                        if (albums.get(i).getIsSingle()) {
                            %>    
                            <tr>
                                <td class="table-date"><%=(i + 1)%></td>
                                <td class="table-name"><%=albums.get(i).getName()%></td>         
                                <td class="table-date">
                                    <%
                                        if (albums.get(i).getIsPublished()) {
                                            out.print("Published");
                                        } else {
                                            out.print("Not Published");
                                        }
                                    %>
                                </td>
                                <td class="actions" style="width: 300px;">
                                    <a href="javascript:editSingles(<%=albums.get(i).getId()%>);" class="buy-tickets">Edit singles</a>
                                    <%if (!albums.get(i).getIsPublished()) {%>
                                    <a href="javascript:publishAlbum(<%=albums.get(i).getId()%>);" class="buy-tickets">Publish</a>
                                    <%}%>
                                </td>
                            </tr>
                            <%
                                        }
                                    }
                                }
                            %>
                        </tbody>
                    </table>

                    <button type="button" class="small" onclick="javascript:addAlbum()" style="margin-right: 10px;">Add Album</button>
                    <button type="button" class="small" onclick="javascript:addSingles()">Add Singles</button>
                </form>
                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>
        </div>
    </section>
</section>
