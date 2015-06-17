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
                <form action="../ClientAccountManagementController" class="form">
                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="name"><strong>Album Name</strong> </label>
                            <input type="text" id="name" name="name" disabled>
                        </div>

                        <div class="col-1-2 last">
                            <label for="image"><strong>Album Image:</strong> </label>
                            <input type="file" id="image" name="image">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="description"><strong>Album Description</strong> </label>
                            <textarea id="description" name="bio" cols="88" rows="6" required></textarea>
                        </div>
                    </div>

                    <input type="submit" value="Add" class="large invert">
                    <input type="hidden" value="ArtistUpdateProfile" name="target">
                    <div class="clear"></div>
                </form>
                <%}%>
            </article>
        </div>
    </section>
</section>
