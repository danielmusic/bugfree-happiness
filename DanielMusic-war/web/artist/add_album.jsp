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
                            var file = $('#picture');
                            var fileSize = $('#picture')[0].files[0].size

                            if (fileSize > 5000000) {
                                document.getElementById("errMsg").style.display = "block";
                                document.getElementById('errMsg').innerHTML = "Image size must be below 5mb.";
                                return false;
                            }

                            if (!isImage(file.val())) {
                                document.getElementById("errMsg").style.display = "block";
                                document.getElementById('errMsg').innerHTML = "Please select a valid image";
                                return false;
                            }

                        });
                    });

                    function back() {
                        window.location.href = "#!/artist/albums";
                    }
                </script>


                <%@page import="EntityManager.Artist"%>
                <%
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    if (artist != null) {
                %>

                <form method="POST" enctype="multipart/form-data" action="MusicManagementController" class="form">
                    <jsp:include page="../jspIncludePages/displayMessage.jsp" />
                    <p class="error" id="errMsg" style="display:none;"></p>

                    <div class="entry-content">
                        <div class="entry-meta" style="float: right;">
                            <span class="entry-cat"><a href="#!/artist/albums">Albums</a></span>
                            <span class="entry-comments">Add Album</span>
                        </div>                   
                    </div>


                    <h2>Album details</h2>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="name"><strong>Title</strong> *</label>
                            <input type="text" id="name" name="name" required>
                        </div>

                        <div class="col-1-2 last">
                            <label for="yearReleased"><strong>Year Released</strong> *</label>
                            <input type="number" id="yearReleased" name="yearReleased" min="1900" max="2050" required>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="picture"><strong>Album Artwork</strong> </label>
                            <input type="file" id="picture" name="picture">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="description"><strong>Album Description</strong> </label>
                            <textarea id="description" name="description"></textarea>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="credits"><strong>Credits</strong> </label>
                            <textarea id="credits" name="credits" placeholder="produced by, Mastering, Recording, Design, Photography..." style="min-height:120px;"></textarea>
                        </div>
                    </div>

                    <input type="hidden" value="AddAlbum" name="target">
                    <input type="hidden" value="Artist" name="source">
                    <button type="button" class="small invert" onclick="javascript:back();" style="margin-right: 10px;">Back</button>
                    <button type="submit" class="small invert">Add Album</button>
                    <div class="clear"></div>
                </form>
                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>
        </div>
    </section>
</section>
