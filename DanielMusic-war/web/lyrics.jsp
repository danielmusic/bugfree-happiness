<!doctype html>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!-- Consider adding a manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!--> 
<%@page import="java.util.List"%>
<%@page import="EntityManager.Album"%>
<%@page import="EntityManager.Music"%>
<%@page import="EntityManager.Artist"%>
<%@page import="EntityManager.Account"%>
<%@page import="java.text.NumberFormat"%>
<html class="no-js" lang="en-US"><!--<![endif]-->
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
    </head>
    <body>
        <%
            Music music = (Music) session.getAttribute("musicLyrics");
            if (music != null) {
        %>

        <article>
            <div class="container">
                <h2 class="heading-l"><%=music.getName()%>'s</h2>

                <%=music.getAlbum().getArtist().getName()%>

                <%
                    if (music.getLyrics() != null) {
                        String lyrics = music.getLyrics().replaceAll("\\r", "<br>");
                        out.print(lyrics);
                    }
                %>
            </div>
            <br/><br/><br/>
        </article>

        <%} else {%>
        <p class='warning'>No music was specified or the lyrics for the specified music is no longer available.</p>
        <%}%>
        <jsp:include page="jspIncludePages/footer.jsp" />
    </body>
</html>