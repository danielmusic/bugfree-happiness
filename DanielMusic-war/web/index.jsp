<!doctype html>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!-- Consider adding a manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!--> 
<html class="no-js" lang="en-US"><!--<![endif]-->
    <head>
        <jsp:include page="jspIncludePages/head.html" />
    </head>
    <body>

        <!-- ############################# Update Message for older browsers ############################# -->
        <!--[if lte IE 7]>
           <div id="ie-message"><p>You are using Internet Explorer 7.0 or older to view this site. Your browser is an eight year old browser which does not display modern web sites properly. Please upgrade to a newer browser to fully enjoy the web. <a href="http://www.microsoft.com/windows/internet-explorer/default.aspx">Upgrade your browser</a></p></div>
        <![endif]-->

        <!-- ############################# navigation section ############################# -->

        <jsp:include page="jspIncludePages/menu.jsp" />

        <!-- ############################# Ajax Page Container ############################# -->
        <section id="ajax-container" class="index-page">

            <!-- ################################################################################### -->
            <!-- ############################# CONTENT LOADED VIA AJAX ############################# -->
            <!-- ################################################################################### -->
        </section>
        <!-- /ajax container -->

        <jsp:include page="jspIncludePages/footer.jsp" />

        <jsp:include page="jspIncludePages/foot.html" />
        <script src="js/lightbox2-master/js/lightbox-plus-jquery.min.js"></script>
    </body>
</html>