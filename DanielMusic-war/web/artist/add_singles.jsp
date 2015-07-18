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
                            <span class="entry-comments">Add Single</span>
                        </div>                   
                    </div>

                    <h2>Singles details</h2>

                    <div class="row clearfix">
                        <div class="col-1-3">
                            <label for="name"><strong>Title</strong> *</label>
                            <input type="text" id="name" name="name" required>
                        </div>

                        <div class="col-1-3">
                            <label for="yearReleased"><strong>Year Released</strong> *</label>
                            <input type="number" id="yearReleased" name="yearReleased" min="1900" max="2050" required>
                        </div>

                        <div class="col-1-3 last" style="margin-bottom: 0px;">
                            <label for="price"><strong>Price</strong> *</label>
                            <input type="number" id="price" name="price" min="0" max="9999" step="0.01" size="4" title="CDA Currency Format - no dollar sign and no comma(s) - cents (.##) are optional" placeholder="Enter 0 if free" required/>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="picture"><strong>Artwork</strong> </label>
                            <input type="file" id="picture" name="picture">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="music"><strong>Music * (WAV format, 44.1 kHz, 16bit)</strong></label>
                            <input type="file" id="music" name="music" required>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="description"><strong>Description</strong> </label>
                            <textarea id="description" name="description" style="min-height:120px;"></textarea>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="lyrics"><strong>Lyrics</strong> </label>
                            <textarea id="lyrics" name="lyrics"></textarea>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="credits"><strong>Credits</strong> </label>
                            <textarea id="credits" name="credits" placeholder="produced by, Mastering, Recording, Design, Photography..." style="min-height:120px;"></textarea>
                        </div>
                    </div>

                    <input type="hidden" value="AddSingles" name="target">
                    <button type="button" class="small invert" onclick="javascript:back();" style="margin-right: 10px;">Back</button>
                    <button type="submit" class="small invert">Add Singles</button>
                    <div class="clear"></div>
                </form>
                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>
        </div>
    </section>
</section>
