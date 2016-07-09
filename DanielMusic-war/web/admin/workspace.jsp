<%@page import="EntityManager.Admin"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Admin admin = (Admin) (session.getAttribute("admin"));
    if (session.isNew() || admin == null) {
        response.sendRedirect("login.jsp?errMsg=Session Expired.");
    } else {
%>
<!doctype html>
<html class="fixed">
    <head>
        <jsp:include page="jspIncludePages/head-outsidefolder.html" />
    </head>
    <script>
        function generateEmail() {
            window.location.href = "../AccountManagementController?target=GenerateEmail";
        }
    </script>
    <body onload="alertFunc()">
        <jsp:include page="jspIncludePages/displayNotification.jsp" />
        <jsp:include page="jspIncludePages/header-outsidefolder.jsp" />

        <div class="inner-wrapper">

            <jsp:include page="jspIncludePages/siderbar-outsidefolder.jsp" />

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
                <h1>Welcome back!</h1>

                <p>Select the relevant function to manage from the sidebar</p>

                <button type="button" class="btn btn-default" onclick="generateEmail()">Generate Verified Account Email</button>
                <!-- end: page -->
            </section>
        </div>


        <jsp:include page="jspIncludePages/foot-outsidefolder.html" />

    </body>
</html>
<%
    }
%>