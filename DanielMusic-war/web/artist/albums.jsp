<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Albums">
    <section class="content section">
        <div class="container">
            <article>
                <%@page import="EntityManager.Artist"%>
                <script>
                    function addAlbum() {
                        window.event.returnValue = true;
                        document.albumManagement.action = "#!/artist/add_album";
                        document.albumManagement.submit();
                    }
                </script>


                <%
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    if (artist != null) {
                %>
                <h2>Albums</h2>

                <form name="albumManagement">
                    <table class="layout display responsive-table">
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th colspan="2">Album Title</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td class="table-date">12/02</td>
                                <td class="table-name">
                                    Papagayo Beach Club
                                </td>
                                <td class="actions" style="width: 250px;">
                                    <a href="javascript:;" class="buy-tickets">Add Track</a>
                                    <a href="javascript:;" class="buy-tickets">Remove Track</a>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <button  class="small" onclick="javascript:addAlbum()">Add Album</button>
                </form>
                <%}%>
            </article>
        </div>
    </section>
</section>
