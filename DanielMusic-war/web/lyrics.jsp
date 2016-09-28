<!doctype html>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!-- Consider adding a manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!--> 
<%@page import="EntityManager.Music"%>
<html lang="en-US"><!--<![endif]-->
    <head>
        <title>sounds.sg</title>
        <meta charset="utf-8">
        <meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="description" content="Singapore music hub">
        <meta name="keywords" content="music"/>
        <meta name="author" content="Sounds Singapore">
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon" />
        <script src="js/jquery.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="css/style.css" media="screen" />
        <link rel="stylesheet" href="css/media-queries.css" media="screen" />
        <script src="js/modernizr.custom.js"></script>
        <style>
            html,
            body {
                height: 100%;
                margin: 0;
                padding: 0;
                border: 0;
                overflow-y: scroll;
            }
        </style>
    </head>
    <body>
        <%
            Music music = (Music) session.getAttribute("musicLyrics");
            if (music != null) {
        %>
        <div class="container" style="margin-top: 25px; margin-bottom: 50px;">
            <h2 class="heading-l" style="line-height: 1em; margin-bottom: 10px; font-size: 32px;"><%=music.getName()%></h2>
            <%
                if (music.getLyrics() != null) {
                    String lyrics = music.getLyrics().replaceAll("\\r", "<br>");
                    out.print(lyrics);
                }
            %>
        </div>
        <%} else {%>
        <p class='warning'>No music was specified or the lyrics for the specified music is no longer available.</p>
        <%}%>

        <!-- ############################# Footer ############################# -->
        <footer id="footer" style="margin-bottom: 0px;">
            <!-- Footer Bottom -->
            <div id="footer-bottom" class="container">
                <div class="footer-copyrights" style="text-align: center; margin-bottom: 5px;">
                    © 2016 - SOUNDS.SG, ALL RIGHTS RESERVED
                </div>
            </div>
            <!-- /footer bottom -->
        </footer>
        <!-- /footer -->
    </body>
</html>