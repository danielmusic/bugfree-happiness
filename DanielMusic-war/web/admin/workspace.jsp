<%@page import="EntityManager.Admin"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Admin admin = (Admin) (session.getAttribute("admin"));
    if (session.isNew()) {
        response.sendRedirect("login.jsp?errMsg=Invalid Request. Please login.");
    } else if (admin == null) {
        response.sendRedirect("login.jsp?errMsg=Session Expired.");
    } else {
%>
<!doctype html>
<html class="fixed">
    <head>
        <title>Daniel Music System</title>

        <!-- Web Fonts  -->
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700,800|Shadows+Into+Light" rel="stylesheet" type="text/css">

        <!-- Vendor CSS -->
        <link rel="stylesheet" href="assets/vendor/bootstrap/css/bootstrap.css" />

        <link rel="stylesheet" href="assets/vendor/font-awesome/css/font-awesome.css" />
        <link rel="stylesheet" href="assets/vendor/magnific-popup/magnific-popup.css" />
        <link rel="stylesheet" href="assets/vendor/bootstrap-datepicker/css/datepicker3.css" />

        <!-- Specific Page Vendor CSS -->
        <link rel="stylesheet" href="assets/vendor/select2/select2.css" />
        <link rel="stylesheet" href="assets/vendor/jquery-datatables-bs3/assets/css/datatables.css" />
        <link rel="stylesheet" href="assets/vendor/pnotify/pnotify.custom.css" />

        <!-- Theme CSS -->
        <link rel="stylesheet" href="assets/stylesheets/theme.css" />

        <!-- Skin CSS -->
        <link rel="stylesheet" href="assets/stylesheets/skins/default.css" />

        <!-- Theme Custom CSS -->
        <link rel="stylesheet" href="assets/stylesheets/theme-custom.css">

        <!-- Head Libs -->
        <script src="assets/vendor/modernizr/modernizr.js"></script>
    </head>
    <body onload="alertFunc()">
        <jsp:include page="displayNotification.jsp" />
        <jsp:include page="header.jsp" />
        <div class="inner-wrapper">
            <jsp:include page="sidebar.jsp" />
            <section role="main" class="content-body">
                <header class="page-header">
                    <h2>Workspace</h2>
                    <div class="right-wrapper pull-right">
                        <ol class="breadcrumbs">
                            <li><i class="fa fa-home"></i></li>
                            <li><span>Workspace &nbsp;&nbsp</span></li>
                        </ol>
                    </div>
                </header>

                <!-- start: page -->

                <!-- end: page -->
            </section>
        </div>

        <!-- Vendor -->
        <script src="assets/vendor/jquery/jquery.js"></script>
        <script src="assets/vendor/jquery-browser-mobile/jquery.browser.mobile.js"></script>
        <script src="assets/vendor/bootstrap/js/bootstrap.js"></script>
        <script src="assets/vendor/nanoscroller/nanoscroller.js"></script>
        <script src="assets/vendor/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
        <script src="assets/vendor/magnific-popup/magnific-popup.js"></script>
        <script src="assets/vendor/jquery-placeholder/jquery.placeholder.js"></script>

        <!-- Specific Page Vendor -->
        <script src="assets/vendor/select2/select2.js"></script>
        <script src="assets/vendor/jquery-datatables/media/js/jquery.dataTables.js"></script>
        <script src="assets/vendor/jquery-datatables/extras/TableTools/js/dataTables.tableTools.min.js"></script>
        <script src="assets/vendor/jquery-datatables-bs3/assets/js/datatables.js"></script>
        <script src="assets/vendor/pnotify/pnotify.custom.js"></script>
        <script src="assets/javascripts/ui-elements/examples.modals.js"></script>
        <!-- Theme Base, Components and Settings -->
        <script src="assets/javascripts/theme.js"></script>

        <!-- Theme Custom -->
        <script src="assets/javascripts/theme.custom.js"></script>

        <!-- Theme Initialization Files -->
        <script src="assets/javascripts/theme.init.js"></script>
    </section>

</body>
</html>
<%
    }
%>