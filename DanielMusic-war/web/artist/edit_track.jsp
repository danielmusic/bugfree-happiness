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
                                var musicFile = $('#music');
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
                                }
                            } else {
                                document.getElementById("errMsg").style.display = "block";
                                document.getElementById('errMsg').innerHTML = "Please upgrade your browser, because your current browser lacks some new features we need!";
                                window.scrollTo(0, 0);
                            }
                        });
                    });

                    function back() {
                        window.location.href = "#!/artist/tracks";
                    }

                    function deleteTrack(id) {
                        window.location.href = "MusicManagementController?target=DeleteTrack&id=" + id;
                    }
                </script>
                <%@page import="EntityManager.Album"%>
                <%@page import="EntityManager.Music"%>
                <%@page import="EntityManager.Artist"%>
                <%
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    Music track = (Music) (session.getAttribute("track"));
                    Album album = (Album) (session.getAttribute("album"));
                    String disableFlag = "";
                    if (album != null && album.getIsPublished()) {
                        disableFlag = "disabled";
                    }

                    String URL_128 = (String) (session.getAttribute("URL_128"));
                    String URL_320 = (String) (session.getAttribute("URL_320"));
                    String URL_Wav = (String) (session.getAttribute("URL_Wav"));
                    if (artist != null && track != null) {
                %>
                <form method="POST" enctype="multipart/form-data" action="MusicManagementController" class="form">
                    <jsp:include page="../jspIncludePages/displayMessage.jsp" />
                    <p class="error" id="errMsg" style="display:none;"></p>

                    <div class="entry-content">
                        <div class="entry-meta" style="float: right;">
                            <span class="entry-cat"><a href="#!/artist/albums">Albums</a></span>
                            <span class="entry-cat"><a href="#!/artist/tracks">Tracks</a></span>
                            <span class="entry-comments">Add Track</span>
                        </div>                   
                    </div>

                    <h2>Track details</h2>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="name"><strong>Title</strong> *</label>
                            <input type="text" id="name" name="name" <%=disableFlag%> value="<%if (track.getName() != null) {
                                    out.print(track.getName());
                                }%>" required>
                        </div>                        

                        <div class="col-1-2 last">
                            <label for="yearReleased"><strong>Year Released</strong> *</label>
                            <input type="number" id="yearReleased" name="yearReleased" min="1900" max="2050" <%=disableFlag%> value="<%if (track.getYearReleased() != 0) {
                                    out.print(track.getYearReleased());
                                }%>" required>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="trackNumber"><strong>Track no</strong> </label>
                            <input type="text" id="trackNumber" name="trackNumber" <%=disableFlag%> value="<%if (track.getTrackNumber() != null && track.getTrackNumber() != 0) {
                                    out.print(track.getTrackNumber());
                                }%>">
                        </div>

                        <div class="col-1-2 last">
                            <label for="price"><strong>Price</strong> *</label>
                            <input type="number" id="price" name="price" min="0" max="9999" step="0.01" size="4" title="CDA Currency Format - no dollar sign and no comma(s) - cents (.##) are optional" required <%=disableFlag%> value="<%if (track.getPrice() != null && track.getPrice() != 0) {
                                    out.print(track.getPrice());
                                }%>"/>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="music"><strong>Music * (WAV format, 44.1 kHz, 16bit)</strong></label>
                            <input type="file" id="music" name="music" <%=disableFlag%>>

                            <a href="<%=URL_128%>" target="_blank">Click here to download 128 kbps</a><br>
                            <a href="<%=URL_320%>" target="_blank">Click here to download 320 kbps</a><br>
                            <a href="<%=URL_Wav%>" target="_blank">Click here to download Wav</a>
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
                            <textarea id="credits" name="credits" <%=disableFlag%> placeholder="produced by, Mastering, Recording, Design, Photography, Instrumentalists, Additional Programming..." style="min-height:120px;"><%if (track.getCredits() != null) {
                                    out.print(track.getCredits());
                                }%></textarea>
                        </div>
                    </div>

                    <input type="hidden" value="EditTrack" name="target">
                    <button type="button" class="small invert" onclick="javascript:back();" style="margin-right: 10px;">Back</button>
                    <%if (!album.getIsPublished()) {%>
                    <button type="submit" class="small invert" style="margin-right: 10px;">Save Changes</button>
                    <button type="button" class="small invert md-trigger" style="margin-right: 10px;" data-modal="modal-delete">Delete Track</button>
                    <div class="md-modal md-effect-1" id="modal-delete">
                        <div class="md-content">
                            <h3>Are you sure?</h3>
                            <div style="text-align:center;">
                                <p>Confirm delete track?</p>
                                <button type="button" onclick="javascript:deleteTrack('<%=track.getId()%>')">Confirm</button>
                                <button class="md-close" type="button">Cancel</button>
                            </div>
                        </div>
                    </div>
                    <%}%>

                    <div class="clear"></div>
                </form>
                <%} else if (artist != null && track == null) {%>
                <p class="warning" id="errMsg">Ops. An error has occurred. <a href="#!/artist/tracks">Click here to try again.</a></p>
                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>


            <div class="md-overlay"></div>
            <script src="js/classie.js"></script>
            <script src="js/modalEffects.js"></script>
            <script>var polyfilter_scriptpath = '/js/';</script> 
            <script src="js/cssParser.js"></script>
            <script src="js/css-filters-polyfill.js"></script>
        </div>
    </section>
</section>
