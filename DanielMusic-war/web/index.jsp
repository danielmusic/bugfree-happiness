<!doctype html>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!-- Consider adding a manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!--> 
<html class="no-js" lang="en-US"><!--<![endif]-->
    <head>
        <title>Noisa - Ultimate Music Theme</title>
        <meta charset="utf-8">
        <!--[if IE]>
                <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" >
        <![endif]-->
        <meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="description" content="Premium theme.">
        <meta name="keywords" content=""/>
        <meta name="author" content="Site Author">
        <!-- Fav icon -->
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon" />
        <!-- <meta name="robots" content="index,follow"> -->

        <!-- ############################# Stylesheets ############################# -->
        <!-- OWL Carousel -->
        <link rel="stylesheet" href="css/owl.carousel.css" media="screen" />
        <link rel="stylesheet" href="css/owl.transitions.css" media="screen" />
        <!-- /OWL Carousel -->
        <!-- Scamp Player stylesheets -->
        <link rel="stylesheet" href="js/scamp_player/css/scamp.player.css" media="screen" />
        <link rel="stylesheet" href="js/scamp_player/css/scamp.player.light.css" media="screen" />
        <!-- /Scamp Player stylesheets -->
        <link rel="stylesheet" href="css/menu.css" media="screen" />
        <link rel="stylesheet" href="css/style.css" media="screen" />
        <link rel="stylesheet" href="css/media-queries.css" media="screen" />
        <!-- Fancybox styles -->
        <link rel="stylesheet" href="css/fancybox.custom.css" media="screen" />
        <!-- REVOLUTION BANNER CSS SETTINGS -->
        <link rel="stylesheet" type="text/css" href="js/rs-plugin/css/settings.css" media="screen" />

        <!-- ############################# Javascripts ############################# -->
        <!-- jQuery -->
        <script src="js/jquery.min.js" type="text/javascript"></script>
        <!-- Add HTML5 support for older IE browsers -->
        <!--[if lt IE 9]> 
                <script src="js/html5.min.js"></script>
                <script src="js/selectivizr-and-extra-selectors.min.js"></script>
        <![endif]-->
        <!-- Modernizr -->
        <script src="js/modernizr.custom.js"></script>
    </head>
    <body>

        <!-- ############################# Update Message for older browsers ############################# -->
        <!--[if lte IE 7]>
           <div id="ie-message"><p>You are using Internet Explorer 7.0 or older to view this site. Your browser is an eight year old browser which does not display modern web sites properly. Please upgrade to a newer browser to fully enjoy the web. <a href="http://www.microsoft.com/windows/internet-explorer/default.aspx">Upgrade your browser</a></p></div>
        <![endif]-->

        <!-- ############################# navigation section ############################# -->
        <jsp:include page="menu.jsp" />
        <!-- /navigation section -->	


        <!-- ############################# Ajax Page Container ############################# -->
        <section id="ajax-container" class="index-page">

            <!-- ################################################################################### -->
            <!-- ############################# CONTENT LOADED VIA AJAX ############################# -->
            <!-- ################################################################################### -->

        </section>
        <!-- /ajax container -->

        <!-- ############################# Footer ############################# -->
        <footer id="footer">
            <!-- Footer Bottom -->
            <div id="footer-bottom" class="container">
                <!-- Footer logo -->
                <div class="footer-logo col-1-2">
                    <img src="placeholders/logo.png" alt="Logo">
                </div>
                <!-- /footer logo -->

                <!-- Footer copyrights -->
                <div class="footer-copyrights col-1-2 last">
                    Copyright &copy; 2006-2014 - Noisa. All rights reserved
                </div>
                <!-- /footer copyrights -->
            </div>
            <!-- /footer bottom -->
        </footer>
        <!-- /footer -->

        <!-- ############################# Scamp Player ############################# -->
        <!-- Special classes: sp-show-player sp-show-list -->
        <div id="scamp_player" class="sp-show-player">
            <a href="placeholders/mp3/1-01 Hands Up.mp3" data-cover="placeholders/release-image01.jpg">LEE YUAN GUANG</a>
        </div>

        <!-- ############################# javascripts ############################# -->
        <script src="js/jquery.easing-1.3.min.js"></script>
        <script src="js/jquery.small.plugins.min.js"></script>
        <script src="js/jquery.dlmenu.js"></script>
        <script src="js/smoothscroll.js"></script>
        <script src="js/jquery.scrollTo.min.js"></script>
        <script src="js/jquery.isotope.min.js"></script>
        <script src="js/nprogress.js"></script>
        <script src="js/jquery.countdown.js"></script>
        <script src="js/nice_select.min.js"></script>
        <script src="js/owl.carousel.min.js"></script>
        <script src="js/jquery.fancybox-1.3.4.pack.js"></script>
        <!-- Google Maps -->
        <script src="https://maps.google.com/maps/api/js?sensor=false"></script>
        <script src="js/jquery.gmap.min.js"></script>
        <!-- /Google Maps -->
        <!-- jQuery REVOLUTION Slider  -->
        <script type="text/javascript" src="js/rs-plugin/js/jquery.themepunch.plugins.min.js"></script>
        <script type="text/javascript" src="js/rs-plugin/js/jquery.themepunch.revolution.min.js"></script>
        <!-- Scamp Player -->
        <script src="https://connect.soundcloud.com/sdk.js"></script>
        <script src="js/scamp_player/js/soundmanager2-nodebug-jsmin.js"></script>
        <script src="js/scamp_player/js/iscroll.js"></script>
        <script src="js/scamp_player/jquery.scamp.player.min.js"></script>
        <!-- /Scamp Player -->
        <!-- custom scripts -->
        <script src="js/custom.js"></script>
    </body>
</html>