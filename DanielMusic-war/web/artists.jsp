<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Sound.sg">
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/artist-single-bg.jpg)">
        <h2 class="heading-l">Artist <span class="color">ZERO</span></h2>
        <br>
        <span class="overlay grids"></span>
    </section>
    <section class="content section">
        <div class="container">

            <%@page import="EntityManager.Account"%>
            <%
                Account account = (Account) session.getAttribute("account");
                if (account != null) {
                    out.write(">>>>>>>>>>>>>>>>>" + account.getName());
                }
            %>

            <div class="sidebar main-left main-medium">
                <div class="widget details-widget">
                    <a style="cursor: default;" class="thumb-glitch">
                        <span class="img">
                            <img src="placeholders/M5.jpg" />
                        </span>
                    </a>
                    <div class="details-meta">
                        <ul class="details-list">
                            <li>
                                <span class="label">Name</span>
                                <div class="data"><b>Obiekt ZERO</b></div>
                            </li>
                            <li>
                                <span class="label">Genres</span>
                                <div class="data">Breakbeat</div>
                            </li>
                        </ul>
                    </div>
                    <!-- Details Share -->
                    <div class="details-social-box">
                        <a href="javascript:;" class="facebook-share"><i class="icon icon-facebook"></i></a>
                        <a href="javascript:;" class="twitter-share"><i class="icon icon-twitter"></i></a>
                        <a href="javascript:;" class="googleplus-share"><i class="icon icon-googleplus"></i></a>
                        <a href="javascript:;" class="googleplus-share"><i class="icon icon-soundcloud"></i></a>
                    </div>
                </div>
            </div>

            <div id="main" class="release main-left main-medium">
                <article>

                    <!-- tabs -->
                    <div class="tabs-wrap">
                        <!-- tabs navigation -->
                        <ul class="tabs">
                            <li><a href="#tab-bio" class="active-tab">Biography</a></li>
                            <li><a href="#tab-releases">Albums</a></li>
                        </ul>
                        <!-- /tabs navigation -->
                        <!-- tab content -->
                        <div id="tab-bio" class="tab-content">
                            <h2>Biography</h2>
                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi eget tellus vitae lacus vestibulum sagittis. Nullam sed risus blandit, pretium magna id, varius lectus. Praesent a condimentum est. Pellentesque rutrum consectetur metus. Curabitur scelerisque, tortor quis ullamcorper semper, lacus metus placerat tellus, et aliquam libero tortor et lectus. Maecenas rhoncus, sem a pellentesque convallis, dolor nulla semper dolor, vestibulum luctus sapien lectus in quam. Nunc accumsan consequat est a porttitor. Proin vitae dolor mauris. Aliquam erat volutpat. Quisque quis tincidunt mi.
                            </p>
                            <p>Duis dolor tellus, faucibus non ligula ac, fringilla porttitor eros. Cras sagittis eleifend erat ac fringilla. Proin ac odio et neque vulputate tempus at vel justo. Maecenas semper imperdiet euismod. Donec tempor erat vel scelerisque tincidunt. Sed sagittis purus orci, eu auctor lectus placerat vel. Nunc imperdiet tincidunt volutpat. Duis ac semper purus. Nunc mauris magna, ornare at lorem et, sollicitudin dapibus tortor.</p>
                            <p>
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi eget tellus vitae lacus vestibulum sagittis. Nullam sed risus blandit, pretium magna id, varius lectus. Praesent a condimentum est. Pellentesque rutrum consectetur metus. Curabitur scelerisque, tortor quis ullamcorper semper, lacus metus placerat tellus, et aliquam libero tortor et lectus. Maecenas rhoncus, sem a pellentesque convallis, dolor nulla semper dolor, vestibulum luctus sapien lectus in quam. Nunc accumsan consequat est a porttitor. Proin vitae dolor mauris. Aliquam erat volutpat. Quisque quis tincidunt mi.
                            </p>
                        </div>
                        <!-- /tab content -->
                        <!-- tab content -->
                        <div id="tab-releases" class="tab-content">
                            <h2>Loop Albums</h2>

                            <ol id="release-list" class="tracklist">
                                <li>
                                    <div class="track-details">
                                        <a class="track sp-play-track" href="placeholders/mp3/adg3com_chuckedknuckles.mp3" data-cover="placeholders/M5.jpg"
                                           data-artist="Madoff"
                                           data-artist_url="http://artist.com/madoff-freak" 
                                           data-artist_target="_self"
                                           data-shop_url="shop_url" 
                                           data-shop_target="_blank"
                                           >
                                            <!-- cover -->
                                            <img class="track-cover" src="placeholders/M5.jpg">
                                            <!-- Title -->
                                            <span class="track-title" data-artist_url="artist_url">One Last Time</span>
                                            <!-- Artists -->
                                            <span class="track-artists">Ariana Grande </span>
                                        </a>

                                        <div class="track-buttons">
                                            <a class="track sp-play-track" href="placeholders/mp3/adg3com_chuckedknuckles.mp3"><i class="icon icon-play2"></i></a>
                                            <a href="javascript:;"><i class="icon icon-cart"></i></a>
                                        </div>
                                    </div>
                                </li>
                            </ol>
                            <p>
                                <a href="javascript:;" class="btn invert sp-play-list" data-id="release-list">Play All Tracks</a>
                                <a href="javascript:;" class="btn sp-add-list" data-id="release-list">Add All Tracks</a>
                            </p>

                        </div>
                        <!-- /tab content -->
                    </div>
                    <!-- /tabs -->


                </article>
                <!-- /article -->

            </div>
            <!-- /main -->
        </div>
        <!-- /container -->
    </section>
    <!-- /Content -->

</section>
<!-- /page -->