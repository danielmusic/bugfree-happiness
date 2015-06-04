<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Albums">

    <section class="content section">
        <div class="container">
            <article>
                <%@page import="EntityManager.Artist"%>
                <%
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    if (artist != null) {
                %>
                <h2>Albums</h2>

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
                            <td class="actions">
                                <a href="javascript:;" class="buy-tickets" title="Buy Tickets">Add Track</a>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <input type="submit" value="Add Album" class="small">
                <%}%>
            </article>
        </div>
    </section>
</section>
