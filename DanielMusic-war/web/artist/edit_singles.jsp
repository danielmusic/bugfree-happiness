<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Single">

    <section class="content section">
        <div class="container">
            <article>
                <script>
                    function back() {
                        window.location.href = "#!/artist/albums";
                    }
                </script>
                <%@page import="EntityManager.Album"%>
                <%@page import="EntityManager.Music"%>
                <%@page import="EntityManager.Artist"%>
                <%
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    Album album = (Album) (session.getAttribute("album"));
                    if (artist != null && album != null) {
                %>
                <form method="POST" enctype="multipart/form-data" action="MusicManagementController" class="form">
                    <jsp:include page="../jspIncludePages/displayMessage.jsp" />
                    <p class="error" id="errMsg" style="display:none;"></p>

                    <div class="entry-content">
                        <div class="entry-meta" style="float: right;">
                            <span class="entry-cat"><a href="#!/artist/albums">Single</a></span>
                            <span class="entry-comments">Edit Single Price</span>
                        </div>                   
                    </div>

                    <h2>Single: <%=album.getName()%></h2>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="price"><strong>Price (SGD)</strong> *</label>
                            <input type="number" id="price" name="price" min="0" max="9999" step="0.01" size="4" title="CDA Currency Format - no dollar sign and no comma(s) - cents (.##) are optional" required value="<%if (album.getPrice() != null) {
                                    out.print(album.getPrice());
                                }%>" />
                        </div>
                    </div>

                    <input type="hidden" value="EditSinglePrice" name="target">

                    <button type="button" class="small invert" onclick="javascript:back();" style="margin-right: 10px;">Back</button>
                    <button type="submit" class="small invert" style="margin-right: 10px;">Save Changes</button>
                    <div class="clear"></div>
                </form>
                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>
        </div>
    </section>
</section>
