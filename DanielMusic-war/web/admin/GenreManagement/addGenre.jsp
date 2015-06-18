<%@page import="EntityManager.Genre"%>
<%@page import="EntityManager.Admin"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Admin admin = (Admin) (session.getAttribute("admin"));
    if (session.isNew() || admin == null) {
        response.sendRedirect("../login.jsp?errMsg=Session Expired.");
    } else {
%>
<!doctype html>
<html class="fixed">
    <head>
        <jsp:include page="../head.html" />
    </head>
    <body onload="alertFunc()">
        <script>
            function back() {
                window.location.href = "../../GenreManagementController?target=ListAllGenre";
            }
        </script>
        <jsp:include page="../displayNotification.jsp" />
        <section class="body">
            <jsp:include page="../header.jsp" />

            <div class="inner-wrapper">
                <jsp:include page="../sidebar.jsp" />
                <section role="main" class="content-body">
                    <header class="page-header">
                        <h2>Genre Management - Add Genre</h2>
                        <div class="right-wrapper pull-right">
                            <ol class="breadcrumbs">
                                <li>
                                    <a href="admin/workspace.jsp">
                                        <i class="fa fa-home"></i>
                                    </a>
                                </li>
                                <li><span>Genre Management</span></li>
                                <li><span>Add Genre &nbsp;&nbsp</span></li>
                            </ol>
                        </div>
                    </header>

                    <!-- start: page -->
                    <div class="row">
                        <div class="col-lg-12">
                            <form class="form-horizontal form-bordered" action="../../GenreManagementController">
                                <section class="panel">
                                    <header class="panel-heading">
                                        <h2 class="panel-title">New Genre</h2>
                                    </header>
                                    <div class="panel-body">
                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Name <span class="required">*</span></label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" name="name" required>
                                            </div>
                                        </div>
                                        <input type="hidden" name="target" value="AddGenre">   
                                    </div>

                                    <footer class="panel-footer">
                                        <div class="row">
                                            <div class="col-sm-9 col-sm-offset-3">
                                                <button class="btn btn-primary">Submit</button>
                                                <input type="button"  class="btn btn-default" value="Back" onclick="javascript:back()"/>
                                            </div>
                                        </div>
                                    </footer>
                                </section>
                            </form>
                        </div>
                    </div>
                    <!-- end: page -->

                </section>
            </div>
        </section>

        <jsp:include page="../foot.html" />
    </body>
</html>
<%    }
%>