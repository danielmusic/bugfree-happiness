<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="sounds.sg | albums">
    <section class="content section">
        <div class="container">
            <article>
                <div class="md-modal md-effect-1" id="modal-profilePic">
                    <div class="md-content">
                        <h3>Standard Requirement</h3>
                        <div>
                            <p>Profile picture file requirement:</p>
                            <ul>
                                <li><strong>Ratio:</strong> Image must be in a ratio of 1:1 (ie 400px x 400px)</li>
                                <li><strong>Pixel:</strong> Image must be at least 300px x 300px</li>
                            </ul>
                            <div style="text-align:center;">
                                <button class="md-close" type="button">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
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
                <%@page import="EntityManager.Genre"%>
                <%@page import="java.util.List"%>
                <%@page import="EntityManager.Artist"%>
                <%
                    List<Genre> genres = (List<Genre>) (session.getAttribute("listOfGenres"));
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    if (artist != null && genres != null) {
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
                        <div class="col-1-3">
                            <label for="name"><strong>Title</strong> *</label>
                            <input type="text" id="name" name="name" required>
                        </div>

                        <div class="col-1-3">
                            <label for="yearReleased"><strong>Year Released</strong> *</label>
                     
                            <select name="yearReleased" id="yearReleased" style="width: 100%; height:42px;"></select>
                            <script>
                                var start = 1900;
                                var end = new Date().getFullYear();
                                var options = "";
                                for (var year = end; year >= start; year--) {
                                    options += "<option value='" + year + "'>" + year + "</option>";
                                }
                                document.getElementById("yearReleased").innerHTML = options;
                            </script>
                        </div>

                        <div class="col-1-3 last">
                            <label for="price"><strong>Price</strong> *</label>
                            <input type="number" id="price" name="price" min="0" max="9999" step="0.01" size="4" title="CDA Currency Format - no dollar sign and no comma(s) - cents (.##) are optional" placeholder="Enter 0 if free" required/>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="genre"><strong>Genre</strong> *</label>
                            <select name="genre" id="genre" style="width: 100%; height:42px;" required>
                                <option value="">Select</option>
                                <%
                                    for (int i = 0; i < genres.size(); i++) {
                                        out.write("<option value='" + genres.get(i).getId() + "'>" + genres.get(i).getName() + "</option>");
                                    }
                                %>
                            </select>
                        </div>

                        <div class="col-1-2 last">
                            <label><strong>Album Artwork</strong> <a class="md-trigger" data-modal="modal-profilePic">(?)</a></label>
                            <input type="file" id="picture" name="picture" style="width: 100%; height:42px;padding-top: 9px;">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="description"><strong>Album Description</strong> </label>
                            <textarea id="description" name="description" style="min-height:120px;"></textarea>
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
                    <div class="clear" style="margin-bottom: 20px;"></div>
                </form>
                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
                <div class="md-overlay"></div><!-- the overlay element -->
                <script src="js/classie.js"></script>
                <script src="js/modalEffects.js"></script>
                <script src="js/cssParser.js"></script>
            </article>
        </div>
    </section>
</section>
