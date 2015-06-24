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

                <form method="POST" enctype="multipart/form-data" action="MusicManagementController" class="form">
                    <jsp:include page="../displayMessage.jsp" />

                    <h2>Album details</h2>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="name"><strong>Album Name</strong> </label>
                            <input type="text" id="name" name="name" required>
                        </div>
                        <div class="col-1-2 last">
                            <label for="yearReleased"><strong>Year Released</strong> </label>
                            <input type="number" id="yearReleased" name="yearReleased" min="1900" max="2050" required>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="picture"><strong>Album Art</strong> </label>
                            <input type="file" id="picture" name="picture">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="description"><strong>Album Description</strong> </label>
                            <textarea id="description" name="description" cols="88" rows="6"></textarea>
                        </div>
                    </div>

                    <input type="hidden" value="AddAlbum" name="target">
                    <input type="hidden" value="Artist" name="source">
                    <button type="submit" class="large invert">Add</button>
                    <div class="clear"></div>
                </form>
                    
                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>
        </div>
    </section>
</section>
