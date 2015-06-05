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
                <h2>Add Album</h2>



                <input type="submit" value="Add Album" class="small">
                <%}%>
            </article>
        </div>
    </section>
</section>
