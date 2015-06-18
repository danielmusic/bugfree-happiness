<%@page import="EntityManager.Genre"%>
<%@page import="EntityManager.Admin"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Admin admin = (Admin) (session.getAttribute("admin"));
    if (session.isNew() || admin == null) {
        response.sendRedirect("../login.jsp?errMsg=Session Expired.");
    } else {
        List<Genre> genres = (List<Genre>) (session.getAttribute("genre"));
%>
<!doctype html>
<html class="fixed">
    <head>
        <jsp:include page="../head.html" />
    </head>
    <body onload="alertFunc()">
        <jsp:include page="../displayNotification.jsp" />
        <script>
            function addGenre() {
                window.location.href = "addGenre.jsp";
            }
        </script>

        <section class="body">
            <jsp:include page="../header.jsp" />

            <div class="inner-wrapper">
                <jsp:include page="../sidebar.jsp" />
                <section role="main" class="content-body">
                    <header class="page-header">
                        <h2>Genre Management</h2>
                        <div class="right-wrapper pull-right">
                            <ol class="breadcrumbs">
                                <li>
                                    <a href="admin/workspace.jsp">
                                        <i class="fa fa-home"></i>
                                    </a>
                                </li>
                                <li><span>Genre Management &nbsp;&nbsp</span></li>
                            </ol>
                        </div>
                    </header>

                    <!-- start: page -->

                    <section class="panel">
                        <header class="panel-heading">
                            <h2 class="panel-title">Genre Management</h2>
                        </header>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-md-12"> 
                                    <button class="btn btn-primary" onclick="addGenre()">Add Genre</button>
                                </div>
                            </div>
                            <br/>


                            <form name="GenreManagement">
                                <table class="table table-bordered table-striped mb-none" id="datatable-default">
                                    <thead>
                                        <tr>
                                            <th>Name</th>
                                            <th>Total Records</th>
                                            <th style="width: 300px; text-align: center;">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            if (genres != null && genres.size() > 0) {
                                                for (int i = 0; i < genres.size(); i++) {
                                        %>
                                        <tr>        
                                            <td><%=genres.get(i).getGenre()%></td>
                                            <td><%=genres.get(i).getListOfMusics().size()%></td>
                                            <td>
                                                <button type="button" class="modal-with-move-anim btn btn-default btn-block"  href="#modalRemove">Disable</button>
                                            </td>
                                        </tr>
                                        <%
                                                }
                                            }
                                        %>

                                    </tbody>
                                </table>
                                <input type="hidden" name="id" value="">
                                <input type="hidden" name="target" value="">    
                            </form>
                        </div>

                    </section>
                    <!-- end: page -->
                </section>
            </div>
        </section>

        <jsp:include page="../foot.html" />
    </body>
</html>
<%    }
%>