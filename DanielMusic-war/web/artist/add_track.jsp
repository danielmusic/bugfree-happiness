<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Albums">
    <section class="content section">
        <div class="container">
            <article>
                <link rel="stylesheet" type="text/css" href="css/progressbar.css">
                <script>
                    var progress;
                    function upload(musicFileSize) {
                        var secs = musicFileSize / 25000;
                        var space = 100 / secs;
                        if (space > 100) {
                            space = 100;
                        }
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
                            
                            uploadprogress += Math.abs((Math.random() * 5) + space - 5);
                            // change progress width
                            if (uploadprogress < 100) {
                                progressBar(Math.round(uploadprogress), $('#progressBar'));
                            } else { // upload finished
                                // stop timer
                                clearInterval(progress);
                                setTimeout(function () {
                                    // hide progress bar
                                    progressBar(100, $('#progressBar'));
                                    $('#upload-title').text("Still Processing...");
                                    $('#upload-spinner').show();
                                    $('#upload-desc').text("Sorry, our music conversion is taking longer than expected, this page will automatically refresh when the upload is completed.");
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

                    function isMusic(filename) {
                        var ext = getExtension(filename);
                        switch (ext.toLowerCase()) {
                            case 'wav':
                                return true;
                        }
                        return false;
                    }

                    $(function () {
                        $('#submit').click(function () {
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
                                //successful
                                upload(musicFileSize);
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
                </script>
                <%@page import="EntityManager.Artist"%>
                <%
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    if (artist != null) {
                %>
                <div class="md-modal md-effect-1" id="modal-upload">
                    <div class="md-content" style="background-color: #000000">
                        <h3 id="upload-title">Uploading...</h3>
                        <div>
                            <p id="upload-desc">Please wait while we upload your music...</p>
                            <center><img id="upload-spinner" style="display: none;" src="../img/AjaxLoader.gif" alt=""/></center>
                            <div id="progressBar" class="default">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>

                <form id="uploadform" method="POST" action="MusicManagementController?target=AddTrack" enctype="multipart/form-data" class="form">
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
                            <input type="text" id="name" name="name" required>
                        </div>                        

                        <div class="col-1-2 last">
                            <label for="yearReleased"><strong>Year Released</strong> *</label>
                            <input type="number" id="yearReleased" name="yearReleased" min="1900" max="2050" required>
                        </div>
                    </div>


                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="trackNumber"><strong>Track no</strong> </label>
                            <input type="number" id="trackNumber" name="trackNumber">
                        </div>

                        <div class="col-1-2 last">
                            <label for="price"><strong>Price (SGD)</strong> *</label>
                            <input type="number" id="price" name="price" min="0" max="9999" step="0.01" size="4" title="CDA Currency Format - no dollar sign and no comma(s) - cents (.##) are optional" placeholder="Enter 0 if free" required/>
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
                            <label for="lyrics"><strong>Lyrics</strong> </label>
                            <textarea id="lyrics" name="lyrics"></textarea>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="credits"><strong>Credits</strong> </label>
                            <textarea id="credits" name="credits" placeholder="produced by, Mastering, Recording, Design, Photography, Instrumentalists, Additional Programming..." style="min-height:120px;"></textarea>
                        </div>
                    </div>

                    <!--                    <input type="hidden" value="AddTrack" name="target">-->
                    <input type="hidden" value="Artist" name="source">
                    <button type="button" class="small invert" onclick="javascript:back();" style="margin-right: 10px;">Back</button>
                    <button type="submit" name="submit" id="submit" class="small invert md-trigger" data-modal="modal-upload" style="margin-right: 10px;">Add Track</button>
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
