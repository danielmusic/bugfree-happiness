<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Albums">
    <section class="content section">
        <div class="container">
            <link rel="stylesheet" type="text/css" href="css/progressbar.css">
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

                <div class="md-modal md-effect-1" id="modal-upload">
                    <div class="md-content">
                        <h3>Uploading...</h3>
                        <div>
                            <p>Please wait while we process your music...</p>
                            <div id="progressBar" class="default">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <script>
                    var progress;
                    function upload(musicFileSize) {
                        var secs = musicFileSize / 100000;
                        var space = 100 / secs;
                        if (progress) {
                            return;
                        }
                        startProgressbar(space);
                    }

                    function startProgressbar(space) {
                        // display progress bar
                        var uploadprogress = 0;
                        $("#modal-upload").addClass("md-show");
                        //$('#uploadProgress').css('display', 'block');
                        // start timer
                        progress = setInterval(function () {
                            // ask progress
                            // get progress from response data
                            uploadprogress += (Math.random() * 5) + space - 5;
                            // change progress width
                            if (uploadprogress < 100) {
                                progressBar(Math.round(uploadprogress), $('#progressBar'));
                            } else { // upload finished
                                // stop timer
                                clearInterval(progress);
                                setTimeout(function () {
                                    // hide progress bar
                                    progressBar(100, $('#progressBar'));
                                    progress = null;
                                }, 1000);
                            }
                        }, 1000);
                    }

                    function progressBar(percent, $element) {
                        var progressBarWidth = percent * $element.width() / 100;
                        $element.find('div').animate({width: progressBarWidth}, 500).html(percent + "%&nbsp;");
                    }

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
                                var musicFile = $('#music');
                                var musicFileSize = $('#music')[0].files[0].size;

                                if (!isMusic(musicFile.val())) {
                                    document.getElementById("errMsg").style.display = "block";
                                    document.getElementById('errMsg').innerHTML = "Only wav format song is allowed";
                                    window.scrollTo(0, 0);
                                    return false;
                                }
                                if (musicFileSize > 100000000) {
                                    document.getElementById("errMsg").style.display = "block";
                                    document.getElementById('errMsg').innerHTML = "Music size must be below 100mb.";
                                    window.scrollTo(0, 0);
                                    return false;
                                }

                                upload(musicFileSize);

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
                            <span class="entry-comments">Add Single</span>
                        </div>                   
                    </div>

                    <h2>Single details</h2>

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
                            <label for="price"><strong>Price (SGD)</strong> *</label>
                            <input type="number" id="price" name="price" min="0" max="9999" step="0.01" size="4" title="CDA Currency Format - no dollar sign and no comma(s) - cents (.##) are optional" placeholder="Enter 0 if free" required/>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="genre"><strong>Genre</strong> *</label>
                            <select name="genre" id="genre" style="width: 100%; height:40px;" required>
                                <option value="">Select</option>
                                <%
                                    for (int i = 0; i < genres.size(); i++) {
                                        out.write("<option value='" + genres.get(i).getId() + "'>" + genres.get(i).getName() + "</option>");
                                    }
                                %>
                            </select>
                        </div>

                        <div class="col-1-2 last">
                            <label><strong>Artwork</strong> <a class="md-trigger" data-modal="modal-profilePic">(?)</a></label>
                            <input type="file" id="picture" name="picture" style="width: 100%; height:40px;padding-top: 9px;">
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
                    <button type="submit" class="small invert">Add Single</button>
                    <div class="clear"></div>
                </form>
                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
                <div class="md-overlay"></div>
            </article>
        </div>
    </section>
</section>
