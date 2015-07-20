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

                    function isMusic(filename) {
                        var ext = getExtension(filename);
                        switch (ext.toLowerCase()) {
                            case 'wav':
                                return true;
                        }
                        return false;
                    }

                    $(function () {
                        $('form').submit(function () {
                            if (window.File && window.FileReader && window.FileList && window.Blob) {
                                var file = $('#picture');
                                var fileSize = $('#picture')[0].files[0].size;

                                /*var musicFile = $('#music');
                                 var musicFileSize = $('#music')[0].files[0].size;
                                 
                                 if (!isMusic(musicFile.val())) {
                                 document.getElementById("errMsg").style.display = "block";
                                 document.getElementById('errMsg').innerHTML = "Only wav format song is allowed";
                                 window.scrollTo(0, 0);
                                 return false;
                                 }
                                 
                                 if (musicFileSize > 50000000) {
                                 document.getElementById("errMsg").style.display = "block";
                                 document.getElementById('errMsg').innerHTML = "Image size must be below 50mb.";
                                 window.scrollTo(0, 0);
                                 return false;
                                 }*/

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
                <%@page import="EntityManager.Music"%>
                <%@page import="EntityManager.Artist"%>
                <%
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    Album album = (Album) (session.getAttribute("album"));
                    Music track = (Music) (session.getAttribute("track"));
                    String disableFlag = "";
                    if (album != null && album.getIsPublished()) {
                        disableFlag = "disabled";
                    }

                    String URL_128 = (String) (session.getAttribute("URL_128"));
                    String URL_320 = (String) (session.getAttribute("URL_320"));
                    String URL_Wav = (String) (session.getAttribute("URL_Wav"));

                    if (artist != null && album != null && track != null) {
                %>
                <form method="POST" enctype="multipart/form-data" action="MusicManagementController" class="form">
                    <jsp:include page="../jspIncludePages/displayMessage.jsp" />
                    <p class="error" id="errMsg" style="display:none;"></p>

                    <div class="entry-content">
                        <div class="entry-meta" style="float: right;">
                            <span class="entry-cat"><a href="#!/artist/albums">Albums</a></span>
                            <span class="entry-comments">Edit Single</span>
                        </div>                   
                    </div>

                    <h2>Single details</h2>

                    <div class="row clearfix">
                        <div class="col-1-3">
                            <label for="name"><strong>Title</strong> *</label>
                            <input type="text" <%=disableFlag%> id="name" name="name" value="<%=album.getName()%>" required>
                        </div>

                        <div class="col-1-3">
                            <label for="yearReleased"><strong>Year Released</strong> *</label>
                            <input type="number" <%=disableFlag%> id="yearReleased" name="yearReleased" min="1900" max="2050" value="<%=album.getYearReleased()%>" required>
                        </div>

                        <div class="col-1-3 last" style="margin-bottom: 0px;">
                            <label for="price"><strong>Price</strong> *</label>
                            <input type="number" id="price" name="price" min="0" max="9999" step="0.01" size="4" title="CDA Currency Format - no dollar sign and no comma(s) - cents (.##) are optional" required <%=disableFlag%> value="<%if (album.getPrice() != null) {
                                    out.print(album.getPrice());
                                }%>" />
                        </div>
                    </div>

                    <%if (album.getImageLocation() != null && !album.getImageLocation().isEmpty()) {%>
                    <img src="http://danielmusictest.storage.googleapis.com/<%=album.getImageLocation()%>">
                    <%}%>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="picture"><strong>Artwork</strong> </label>
                            <input type="file" <%=disableFlag%> id="picture" name="picture">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="music"><strong>Music * (WAV format, 44.1 kHz, 16bit)</strong></label>
                            <input type="file" <%=disableFlag%> id="music" name="music" required>

                            <a href="<%=URL_128%>" target="_blank">Click here to download 128 kbps</a><br>
                            <a href="<%=URL_320%>" target="_blank">Click here to download 320 kbps</a><br>
                            <a href="<%=URL_Wav%>" target="_blank">Click here to download Wav</a>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="description"><strong>Description</strong> </label>
                            <textarea id="description" name="description" style="min-height:120px;" <%=disableFlag%>><%if (album.getDescription() != null) {
                                    out.print(album.getDescription());
                                }%></textarea>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="lyrics"><strong>Lyrics</strong> </label>
                            <textarea id="lyrics" name="lyrics" <%=disableFlag%>><%if (track.getLyrics() != null) {
                                        out.print(track.getLyrics());
                                    }%></textarea>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="credits"><strong>Credits</strong> </label>
                            <textarea id="credits" name="credits" <%=disableFlag%> placeholder="produced by, Mastering, Recording, Design, Photography..." style="min-height:120px;"><%if (album.getCredits() != null) {
                                    out.print(album.getCredits());
                                }%></textarea>
                        </div>
                    </div>

                    <input type="hidden" value="EditSingles" name="target">
                    <button type="button" class="small invert" onclick="javascript:back();" style="margin-right: 10px;">Back</button>
                    <%if (!album.getIsPublished()) {%>
                    <button type="submit" class="small invert" style="margin-right: 10px;">Save Changes</button>
                    <button type="button" class="small invert md-trigger" style="margin-right: 10px;" data-modal="modal-delete">Delete Singles</button>
                    <div class="md-modal md-effect-1" id="modal-delete">
                        <div class="md-content">
                            <h3>Are you sure?</h3>
                            <div style="text-align:center;">
                                <p>Are you sure?</p>
                                <button type="button" onclick="javascript:deleteAlbum('<%=album.getId()%>')">Confirm</button>
                                <button class="md-close" type="button">Cancel</button>
                            </div>
                        </div>
                    </div>
                    <%}%>
                    <div class="clear"></div>
                </form>
                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>
        </div>
    </section>
</section>
