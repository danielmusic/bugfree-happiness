<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="sounds.sg">
    <!-- ############################# Sections ############################# -->
    <div class="slider-container">
        <ul id="demo1">
            <li style="margin-left: 0;"><a href="#"><img src="placeholders/Terrestrea1.jpg"></a></li>
            <li style="margin-left: 0;"><a href="#"><img src="placeholders/Terrestrea2.jpg"></a></li>
            <li style="margin-left: 0;"><a href="#"><img src="placeholders/Terrestrea3.jpg"></a></li>
        </ul>
    </div> 
    
    <div class="container">
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
            </script>

            <div id="main" class="release main-left main-medium" style="margin-bottom: 0px;">
                <article style="margin-bottom: 0px;">
                    <p class="error" id="errMsg" style="display:none;"></p>
                    <p class="success" id="goodMsg" style="display:none;"></p>

                    <h1>One Direction</h1>
                </article>
            </div>

            <div class="sidebar main-left main-medium">
                <div class="widget details-widget">
                    
                    <a href="http://sounds.sg.storage.googleapis.com/images/artist/profile/profilepictures_25" title="One Direction" data-lightbox="lightboximages/artist/profile/profilepictures_25">
                        <span class="img">
                            <img src="http://sounds.sg.storage.googleapis.com/images/artist/profile/profilepictures_25">
                        </span>
                    </a>
                    

                    <div class="details-meta">
                        <ul class="details-list">
                            <li>
                                <span class="label">Genre</span>
                                <div class="data">Jazz</div>
                            </li>
                            
                            <li>
                                <span class="label" style="margin-bottom: 5px;">URL</span>
                                
                                <input type="text" class="artistURL" value="http://sounds.sg/music/One+Direction" style="width: 100%; height: 30px; font-size: 12px; padding-top: 5px; padding-bottom: 5px;">
                                <span title="click here to copy" class="fa fa-clipboard fa-1x" style="cursor: pointer; float: right; z-index: 2;position: relative;margin-right: 6px;margin-top: -23px;" onclick="javascript:copyArtistURL();"></span>
                                <!--<button class="" type="button" style="float: right; z-index: 2;position: relative;margin-right: 6px;margin-top: -40px;" onclick="javascript:copyArtistURL();">Copy URL</button>-->
                                
                            </li>
                        </ul>
                    </div>
                    <!-- Details Share -->
                    <div class="details-social-box">
                        
                        <a href="http://a" target="_blank"><i class="icon icon-facebook"></i></a>
                            

                        
                        <a href="http://a" target="_blank"><i class="icon icon-twitter"></i></a>
                            

                        
                        <a href="http://a" target="_blank"><i class="fa fa-instagram "></i></a>
                            

                        
                        <a href="http://a" target="_blank"><i class="icon icon-IE"></i></a>
                            
                    </div>
                </div>

                <p>
                    <a class="btn invert sp-play-list" data-id="release-list" style="width: 100%;">Play All Tracks</a>
                    <a class="btn invert sp-add-list" data-id="release-list" style="width: 100%;">Add All Tracks to Playlist</a>
                </p>
            </div>

            <div id="main" class="release main-left main-medium">
                <article>
                    <!-- tabs -->
                    <div id="tabs" class="tabs-wrap">
                        <!-- tabs navigation -->
                        <ul class="tabs">
                            <li><a href="#tab-bio" class="active-tab">Biography</a></li>
                            <li><a href="#tab-releases" class="">Albums</a></li>
                        </ul>
                        <!-- /tabs navigation -->
                        <!-- tab content -->
                        <div id="tab-bio" class="tab-content" style="display: block;">
                            


                            
                            <h2>About</h2>
                            <p> One Direction (commonly abbreviated to 1D) is a UK-Irish boy band. The original 5-member group was made up of Liam Payne (born 29 August 1993) from Wolverhampton, Louis Tomlinson (born 24 December 1991) from Doncaster, Niall Horan (born 13th September 1993) from Mullingar, Westmeath, Ireland, Harry Styles, (born 1 February 1994) from Holmes Chapel, Cheshire and Zayn Malik, (born 12 January 1993) from East Bowling, Bradford: Zayn Malik announced he was to leave the band on March 25, 2015, and the remainder of the group confirmed they would continue as a 4-piece band. They applied as solo candidates on the 2010 edition of the X Factor, but the judges, after a suggestion by Simon Cowell, put them in a band in London, UK; therefore qualifying for the Groups category.</p>
                            

                            
                            <h2>Influences</h2>
                            <p> One Direction's first single, What Makes You Beautiful, received its first airplay on August 10, 2011 and was released a month later on September 11, 2011. It was the fastest selling single of the year 2011 and peaked at #1 on the UK Singles Chart. The boys released their second single Gotta Be You on November 13th peaking at #3 on the UK Singles Chart. Their album Up All Night followed on November 21st reaching #2 on the UK Albums Chart, narrowly missing out on #1 to Rihanna. When their album Up All Night was released in the USA, it debuted at #1 on the Billboard charts, setting the record for the highest-selling debut album by a British boy band.</p>
                            

                            
                            <h2>Contact Email</h2>
                            <p> one-direction@gmail.com</p>
                            


                        </div>
                        <!-- /tab content -->
                        <!-- play all tracks content/add all tracks to playlist button-->
                        <ul id="release-list" class="tracklist" style="display:none;">
                            
                            <li>
                                <div class="track-details">
                                    <div class="track-buttons">
                                        <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/29/37/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(37);" data-shop_target="_self">
                                            <i class="icon icon-plus">
                                                <span style="display: none;">
                                                    <span class="track-title">Live While We're Young</span>
                                                    <span class="track-artists">One Direction</span>
                                                </span>
                                            </i>
                                        </a>
                                    </div>
                                </div>
                            </li>
                            
                            <li>
                                <div class="track-details">
                                    <div class="track-buttons">
                                        <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/29/39/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(39);" data-shop_target="_self">
                                            <i class="icon icon-plus">
                                                <span style="display: none;">
                                                    <span class="track-title">Kiss You</span>
                                                    <span class="track-artists">One Direction</span>
                                                </span>
                                            </i>
                                        </a>
                                    </div>
                                </div>
                            </li>
                            
                            <li>
                                <div class="track-details">
                                    <div class="track-buttons">
                                        <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/29/40/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(40);" data-shop_target="_self">
                                            <i class="icon icon-plus">
                                                <span style="display: none;">
                                                    <span class="track-title">Little Things</span>
                                                    <span class="track-artists">One Direction</span>
                                                </span>
                                            </i>
                                        </a>
                                    </div>
                                </div>
                            </li>
                            
                            <li>
                                <div class="track-details">
                                    <div class="track-buttons">
                                        <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/29/41/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(41);" data-shop_target="_self">
                                            <i class="icon icon-plus">
                                                <span style="display: none;">
                                                    <span class="track-title">C'mon, C'mon</span>
                                                    <span class="track-artists">One Direction</span>
                                                </span>
                                            </i>
                                        </a>
                                    </div>
                                </div>
                            </li>
                            
                            <li>
                                <div class="track-details">
                                    <div class="track-buttons">
                                        <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/29/42/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(42);" data-shop_target="_self">
                                            <i class="icon icon-plus">
                                                <span style="display: none;">
                                                    <span class="track-title">Last First Kiss</span>
                                                    <span class="track-artists">One Direction</span>
                                                </span>
                                            </i>
                                        </a>
                                    </div>
                                </div>
                            </li>
                            
                            <li>
                                <div class="track-details">
                                    <div class="track-buttons">
                                        <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/30/33/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(33);" data-shop_target="_self">
                                            <i class="icon icon-plus">
                                                <span style="display: none;">
                                                    <span class="track-title">What Makes You Beautiful</span>
                                                    <span class="track-artists">One Direction</span>
                                                </span>
                                            </i>
                                        </a>
                                    </div>
                                </div>
                            </li>
                            
                            <li>
                                <div class="track-details">
                                    <div class="track-buttons">
                                        <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/30/34/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(34);" data-shop_target="_self">
                                            <i class="icon icon-plus">
                                                <span style="display: none;">
                                                    <span class="track-title">Gotta Be You</span>
                                                    <span class="track-artists">One Direction</span>
                                                </span>
                                            </i>
                                        </a>
                                    </div>
                                </div>
                            </li>
                            
                            <li>
                                <div class="track-details">
                                    <div class="track-buttons">
                                        <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/30/35/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(35);" data-shop_target="_self">
                                            <i class="icon icon-plus">
                                                <span style="display: none;">
                                                    <span class="track-title">One Thing</span>
                                                    <span class="track-artists">One Direction</span>
                                                </span>
                                            </i>
                                        </a>
                                    </div>
                                </div>
                            </li>
                            
                            <li>
                                <div class="track-details">
                                    <div class="track-buttons">
                                        <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/30/43/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(43);" data-shop_target="_self">
                                            <i class="icon icon-plus">
                                                <span style="display: none;">
                                                    <span class="track-title">More Than This</span>
                                                    <span class="track-artists">One Direction</span>
                                                </span>
                                            </i>
                                        </a>
                                    </div>
                                </div>
                            </li>
                            
                            <li>
                                <div class="track-details">
                                    <div class="track-buttons">
                                        <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/31/32/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/images/album/31/albumart/Story of My Life.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(32);" data-shop_target="_self">
                                            <i class="icon icon-plus">
                                                <span style="display: none;">
                                                    <span class="track-title">Story of My Life</span>
                                                    <span class="track-artists">One Direction</span>
                                                </span>
                                            </i>
                                        </a>
                                    </div>
                                </div>
                            </li>
                            
                        </ul>
                        <!-- tab content -->
                        <div id="tab-releases" class="tab-content" style="display: none;">
                            
                            <h2 id="album_29">Take Me Home <div style="font-weight: 100; display: inline;">(2012)</div></h2> 
                            <p>
                                Take Me Home is the second studio album by English-Irish boy band One Direction, released globally in November 2012 by Syco Records and Columbia Records (Sony Music Entertainment). After extensive promotional appearances and touring in North America and Oceania in support of their debut album, One Direction began recording the album in May 2012. 
                            </p>
                            <p>
                                Price: 
                                8.00
                            </p>
                            
                            <a class="btn sp-add-list invert" onclick="addAlbumToCart(29)">Add Album to Cart</a>
                            
                            <ul id="release-list" class="tracklist">
                                

                                <div class="toggle">
                                    <li>
                                        <div class="track-details">
                                            <a class="track" href="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" title="One Direction" data-lightbox="lightboxOne Direction">
                                                <img class="track-cover" title="One Direction" alt="Track Cover" src="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg">
                                                <span>&nbsp;&nbsp;</span>
                                            </a>

                                            <span class="track"> 
                                             <!--     <img class="track-cover" src="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg">-->
                                                <span style="display: block; margin-left: 40px;">Live While We're Young</span>
                                                <span class="track-artists" style="margin-left: 40px;">Jazz</span>
                                            </span>


                                            <div class="track-buttons">
                                                <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/29/37/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(37);" data-shop_target="_self">
                                                    <i class="icon icon-plus">
                                                        <span style="display: none;">
                                                            <span class="track-title">Live While We're Young</span>
                                                            <span class="track-artists">One Direction</span>
                                                        </span>
                                                    </i>
                                                </a>

                                                <span id="btnPlay37">
                                                    <a class="track sp-play-track" style="margin-left: 6px;" href="http://sounds.sg.storage.googleapis.com/music/25/29/37/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(37);" data-shop_target="__self">
                                                        <i class="icon icon-play2">
                                                            <span style="display: none;">
                                                                <span class="track-title">Live While We're Young</span>
                                                                <span class="track-artists">One Direction</span>
                                                            </span>
                                                        </i>
                                                    </a>
                                                </span>

                                                <i style="cursor: pointer;" class="icon icon-menu2 toggle-title">
                                                    <span style="display: none;"></span>
                                                </i>
                                                
                                                <a style="cursor: pointer;" onclick="addTrackToCart(37)">
                                                    
                                                        <i class="icon icon-cart"></i>
                                                    </a>

                                                    <span style="margin-left: 6px;">
                                                        1.50
                                                    </span>
                                            </div>
                                        </div>

                                        <div class="toggle-content">
                                             
                                            <br><br><a style="cursor: pointer" onclick="window.open('./MusicController?target=Lyrics&amp;id=37', '_blank', 'width=600,height=760')">Open in new window</a>
                                        </div>
                                    </li>
                                </div>

                                

                                <div class="toggle">
                                    <li>
                                        <div class="track-details">
                                            <a class="track" href="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" title="One Direction" data-lightbox="lightboxOne Direction">
                                                <img class="track-cover" title="One Direction" alt="Track Cover" src="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg">
                                                <span>&nbsp;&nbsp;</span>
                                            </a>

                                            <span class="track"> 
                                             <!--     <img class="track-cover" src="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg">-->
                                                <span style="display: block; margin-left: 40px;">Kiss You</span>
                                                <span class="track-artists" style="margin-left: 40px;">Jazz</span>
                                            </span>


                                            <div class="track-buttons">
                                                <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/29/39/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(39);" data-shop_target="_self">
                                                    <i class="icon icon-plus">
                                                        <span style="display: none;">
                                                            <span class="track-title">Kiss You</span>
                                                            <span class="track-artists">One Direction</span>
                                                        </span>
                                                    </i>
                                                </a>

                                                <span id="btnPlay39">
                                                    <a class="track sp-play-track" style="margin-left: 6px;" href="http://sounds.sg.storage.googleapis.com/music/25/29/39/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(39);" data-shop_target="__self">
                                                        <i class="icon icon-play2">
                                                            <span style="display: none;">
                                                                <span class="track-title">Kiss You</span>
                                                                <span class="track-artists">One Direction</span>
                                                            </span>
                                                        </i>
                                                    </a>
                                                </span>

                                                <i style="cursor: pointer;" class="icon icon-menu2 toggle-title">
                                                    <span style="display: none;"></span>
                                                </i>
                                                
                                                <a style="cursor: pointer;" onclick="addTrackToCart(39)">
                                                    
                                                        <i class="icon icon-cart"></i>
                                                    </a>

                                                    <span style="margin-left: 6px;">
                                                        1.30
                                                    </span>
                                            </div>
                                        </div>

                                        <div class="toggle-content">
                                             
                                            <br><br><a style="cursor: pointer" onclick="window.open('./MusicController?target=Lyrics&amp;id=39', '_blank', 'width=600,height=760')">Open in new window</a>
                                        </div>
                                    </li>
                                </div>

                                

                                <div class="toggle">
                                    <li>
                                        <div class="track-details">
                                            <a class="track" href="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" title="One Direction" data-lightbox="lightboxOne Direction">
                                                <img class="track-cover" title="One Direction" alt="Track Cover" src="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg">
                                                <span>&nbsp;&nbsp;</span>
                                            </a>

                                            <span class="track"> 
                                             <!--     <img class="track-cover" src="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg">-->
                                                <span style="display: block; margin-left: 40px;">Little Things</span>
                                                <span class="track-artists" style="margin-left: 40px;">Jazz</span>
                                            </span>


                                            <div class="track-buttons">
                                                <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/29/40/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(40);" data-shop_target="_self">
                                                    <i class="icon icon-plus">
                                                        <span style="display: none;">
                                                            <span class="track-title">Little Things</span>
                                                            <span class="track-artists">One Direction</span>
                                                        </span>
                                                    </i>
                                                </a>

                                                <span id="btnPlay40">
                                                    <a class="track sp-play-track" style="margin-left: 6px;" href="http://sounds.sg.storage.googleapis.com/music/25/29/40/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(40);" data-shop_target="__self">
                                                        <i class="icon icon-play2">
                                                            <span style="display: none;">
                                                                <span class="track-title">Little Things</span>
                                                                <span class="track-artists">One Direction</span>
                                                            </span>
                                                        </i>
                                                    </a>
                                                </span>

                                                <i style="cursor: pointer;" class="icon icon-menu2 toggle-title">
                                                    <span style="display: none;"></span>
                                                </i>
                                                
                                                <a style="cursor: pointer;" onclick="addTrackToCart(40)">
                                                    
                                                        <i class="icon icon-cart"></i>
                                                    </a>

                                                    <span style="margin-left: 6px;">
                                                        0.90
                                                    </span>
                                            </div>
                                        </div>

                                        <div class="toggle-content">
                                             
                                            <br><br><a style="cursor: pointer" onclick="window.open('./MusicController?target=Lyrics&amp;id=40', '_blank', 'width=600,height=760')">Open in new window</a>
                                        </div>
                                    </li>
                                </div>

                                

                                <div class="toggle">
                                    <li>
                                        <div class="track-details">
                                            <a class="track" href="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" title="One Direction" data-lightbox="lightboxOne Direction">
                                                <img class="track-cover" title="One Direction" alt="Track Cover" src="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg">
                                                <span>&nbsp;&nbsp;</span>
                                            </a>

                                            <span class="track"> 
                                             <!--     <img class="track-cover" src="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg">-->
                                                <span style="display: block; margin-left: 40px;">C'mon, C'mon</span>
                                                <span class="track-artists" style="margin-left: 40px;">Jazz</span>
                                            </span>


                                            <div class="track-buttons">
                                                <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/29/41/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(41);" data-shop_target="_self">
                                                    <i class="icon icon-plus">
                                                        <span style="display: none;">
                                                            <span class="track-title">C'mon, C'mon</span>
                                                            <span class="track-artists">One Direction</span>
                                                        </span>
                                                    </i>
                                                </a>

                                                <span id="btnPlay41">
                                                    <a class="track sp-play-track" style="margin-left: 6px;" href="http://sounds.sg.storage.googleapis.com/music/25/29/41/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(41);" data-shop_target="__self">
                                                        <i class="icon icon-play2">
                                                            <span style="display: none;">
                                                                <span class="track-title">C'mon, C'mon</span>
                                                                <span class="track-artists">One Direction</span>
                                                            </span>
                                                        </i>
                                                    </a>
                                                </span>

                                                <i style="cursor: pointer;" class="icon icon-menu2 toggle-title">
                                                    <span style="display: none;"></span>
                                                </i>
                                                
                                                <a style="cursor: pointer;" onclick="addTrackToCart(41)">
                                                    
                                                        <i class="icon icon-cart"></i>
                                                    </a>

                                                    <span style="margin-left: 6px;">
                                                        1.50
                                                    </span>
                                            </div>
                                        </div>

                                        <div class="toggle-content">
                                             
                                            <br><br><a style="cursor: pointer" onclick="window.open('./MusicController?target=Lyrics&amp;id=41', '_blank', 'width=600,height=760')">Open in new window</a>
                                        </div>
                                    </li>
                                </div>

                                

                                <div class="toggle">
                                    <li>
                                        <div class="track-details">
                                            <a class="track" href="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" title="One Direction" data-lightbox="lightboxOne Direction">
                                                <img class="track-cover" title="One Direction" alt="Track Cover" src="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg">
                                                <span>&nbsp;&nbsp;</span>
                                            </a>

                                            <span class="track"> 
                                             <!--     <img class="track-cover" src="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg">-->
                                                <span style="display: block; margin-left: 40px;">Last First Kiss</span>
                                                <span class="track-artists" style="margin-left: 40px;">Jazz</span>
                                            </span>


                                            <div class="track-buttons">
                                                <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/29/42/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(42);" data-shop_target="_self">
                                                    <i class="icon icon-plus">
                                                        <span style="display: none;">
                                                            <span class="track-title">Last First Kiss</span>
                                                            <span class="track-artists">One Direction</span>
                                                        </span>
                                                    </i>
                                                </a>

                                                <span id="btnPlay42">
                                                    <a class="track sp-play-track" style="margin-left: 6px;" href="http://sounds.sg.storage.googleapis.com/music/25/29/42/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/29/albumart/What Makes You Beautiful.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(42);" data-shop_target="__self">
                                                        <i class="icon icon-play2">
                                                            <span style="display: none;">
                                                                <span class="track-title">Last First Kiss</span>
                                                                <span class="track-artists">One Direction</span>
                                                            </span>
                                                        </i>
                                                    </a>
                                                </span>

                                                <i style="cursor: pointer;" class="icon icon-menu2 toggle-title">
                                                    <span style="display: none;"></span>
                                                </i>
                                                
                                                <a style="cursor: pointer;" onclick="addTrackToCart(42)">
                                                    
                                                        <i class="icon icon-cart"></i>
                                                    </a>

                                                    <span style="margin-left: 6px;">
                                                        0.90
                                                    </span>
                                            </div>
                                        </div>

                                        <div class="toggle-content">
                                             
                                            <br><br><a style="cursor: pointer" onclick="window.open('./MusicController?target=Lyrics&amp;id=42', '_blank', 'width=600,height=760')">Open in new window</a>
                                        </div>
                                    </li>
                                </div>

                                            
                            </ul>

                            <!-- end of looping-->
                            <br><br>
                            
                            <h2 id="album_30">Up All Night <div style="font-weight: 100; display: inline;">(2011)</div></h2> 
                            <p>
                                Up All Night is the debut studio album by English-Irish boy band One Direction, released by Syco Records in November 2011 in Ireland and the United Kingdom, followed by a worldwide release during 2012. After finishing third in the seventh series of British reality singing contest The X Factor in December 2010, One Direction began recording the album in Sweden, UK and the United States, working with a variety of writers and boom!
                            </p>
                            <p>
                                Price: 
                                5.00
                            </p>
                            
                            <a class="btn sp-add-list invert" onclick="addAlbumToCart(30)">Add Album to Cart</a>
                            
                            <ul id="release-list" class="tracklist">
                                

                                <div class="toggle">
                                    <li>
                                        <div class="track-details">
                                            <a class="track" href="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" title="One Direction" data-lightbox="lightboxOne Direction">
                                                <img class="track-cover" title="One Direction" alt="Track Cover" src="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg">
                                                <span>&nbsp;&nbsp;</span>
                                            </a>

                                            <span class="track"> 
                                             <!--     <img class="track-cover" src="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg">-->
                                                <span style="display: block; margin-left: 40px;">What Makes You Beautiful</span>
                                                <span class="track-artists" style="margin-left: 40px;">Jazz</span>
                                            </span>


                                            <div class="track-buttons">
                                                <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/30/33/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(33);" data-shop_target="_self">
                                                    <i class="icon icon-plus">
                                                        <span style="display: none;">
                                                            <span class="track-title">What Makes You Beautiful</span>
                                                            <span class="track-artists">One Direction</span>
                                                        </span>
                                                    </i>
                                                </a>

                                                <span id="btnPlay33">
                                                    <a class="track sp-play-track" style="margin-left: 6px;" href="http://sounds.sg.storage.googleapis.com/music/25/30/33/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(33);" data-shop_target="__self">
                                                        <i class="icon icon-play2">
                                                            <span style="display: none;">
                                                                <span class="track-title">What Makes You Beautiful</span>
                                                                <span class="track-artists">One Direction</span>
                                                            </span>
                                                        </i>
                                                    </a>
                                                </span>

                                                <i style="cursor: pointer;" class="icon icon-menu2 toggle-title">
                                                    <span style="display: none;"></span>
                                                </i>
                                                
                                                <a style="cursor: pointer;" onclick="addTrackToCart(33)">
                                                    
                                                        <i class="icon icon-cart"></i>
                                                    </a>

                                                    <span style="margin-left: 6px;">
                                                        0.90
                                                    </span>
                                            </div>
                                        </div>

                                        <div class="toggle-content">
                                             
                                            <br><br><a style="cursor: pointer" onclick="window.open('./MusicController?target=Lyrics&amp;id=33', '_blank', 'width=600,height=760')">Open in new window</a>
                                        </div>
                                    </li>
                                </div>

                                

                                <div class="toggle">
                                    <li>
                                        <div class="track-details">
                                            <a class="track" href="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" title="One Direction" data-lightbox="lightboxOne Direction">
                                                <img class="track-cover" title="One Direction" alt="Track Cover" src="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg">
                                                <span>&nbsp;&nbsp;</span>
                                            </a>

                                            <span class="track"> 
                                             <!--     <img class="track-cover" src="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg">-->
                                                <span style="display: block; margin-left: 40px;">Gotta Be You</span>
                                                <span class="track-artists" style="margin-left: 40px;">Jazz</span>
                                            </span>


                                            <div class="track-buttons">
                                                <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/30/34/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(34);" data-shop_target="_self">
                                                    <i class="icon icon-plus">
                                                        <span style="display: none;">
                                                            <span class="track-title">Gotta Be You</span>
                                                            <span class="track-artists">One Direction</span>
                                                        </span>
                                                    </i>
                                                </a>

                                                <span id="btnPlay34">
                                                    <a class="track sp-play-track" style="margin-left: 6px;" href="http://sounds.sg.storage.googleapis.com/music/25/30/34/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(34);" data-shop_target="__self">
                                                        <i class="icon icon-play2">
                                                            <span style="display: none;">
                                                                <span class="track-title">Gotta Be You</span>
                                                                <span class="track-artists">One Direction</span>
                                                            </span>
                                                        </i>
                                                    </a>
                                                </span>

                                                <i style="cursor: pointer;" class="icon icon-menu2 toggle-title">
                                                    <span style="display: none;"></span>
                                                </i>
                                                
                                                <a style="cursor: pointer;" onclick="addTrackToCart(34)">
                                                    
                                                        <i class="icon icon-cart"></i>
                                                    </a>

                                                    <span style="margin-left: 6px;">
                                                        0.90
                                                    </span>
                                            </div>
                                        </div>

                                        <div class="toggle-content">
                                             
                                            <br><br><a style="cursor: pointer" onclick="window.open('./MusicController?target=Lyrics&amp;id=34', '_blank', 'width=600,height=760')">Open in new window</a>
                                        </div>
                                    </li>
                                </div>

                                

                                <div class="toggle">
                                    <li>
                                        <div class="track-details">
                                            <a class="track" href="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" title="One Direction" data-lightbox="lightboxOne Direction">
                                                <img class="track-cover" title="One Direction" alt="Track Cover" src="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg">
                                                <span>&nbsp;&nbsp;</span>
                                            </a>

                                            <span class="track"> 
                                             <!--     <img class="track-cover" src="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg">-->
                                                <span style="display: block; margin-left: 40px;">One Thing</span>
                                                <span class="track-artists" style="margin-left: 40px;">Jazz</span>
                                            </span>


                                            <div class="track-buttons">
                                                <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/30/35/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(35);" data-shop_target="_self">
                                                    <i class="icon icon-plus">
                                                        <span style="display: none;">
                                                            <span class="track-title">One Thing</span>
                                                            <span class="track-artists">One Direction</span>
                                                        </span>
                                                    </i>
                                                </a>

                                                <span id="btnPlay35">
                                                    <a class="track sp-play-track" style="margin-left: 6px;" href="http://sounds.sg.storage.googleapis.com/music/25/30/35/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(35);" data-shop_target="__self">
                                                        <i class="icon icon-play2">
                                                            <span style="display: none;">
                                                                <span class="track-title">One Thing</span>
                                                                <span class="track-artists">One Direction</span>
                                                            </span>
                                                        </i>
                                                    </a>
                                                </span>

                                                <i style="cursor: pointer;" class="icon icon-menu2 toggle-title">
                                                    <span style="display: none;"></span>
                                                </i>
                                                
                                                <a style="cursor: pointer;" onclick="addTrackToCart(35)">
                                                    
                                                        <i class="icon icon-cart"></i>
                                                    </a>

                                                    <span style="margin-left: 6px;">
                                                        0.90
                                                    </span>
                                            </div>
                                        </div>

                                        <div class="toggle-content">
                                             
                                            <br><br><a style="cursor: pointer" onclick="window.open('./MusicController?target=Lyrics&amp;id=35', '_blank', 'width=600,height=760')">Open in new window</a>
                                        </div>
                                    </li>
                                </div>

                                

                                <div class="toggle">
                                    <li>
                                        <div class="track-details">
                                            <a class="track" href="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" title="One Direction" data-lightbox="lightboxOne Direction">
                                                <img class="track-cover" title="One Direction" alt="Track Cover" src="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg">
                                                <span>&nbsp;&nbsp;</span>
                                            </a>

                                            <span class="track"> 
                                             <!--     <img class="track-cover" src="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg">-->
                                                <span style="display: block; margin-left: 40px;">More Than This</span>
                                                <span class="track-artists" style="margin-left: 40px;">Jazz</span>
                                            </span>


                                            <div class="track-buttons">
                                                <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/30/43/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(43);" data-shop_target="_self">
                                                    <i class="icon icon-plus">
                                                        <span style="display: none;">
                                                            <span class="track-title">More Than This</span>
                                                            <span class="track-artists">One Direction</span>
                                                        </span>
                                                    </i>
                                                </a>

                                                <span id="btnPlay43">
                                                    <a class="track sp-play-track" style="margin-left: 6px;" href="http://sounds.sg.storage.googleapis.com/music/25/30/43/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/image/album/30/albumart/Up All Night.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(43);" data-shop_target="__self">
                                                        <i class="icon icon-play2">
                                                            <span style="display: none;">
                                                                <span class="track-title">More Than This</span>
                                                                <span class="track-artists">One Direction</span>
                                                            </span>
                                                        </i>
                                                    </a>
                                                </span>

                                                <i style="cursor: pointer;" class="icon icon-menu2 toggle-title">
                                                    <span style="display: none;"></span>
                                                </i>
                                                
                                                <a style="cursor: pointer;" onclick="addTrackToCart(43)">
                                                    
                                                        <i class="icon icon-cart"></i>
                                                    </a>

                                                    <span style="margin-left: 6px;">
                                                        0.90
                                                    </span>
                                            </div>
                                        </div>

                                        <div class="toggle-content">
                                            I'm broken, do you hear me?<br>
