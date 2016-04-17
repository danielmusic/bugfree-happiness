<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="sounds.sg">
    <ul id="demo1">
        <li style="margin-left: 0;"><a href="#"><img src="placeholders/Terrestrea1.jpg"></a></li>
        <li style="margin-left: 0;"><a href="#"><img src="placeholders/Terrestrea2.jpg"></a></li>
        <li style="margin-left: 0;"><a href="#"><img src="placeholders/Terrestrea3.jpg"></a></li>
    </ul>

    <div class="container" style="margin-top: 80px;">
        <script>
            function loadArtist2(id) {
                url = "./MusicController?target=GetArtistByID";
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    data: {'id': id},
                    dataType: "text",
                    success: function (val) {
                        var json = JSON.parse(val);
                        if (json.result) {
                            window.location.href = "#!/artists";
                        }
                    },
                    error: function (xhr, status, error) {
                        document.getElementById("errMsg").style.display = "block";
                        document.getElementById('errMsg').innerHTML = error;
                        hideLoader();
                        ajaxResultsError(xhr, status, error);
                    }
                });
            }

            function addTrackToCart(trackID) {
                url = "./MusicManagementController?target=AddTrackToShoppingCart";
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    data: {'trackID': trackID},
                    success: function () {
                        window.location.href = "#!/cart";
                    },
                    error: function (xhr, status, error) {
                        document.getElementById("errMsg").style.display = "block";
                        document.getElementById('errMsg').innerHTML = error;
                        hideLoader();
                        ajaxResultsError(xhr, status, error);
                    }
                });
            }

            function addAlbumToCart(albumID) {
                url = "./MusicManagementController?target=AddAlbumToShoppingCart";
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    data: {'albumID': albumID},
                    success: function () {
                        window.location.href = "#!/cart";
                    },
                    error: function (xhr, status, error) {
                        document.getElementById("errMsg").style.display = "block";
                        document.getElementById('errMsg').innerHTML = error;
                        hideLoader();
                        ajaxResultsError(xhr, status, error);
                    }
                });
            }

            function copyArtistURL() {
                var copyTextarea = document.querySelector('.artistURL');
                copyTextarea.select();
                document.execCommand('copy');
            }

            function goToProfile(id) {
                url = "./MusicController?target=GetArtistByID";
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    data: {'id': id},
                    dataType: "text",
                    success: function (val) {
                        var json = JSON.parse(val);
                        if (json.result) {
                            window.location.href = "#!/artists";
                        }
                    },
                    error: function (xhr, status, error) {
                        document.getElementById("errMsg").style.display = "block";
                        document.getElementById('errMsg').innerHTML = error;
                        hideLoader();
                        ajaxResultsError(xhr, status, error);
                    }
                });
            }
        </script>

        <div id="main" class="release main-right main-medium" style="margin-bottom: 0px;">
            <article style="margin-bottom: 0px;">
                <p class="error" id="errMsg" style="display:none;"></p>
                <p class="success" id="goodMsg" style="display:none;"></p>
                <p>Featured Artist of The Month</p>
                <h1>Terrestrea</h1>
            </article>
        </div>

        <div class="sidebar main-right main-medium">
            <div class="widget details-widget">
                <a href="http://sounds.sg.storage.googleapis.com/images/album/906/albumart/Sunset Memory.jpg" title="Terrestrea" data-lightbox="lightboximages/artist/profile/profilepictures_25">
                    <span class="img">
                        <img src="http://sounds.sg.storage.googleapis.com/images/album/906/albumart/Sunset Memory.jpg">
                    </span>
                </a>
                <div class="details-meta">
                    <ul class="details-list">
                        <li>
                            <span class="label">Genre</span>
                            <div class="data">Rock</div>
                        </li>
                        <li>
                            <span class="label" style="margin-bottom: 5px;">URL</span>
                            <input type="text" class="artistURL" value="http://sounds.sg/music/Terrestrea" style="width: 100%; height: 30px; font-size: 12px; padding-top: 5px; padding-bottom: 5px;">
                            <span title="click here to copy" class="fa fa-clipboard fa-1x" style="cursor: pointer; float: right; z-index: 2;position: relative;margin-right: 6px;margin-top: -23px;" onclick="javascript:copyArtistURL();"></span>
                            <!--<button class="" type="button" style="float: right; z-index: 2;position: relative;margin-right: 6px;margin-top: -40px;" onclick="javascript:copyArtistURL();">Copy URL</button>-->
                        </li>
                    </ul>
                </div>
                <!-- Details Share -->
                <div class="details-social-box">
                    <a href="https://www.facebook.com/terrestrea/" target="_blank"><i class="icon icon-facebook"></i></a>
                    <a href="https://www.instagram.com/terrestrea/" target="_blank"><i class="fa fa-instagram "></i></a>
                </div>
            </div>
            <form name="homeForm">
                <p>
                    <a class="btn invert" onclick="javascript:goToProfile(902)" style="width: 100%;">Go To Profile</a>
                    <a class="btn invert sp-play-list" data-id="release-list" style="width: 100%;">Play All Tracks</a>
            </form>

        </div>

        <div id="main" class="release main-right main-medium">
            <article>
                <p>&quot;Terrestrial means 'of the earth', Astrea means 'of the stars'. We put that together and got Terrestrea &mdash; we're somewhere in between.&quot;</p>

                <!--start of hidden playlist-->
                <ul id="release-list" class="tracklist" style="display:none;">

                    <li>
                        <div class="track-details">
                            <div class="track-buttons">
                                <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/902/906/907/128/Sunset Memory - Final Mix.mp3" data-cover="http://sounds.sg.storage.googleapis.com/images/album/906/albumart/Sunset Memory.jpg" data-artist="Terrestrea" data-artist_url="javascript:loadArtist2(902);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(907);" data-shop_target="_self">
                                    <i class="icon icon-plus">
                                        <span style="display: none;">
                                            <span class="track-title">Sunset Memory</span>
                                            <span class="track-artists">Terrestrea</span>
                                        </span>
                                    </i>
                                </a>
                            </div>
                        </div>
                    </li>

                    <li>
                        <div class="track-details">
                            <div class="track-buttons">
                                <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/902/915/916/128/Furnace.mp3" data-cover="http://sounds.sg.storage.googleapis.com/images/album/915/albumart/Furnace.jpg" data-artist="Terrestrea" data-artist_url="javascript:loadArtist2(902);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(916);" data-shop_target="_self">
                                    <i class="icon icon-plus">
                                        <span style="display: none;">
                                            <span class="track-title">Furnace</span>
                                            <span class="track-artists">Terrestrea</span>
                                        </span>
                                    </i>
                                </a>
                            </div>
                        </div>
                    </li>
                </ul>
                <!--end of hidden playlist-->

                <ul id="release-list" class="tracklist">
                    <div class="toggle">
                        <li>
                            <div class="track-details">
                                <a class="track" href="http://sounds.sg.storage.googleapis.com/images/album/906/albumart/Sunset Memory.jpg" title="Terrestrea" data-lightbox="lightboxTerrestrea">
                                    <img class="track-cover" title="Terrestrea" alt="Track Cover" src="http://sounds.sg.storage.googleapis.com/images/album/906/albumart/Sunset Memory.jpg">
                                    <span>&nbsp;&nbsp;</span>
                                </a>

                                <span class="track"> 
                                    <span style="display: block; margin-left: 40px;">Sunset Memory</span>
                                    <span class="track-artists" style="margin-left: 40px;">Rock</span>
                                </span>


                                <div class="track-buttons">
                                    <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/images/album/906/albumart/Sunset Memory.jpg" data-cover="http://sounds.sg.storage.googleapis.com/images/album/906/albumart/Sunset Memory.jpg" 
                                       data-artist="Terrestrea" 
                                       data-artist_url="javascript:loadArtist2(902);" 
                                       data-artist_target="_self" 
                                       data-shop_url="javascript:addTrackToCart(907);" 
                                       data-shop_target="_self">
                                        <i class="icon icon-plus">
                                            <span style="display: none;">
                                                <span class="track-title">Sunset Memory</span>
                                                <span class="track-artists">Terrestrea</span>
                                            </span>
                                        </i>
                                    </a>

                                    <a class="track sp-play-track" style="margin-left: 6px;" href="http://sounds.sg.storage.googleapis.com/music/902/906/907/128/Sunset Memory - Final Mix.mp3" data-cover="http://sounds.sg.storage.googleapis.com/images/album/906/albumart/Sunset Memory.jpg" 
                                       data-artist="Terrestrea" 
                                       data-artist_url="javascript:loadArtist2(902);" 
                                       data-artist_target="_self" 
                                       data-shop_url="javascript:addTrackToCart(907);" 
                                       data-shop_target="_self">
                                        <i class="icon icon-play2">
                                            <span style="display: none;">
                                                <span class="track-title">Sunset Memory</span>
                                                <span class="track-artists">Terrestrea</span>
                                            </span>
                                        </i>
                                    </a>

                                    <i style="cursor: pointer;" class="icon icon-menu2 toggle-title">
                                        <span style="display: none;"></span>
                                    </i>

                                    <a style="cursor: pointer;" onclick="addTrackToCart(906)">
                                        <i class="icon icon-cart"></i>
                                    </a>

                                    <span style="margin-left: 6px;">
                                        0.99
                                    </span>
                                </div>
                            </div>

                            <div class="toggle-content" style="margin-left: 20px;margin-right: 20px;">
                                <p>As I listen to the birds<br>  In the peace of the evening<br>  I remember<br>  A face I used to know <br>  How she would tremble<br>  When I held my fingers to her lips<br>  A breeze cuts through the quiet<br>  Safety is so cold, it&rsquo;s so cold<br>  <br>  I can&rsquo;t see the wounds I&rsquo;ve caused<br>  But you know I&rsquo;d try to heal them<br>  Cause without you it&rsquo;s paradise lost<br>  With no one to share it&rsquo;s secrets<br>  The spaces of this place we shared<br>  And the faces of you<br>  I&rsquo;d never see again<br>  <br>  The fading light is soft<br>  And the heat it makes me tingle<br>  I try to force a smile<br>  But the night is coming<br>  Why is there still an ember?<br>  Why can&rsquo;t I just kill the flames?<br>  My hand is steady<br>  But I&rsquo;m just not ready yet<br>  <br>  I can&rsquo;t see the wounds I&rsquo;ve caused<br>  But you know I&rsquo;d try to heal them<br>  Cause without you it&rsquo;s paradise lost<br>  With no one to share it&rsquo;s secrets<br>  The spaces of this place we shared<br>  And the faces of you<br>  I&rsquo;d never see again<br>  <br>  My heart is an open grave<br>  And everybody misbehaves<br>  But my soul,<br>  It grows with every word that I proclaim!<br>  <br>  I can&rsquo;t see the wounds I&rsquo;ve caused<br>  But you know I&rsquo;d try to heal them<br>  Cause without you it&rsquo;s paradise lost<br>  With no one to share it&rsquo;s secrets<br>  The spaces of this place we shared<br>  And the faces of you<br>  I&rsquo;d never see again</p>
                                <a style="cursor: pointer" onclick="window.open('./MusicController?target=Lyrics&id=907', '_blank', 'width=600,height=760')">
                                    Open in new window
                                </a>
                            </div>
                        </li>
                    </div>
                </ul>

                <p>The band's first original, post-rock ballad Sunset Memory, grew out of a guitar riff by Imran. Building on that riff with Jared, the duo worked it into the song that it is now, roping in friends from school: Ben, Kenny, and Irsyad. While they play in the same band, Imran is quick to point out that they listen to a variety of music, and in this manner the band is a confluence of their diverse influences. Citing giants from the post-hardcore era such as Underoath and Funeral For A Friend as their roots, they subsequently got into more experimental acts like Periphery and Toe, and are currently building elements such as poly-rhythmic patterns into their own sound.</p>

                <p>&quot;The funny thing is that none of our songs have turned out the way we imagined. Let's say I write some lyrics or have an idea. Once we jam it out it becomes something completely different, and that's one of the best things about being in a band.&quot;</p>

                <p>The band is busy refining existing material and has a new song in the works. Titled Furnace, they describe this second single as less reflective but more melodic. Where Sunset Memory drew on personal experience, this new track poses a challenge to the listener. Key songwriter Jared says it's about reaching a point where a decision must be made.</p>

                <p>Their current goals include releasing an EP at the end of the year and making Terrestrea sound like Terrestrea. We wish them the very best.</p>
            </article>
        </div>

    </div>
</section>

<script type="text/javascript">
    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-66150326-1']);
    var d = document.location.pathname + document.location.search + document.location.hash;
    _gaq.push(['_trackPageview', d]);

    (function () {
        var ga = document.createElement('script');
        ga.type = 'text/javascript';
        ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(ga, s);
    })();
</script>
<script src="../js/slippry-1.3.1/slippry.min.js"></script>
<link rel="stylesheet" type="text/css" href="../js/slippry-1.3.1/slippry.css"/>
<script>


    $(function () {
        var demo1 = $("#demo1").slippry({
            pager: false,
            controls: false
                    // transition: 'fade',
                    // useCSS: true,
                    // speed: 1000,
                    // pause: 3000,
                    // auto: true,
                    // preload: 'visible',
                    // autoHover: false
        });
    });
</script>
<!--<link rel="stylesheet" href="../js/cssscript-slider/slider.css"/>-->