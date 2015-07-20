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

                    function deleteAlbum(id) {
                        window.location.href = "MusicManagementController?target=DeleteAlbum&id=" + id;
                    }
                </script>
                <%@page import="EntityManager.Genre"%>
                <%@page import="EntityManager.Album"%>
                <%@page import="java.util.List"%>
                <%@page import="EntityManager.Artist"%>
                <%
                    List<Genre> genres = (List<Genre>) (session.getAttribute("listOfGenres"));
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    Album album = (Album) (session.getAttribute("album"));
                    String disableFlag = "";
                    if (album != null && album.getIsPublished()) {
                        disableFlag = "disabled";
                    }
                    if (artist != null && album != null && genres != null) {
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
                        <div class="col-1-3">
                            <label for="name"><strong>Title</strong> *</label>
                            <input type="text" id="name" name="name" value="<%=album.getName()%>" required <%=disableFlag%>>
                        </div>

                        <div class="col-1-3">
                            <label for="yearReleased"><strong>Year Released</strong> *</label>
                            <input type="number" id="yearReleased" name="yearReleased" value="<%=album.getYearReleased()%>" min="1900" max="2050" required <%=disableFlag%>>
                        </div>

                        <div class="col-1-3 last">
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
                        <div class="col-1-2">
                            <label for="genre"><strong>Genre</strong> *</label>
                            <select name="genre" id="genre" style="width: 100%; height:40px;" required>
                                <option value="">Select</option>
                                <%
                                    for (int i = 0; i < genres.size(); i++) {
                                        if (album.getGenreName() != null && genres.get(i).getName().equals(album.getGenreName())) {
                                            out.write("<option selected value='" + genres.get(i).getId() + "'>" + genres.get(i).getName() + "</option>");
                                        } else {
                                            out.write("<option value='" + genres.get(i).getId() + "'>" + genres.get(i).getName() + "</option>");
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <div class="col-1-2 last">
                            <label for="picture"><strong>Album Artwork</strong> </label>
                            <input type="file" id="picture" name="picture" <%=disableFlag%> style="width: 100%; height:40px;padding-top: 9px;">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="description"><strong>Album Description</strong> </label>
                            <textarea id="description" name="description" style="min-height:120px;" <%=disableFlag%>><%if (album.getDescription() != null) {
                                    out.print(album.getDescription());
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


                    <input type="hidden" value="UpdateAlbum" name="target">
                    <input type="hidden" value="<%=album.getId()%>" name="id">
                    <button type="button" class="small invert" onclick="javascript:back();" style="margin-right: 10px;">Back</button>

                    <%if (!album.getIsPublished()) {%>
                    <button type="submit" class="small invert" style="margin-right: 10px;">Save Changes</button>
                    <button type="button" class="small invert md-trigger" style="margin-right: 10px;" data-modal="modal-delete">Delete Album</button>
                    <div class="md-modal md-effect-1" id="modal-delete">
                        <div class="md-content">
                            <h3>Are you sure?</h3>
                            <div style="text-align:center;">
                                <p>All the associated tracks will also be removed.</p>
                                <button type="button" onclick="javascript:deleteAlbum('<%=album.getId()%>')">Confirm</button>
                                <button class="md-close" type="button">Cancel</button>
                            </div>
                        </div>
                    </div>
                    <%}%>

                    <div class="clear"></div>
                </form>
                <%} else if (artist != null && album == null) {%>
                <p class="warning" id="errMsg">Ops. An error has occurred. <a href="#!/artist/albums">Click here to try again.</a></p>
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
