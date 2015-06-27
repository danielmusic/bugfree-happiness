<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Albums">

    <section class="content section">
        <div class="container">
            <article>
                <script>
                    function getExtension(filename) {
                        var parts = filename.split('.');
                        return parts[parts.length - 1];
                    }

                    function isImage(filename) {
                        var ext = getExtension(filename);
                        switch (ext.toLowerCase()) {
                            case 'jpg':
                            case 'gif':
                            case 'bmp':
                            case 'png':
                                return true;
                        }
                        return false;
                    }

                    $(function () {
                        $('form').submit(function () {
                            if (window.File && window.FileReader && window.FileList && window.Blob) {
                                var file = $('#picture');
                                var fileSize = $('#picture')[0].files[0].size;

                                if (fileSize > 5000000) {
                                    document.getElementById("errMsg").style.display = "block";
                                    document.getElementById('errMsg').innerHTML = "Image size must be below 5mb.";
                                    window.scrollTo(0, 0);
                                    return false;
                                }

                                if (!isImage(file.val())) {
                                    document.getElementById("errMsg").style.display = "block";
                                    document.getElementById('errMsg').innerHTML = "Please select a valid image";
                                    window.scrollTo(0, 0);
                                    return false;
                                }
                            } else {
                                document.getElementById("errMsg").style.display = "block";
                                document.getElementById('errMsg').innerHTML = "Please upgrade your browser, because your current browser lacks some new features we need!";
                                window.scrollTo(0, 0);
                            }
                        });
                    });

                    function back() {
                        window.location.href = "#!/artist/albums";
                    }
                </script>

                <%@page import="EntityManager.Album"%>
                <%@page import="java.util.List"%>
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
                            <span class="entry-cat"><a href="#!/artist/albums">Albums</a></span>
                            <span class="entry-comments">Edit Album</span>
                        </div>                   
                    </div>

                    <h2>Album details</h2>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="name"><strong>Album Name</strong> </label>
                            <input type="text" id="name" name="name" value="<%=album.getName()%>" required>
                        </div>

                        <div class="col-1-2 last">
                            <label for="yearReleased"><strong>Year Released</strong> </label>
                            <input type="number" id="yearReleased" name="yearReleased" value="<%=album.getYearReleased()%>" min="1900" max="2050" required>
                        </div>
                    </div>

                    <%if (album.getImageLocation() != null && !album.getImageLocation().isEmpty()) {%>
                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="picture"><strong>Album Artwork</strong> </label>
                            <img src="http://danielmusictest.storage.googleapis.com/<%=album.getImageLocation()%>">
                            <input type="file" id="picture" name="picture">
                        </div>
                    </div>
                    <%}%>


                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="description"><strong>Album Description</strong> </label>
                            <textarea id="description" name="description"><%if (album.getDescription() != null) {
                                    out.print(album.getDescription());
                                }%></textarea>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="credits"><strong>Credits</strong> </label>
                            <textarea id="credits" name="credits" placeholder="produced by, Mastering, Recording, Design, Photography..." style="min-height:120px;"><%if (album.getCredits() != null) {
                                    out.print(album.getCredits());
                                }%></textarea>
                        </div>
                    </div>

                    <input type="hidden" value="UpdateAlbum" name="target">
                    <input type="hidden" value="Artist" name="source">
                    <input type="hidden" value="<%=album.getId()%>" name="id">

                    <button type="button" class="small invert" onclick="javascript:back();" style="margin-right: 10px;">Back</button>
                    <button type="submit" class="small invert" style="margin-right: 10px;">Save Changes</button>
                    <button type="button" class="small invert">Publish Album</button>
                    <div class="clear"></div>
                </form>
                <%} else if (album == null) {%>
                <p class="warning" id="errMsg">Ops. An error has occured. <a href="#!/artist/albums">Click here to try again.</a></p>
                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>
        </div>
    </section>
</section>
