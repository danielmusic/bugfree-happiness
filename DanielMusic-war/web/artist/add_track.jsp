<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Albums">

    <section class="content section">
        <div class="container">
            <article>
                <script>
                    var progress;
                    function upload(musicFileSize) {
                        var secs = musicFileSize / 500000;
                        var space = 100 / secs;
                        // avoid concurrent processing
                        if (progress)
                            return;
                        //uploadform.action = 'MusicManagementController?target=AddTrack&time=' + time;
                        //uploadform.target = 'target-frame';
                        //uploadform.submit();
                        startProgressbar(space);
                    }
                    function startProgressbar(space) {
                        // display progress bar
                        var uploadprogress = 0;
                        $('.progress-bar').css('display', 'block');
                        // start timer
                        progress = setInterval(function () {
                            // ask progress
                            // get progress from response data
                            uploadprogress += space;
                            // change progress width
                            if (uploadprogress < 100) {
                                $('.progress').css('width', uploadprogress + 'px');
                            } else { // upload finished
                                // stop timer
                                clearInterval(progress);
                                setTimeout(function () {
                                    // hide progress bar
                                    $('.progress-bar').css('display', '');
                                    $('.progress').css('width', '');
                                    // clear timer variable
                                    progress = null;
                                }, 1000);
                            }
                        }, 1000);
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
                            alert("hello");
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
                <form id="uploadform" method="POST" enctype="multipart/form-data" class="form">
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
                    <button type="button" name="submit" id="submit" class="small invert" style="margin-right: 10px;">Add Track</button>
                    <!-- progress bar -->
                    <div class="progress-bar">
                        <div class="progress"></div>
                    </div>
                    <div class="clear"></div>
                </form>
                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>
        </div>
    </section>
</section>