I'm blinded, 'cause you are everything I see,<br>
I'm dancin' alone, I'm praying,<br>
That your heart will just turn around,<br>
<br>
And as I walk up to your door,<br>
My head turns to face the floor,<br>
'Cause I can't look you in the eyes and say,<br>
<br>
[Chorus]<br>
When he opens his arms and holds you close tonight,<br>
It just won't feel right,<br>
'Cause I can't love you more than this, yeah,<br>
When he lays you down,<br>
I might just die inside,<br>
It just don't feel right,<br>
'Cause I can't love you more than this,<br>
Can love you more than, this<br>
<br>
If I'm louder, would you see me?<br>
Would you lay down<br>
In my arms and rescue me?<br>
'Cause we are the same<br>
You saved me,<br>
When you leave it's gone again,<br>
<br>
And when I see you on the street,<br>
In his arms, I get weak,<br>
My body fails, I'm on my knees,<br>
Prayin',<br>
<br>
[Chorus]<br>
<br>
I've never had the words to say,<br>
But now I'm askin' you to stay<br>
For a little while inside my arms,<br>
And as you close your eyes tonight,<br>
I pray that you will see the light,<br>
That's shining from the stars above,<br>
<br>
(And I say)<br>
When he opens his arms and holds you close tonight,<br>
It just won't feel right,<br>
'Cause I can't love you more than this,<br>
'Cause I can't love you more than this<br>
<br>
When he lays you down,<br>
I might just die inside,<br>
It just don't feel right,<br>
'Cause I can love you more than this, yeah,<br>
<br>
When he opens his arms and hold you close tonight,<br>
It just won't feel right,<br>
'Cause I can love you more than this,<br>
<br>
When he lays you down,<br>
I might just die inside,<br>
It just don't feel right,<br>
'Cause I can't love you more than this,<br>
Can't love you more than this<br>
<br>
<br>
<br>
Read more: One Direction - More Than This Lyrics | MetroLyrics  
                                            <br><br><a style="cursor: pointer" onclick="window.open('./MusicController?target=Lyrics&amp;id=43', '_blank', 'width=600,height=760')">Open in new window</a>
                                        </div>
                                    </li>
                                </div>

                                            
                            </ul>

                            <!-- end of looping-->
                            <br><br>
                            
                            <h2 id="album_31">Story of My Life <div style="font-weight: 100; display: inline;">(2013)</div></h2> 
                            <p>
                                Story of My Life" (commonly abbreviated as SOML) is a song recorded by British-Irish boy band One Direction. It was released on 25 October 2013 by Syco Music and Columbia Records as the second single from the group's third studio album, Midnight Memories (2013)
                            </p>
                            <p>
                                Price: 
                                3.00
                            </p>
                            
                            <a class="btn sp-add-list invert" onclick="addAlbumToCart(31)">Add Single to Cart</a>
                            
                            <ul id="release-list" class="tracklist">
                                

                                <div class="toggle">
                                    <li>
                                        <div class="track-details">
                                            <a class="track" href="http://sounds.sg.storage.googleapis.com/images/album/31/albumart/Story of My Life.jpg" title="One Direction" data-lightbox="lightboxOne Direction">
                                                <img class="track-cover" title="One Direction" alt="Track Cover" src="http://sounds.sg.storage.googleapis.com/images/album/31/albumart/Story of My Life.jpg">
                                                <span>&nbsp;&nbsp;</span>
                                            </a>

                                            <span class="track"> 
                                             <!--     <img class="track-cover" src="http://sounds.sg.storage.googleapis.com/images/album/31/albumart/Story of My Life.jpg">-->
                                                <span style="display: block; margin-left: 40px;">Story of My Life</span>
                                                <span class="track-artists" style="margin-left: 40px;">Jazz</span>
                                            </span>


                                            <div class="track-buttons">
                                                <a class="track sp-add-track" href="http://sounds.sg.storage.googleapis.com/music/25/31/32/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/images/album/31/albumart/Story of My Life.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(32);" data-shop_target="_self">
                                                    <i class="icon icon-plus">
                                                        <span style="display: none;">
                                                            <span class="track-title">Story of My Life</span>
                                                            <span class="track-artists">One Direction</span>
                                                        </span>
                                                    </i>
                                                </a>

                                                <span id="btnPlay32">
                                                    <a class="track sp-play-track" style="margin-left: 6px;" href="http://sounds.sg.storage.googleapis.com/music/25/31/32/128/01_part1_get_portal_gun-1.mp3" data-cover="http://sounds.sg.storage.googleapis.com/images/album/31/albumart/Story of My Life.jpg" data-artist="One Direction" data-artist_url="javascript:loadArtist2(25);" data-artist_target="_self" data-shop_url="javascript:addTrackToCart(32);" data-shop_target="__self">
                                                        <i class="icon icon-play2">
                                                            <span style="display: none;">
                                                                <span class="track-title">Story of My Life</span>
                                                                <span class="track-artists">One Direction</span>
                                                            </span>
                                                        </i>
                                                    </a>
                                                </span>

                                                <i style="cursor: pointer;" class="icon icon-menu2 toggle-title">
                                                    <span style="display: none;"></span>
                                                </i>
                                                
                                                    <a style="cursor: pointer;" onclick="addAlbumToCart(31)">
                                                        
                                                        <i class="icon icon-cart"></i>
                                                    </a>

                                                    <span style="margin-left: 6px;">
                                                        3.00
                                                    </span>
                                            </div>
                                        </div>

                                        <div class="toggle-content">
                                             
                                            <br><br><a style="cursor: pointer" onclick="window.open('./MusicController?target=Lyrics&amp;id=32', '_blank', 'width=600,height=760')">Open in new window</a>
                                        </div>
                                    </li>
                                </div>

                                            
                            </ul>

                            <!-- end of looping-->
                            <br><br>
                            
                        </div>
                    </div>
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
            pager: true,
            controls:true
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