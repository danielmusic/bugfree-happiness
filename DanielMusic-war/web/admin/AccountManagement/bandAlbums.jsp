<%@page import="EntityManager.Album"%>
<%@page import="EntityManager.Artist"%>
<%@page import="EntityManager.Admin"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Admin admin = (Admin) (session.getAttribute("admin"));
    Artist band = (Artist) (session.getAttribute("band"));
    if (session.isNew() || admin == null) {
        response.sendRedirect("../login.jsp?errMsg=Session Expired.");
    } else if (band == null) {
        response.sendRedirect("../AccountManagement/bandManagement.jsp?errMsg=An error has occured.");
    } else {
%>
<!doctype html>
<html class="fixed">
    <head>
        <jsp:include page="../jspIncludePages/head.html" />
    </head>
    <body onload="alertFunc()">
        <jsp:include page="../jspIncludePages/displayNotification.jsp" />
        <script>
            function viewBandTrack(id) {
                window.location.href = "bandTracks.jsp?id=" + id;
            }

            function back() {
                window.location.href = "band.jsp";
            }
        </script>

        <section class="body">
            <jsp:include page="../jspIncludePages/header.jsp" />

            <div class="inner-wrapper">
                <jsp:include page="../jspIncludePages/sidebar.jsp" />
                <section role="main" class="content-body">
                    <header class="page-header">
                        <h2>Band Albums  - <%=band.getName()%></h2>
                        <div class="right-wrapper pull-right">
                            <ol class="breadcrumbs">
                                <li><a href="admin/workspace.jsp"><i class="fa fa-home"></i></a></li>
                                <li><a href="../../AccountManagementController?target=ListAllBand">Band Management</a></li>
                                <li><a href="band.jsp"><%=band.getName()%></a></li>
                                <li><span>Band Albums &nbsp;&nbsp</span></li>
                            </ol>
                        </div>
                    </header>

                    <!-- start: page -->
                    <section class="panel">
                        <header class="panel-heading">
                            <h2 class="panel-title">Albums - <%=band.getName()%></h2>
                        </header>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-md-12"> 
                                    <button type="button" class="btn btn-default" onclick="javascript:back()"> Back</button>
                                </div>
                            </div>
                            <br/>
                            <form name="bandAlbumManagement">
                                <table class="table table-bordered table-striped mb-none" id="datatable-default">
                                    <thead>
                                        <tr>
                                            <th>Title</th>
                                            <th>Year Released</th>
                                            <th>Price</th>
                                            <th>Album Artwork</th>
                                            <th>Description</th>
                                            <th>Credits</th>

                                            <th style="text-align: center;">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            if (band != null) {
                                                List<Album> albums = band.getListOfAlbums();
                                                for (int i = 0; i < albums.size(); i++) {
                                        %>
                                        <tr>        
                                            <td><%=albums.get(i).getName()%></td>
                                            <td><%=albums.get(i).getYearReleased()%></td>
                                            <td><%=albums.get(i).getPrice()%></td>

                                            <td>
                                                <%if (albums.get(i).getImageLocation() != null && !albums.get(i).getImageLocation().isEmpty()) {%>
                                                <a class="modal-with-move-anim btn btn-default btn-block" href="#modalAlbumArt">View</a>
                                                <div id="modalAlbumArt" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
                                                    <section class="panel">
                                                        <header class="panel-heading">
                                                            <h2 class="panel-title">Album Artwork</h2>
                                                        </header>
                                                        <div class="panel-body">
                                                            <div class="modal-wrapper">
                                                                <div class="modal-text">
                                                                    <img src="http://danielmusictest.storage.googleapis.com/<%=albums.get(i).getImageLocation()%>">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <footer class="panel-footer">
                                                            <div class="row">
                                                                <div class="col-md-12 text-right">
                                                                    <button class="btn btn-default modal-dismiss">Close</button>
                                                                </div>
                                                            </div>
                                                        </footer>
                                                    </section>
                                                </div>
                                                <%
                                                    } else {
                                                        out.print("-");
                                                    }
                                                %>
                                            </td>

                                            <td>
                                                <%if (albums.get(i).getCredits() != null && !albums.get(i).getCredits().isEmpty()) {%>
                                                <a class="modal-with-move-anim btn btn-default btn-block" href="#modalDescription">View</a>
                                                <div id="modalDescription" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
                                                    <section class="panel">
                                                        <header class="panel-heading">
                                                            <h2 class="panel-title">Description</h2>
                                                        </header>
                                                        <div class="panel-body">
                                                            <div class="modal-wrapper">
                                                                <div class="modal-text" style="height: 500px;">
                                                                    <textarea style="height:100%; width: 100%;"><%=albums.get(i).getDescription()%></textarea>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <footer class="panel-footer">
                                                            <div class="row">
                                                                <div class="col-md-12 text-right">
                                                                    <button class="btn btn-default modal-dismiss">Close</button>
                                                                </div>
                                                            </div>
                                                        </footer>
                                                    </section>
                                                </div>
                                                <%
                                                    } else {
                                                        out.print("-");
                                                    }
                                                %>
                                            </td>

                                            <td>
                                                <%if (albums.get(i).getCredits() != null && !albums.get(i).getCredits().isEmpty()) {%>
                                                <a class="modal-with-move-anim btn btn-default btn-block" href="#modalCredits">View</a>
                                                <div id="modalCredits" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
                                                    <section class="panel">
                                                        <header class="panel-heading">
                                                            <h2 class="panel-title">Credits</h2>
                                                        </header>
                                                        <div class="panel-body">
                                                            <div class="modal-wrapper">
                                                                <div class="modal-text" style="height: 350px;">
                                                                    <textarea style="height:100%; width: 100%;"><%=albums.get(i).getCredits()%></textarea>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <footer class="panel-footer">
                                                            <div class="row">
                                                                <div class="col-md-12 text-right">
                                                                    <button class="btn btn-default modal-dismiss">Close</button>
                                                                </div>
                                                            </div>
                                                        </footer>
                                                    </section>
                                                </div>
                                                <%
                                                    } else {
                                                        out.print("-");
                                                    }
                                                %>
                                            </td>

                                            <td><button type="button" class="btn btn-default btn-block" onclick="javascript:viewBandTrack('<%=albums.get(i).getId()%>');">View Tracks</button></td>
                                        </tr>
                                        <%
                                                }
                                            }
                                        %>
                                    </tbody>
                                </table>
                            </form>
                        </div>

                    </section>
                    <!-- end: page -->
                </section>
            </div>
        </section>
        <jsp:include page="../jspIncludePages/foot.html" />

    </body>
</html>
<%}%>
