<%@page import="EntityManager.Music"%>
<%@page import="EntityManager.Album"%>
<%@page import="EntityManager.Artist"%>
<%@page import="EntityManager.Admin"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Admin admin = (Admin) (session.getAttribute("admin"));
    Artist band = (Artist) (session.getAttribute("band"));
    String albumID = request.getParameter("id");

    if (session.isNew() || admin == null) {
        response.sendRedirect("../login.jsp?errMsg=Session Expired.");
    } else if (band == null || albumID == null) {
        response.sendRedirect("../AccountManagement/bandManagement.jsp?errMsg=An error has occured.");
    } else {
        List<Album> albums = band.getListOfAlbums();
        List<Music> tracks = null;

        for (int k = 0; k < albums.size(); k++) {
            if (albums.get(k).getId() == Long.parseLong(albumID)) {
                tracks = albums.get(k).getListOfMusics();
            }
        }
%>
<!doctype html>
<html class="fixed">
    <head>
        <jsp:include page="../jspIncludePages/head.html" />
    </head>
    <body onload="alertFunc()">
        <jsp:include page="../jspIncludePages/displayNotification.jsp" />
        <script>
            function back() {
                window.location.href = "bandAlbums.jsp";
            }
            function getDownloadLink(id) {
                window.location.href = "../../AccountManagementController?target=GetDownloadLink&id=" + id;
            }
        </script>

        <section class="body">
            <jsp:include page="../jspIncludePages/header.jsp" />

            <div class="inner-wrapper">
                <jsp:include page="../jspIncludePages/sidebar.jsp" />
                <section role="main" class="content-body">
                    <header class="page-header">
                        <h2>Band Tracks - <%=band.getName()%></h2>
                        <div class="right-wrapper pull-right">
                            <ol class="breadcrumbs">
                                <li><a href="admin/workspace.jsp"><i class="fa fa-home"></i></a></li>
                                <li><a href="../../AccountManagementController?target=ListAllBand">Band Management</a></li>
                                <li><a href="band.jsp"><%=band.getName()%></a></li>
                                <li><a href="bandAlbums.jsp">Albums</a></li>
                                <li><span>Band Tracks &nbsp;&nbsp</span></li>
                            </ol>
                        </div>
                    </header>

                    <!-- start: page -->
                    <section class="panel">
                        <header class="panel-heading">
                            <h2 class="panel-title">Tracks - <%=band.getName()%></h2>
                        </header>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-md-12"> 
                                    <button type="button" class="btn btn-default" onclick="javascript:back()"> Back</button>
                                </div>
                            </div>
                            <br/>
                            <form name="bandTracksManagement">
                                <table class="table table-bordered table-striped mb-none" id="datatable-default">
                                    <thead>
                                        <tr>
                                            <th>Title</th>
                                            <th>Year Released</th>
                                            <th>Track no</th>
                                            <th>Price</th>
                                            <th>Lyrics</th>
                                            <th>Credits</th>
                                            <th>Download Link</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            if (band != null) {
                                                for (int i = 0; i < tracks.size(); i++) {
                                        %>
                                        <tr>        
                                            <td><%=tracks.get(i).getName()%></td>
                                            <td><%=tracks.get(i).getYearReleased()%></td>
                                            <td><%=tracks.get(i).getTrackNumber()%></td>
                                            <td><%=tracks.get(i).getPrice()%></td>

                                            <td>
                                                <%if (tracks.get(i).getLyrics() != null && !tracks.get(i).getLyrics().isEmpty()) {%>
                                                <a class="modal-with-move-anim btn btn-default btn-block" href="#modalLyrics">View</a>
                                                <div id="modalLyrics" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
                                                    <section class="panel">
                                                        <header class="panel-heading">
                                                            <h2 class="panel-title">Lyrics</h2>
                                                        </header>
                                                        <div class="panel-body">
                                                            <div class="modal-wrapper">
                                                                <div class="modal-text" style="height: 500px;">
                                                                    <textarea style="height:100%; width: 100%;"><%if (tracks.get(i).getLyrics() != null) {
                                                                            out.print(tracks.get(i).getLyrics());
                                                                        }%>"></textarea>
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
                                                <%if (tracks.get(i).getCredits() != null && !tracks.get(i).getCredits().isEmpty()) {%>
                                                <a class="modal-with-move-anim btn btn-default btn-block" href="#modalCredits">View</a>
                                                <div id="modalCredits" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
                                                    <section class="panel">
                                                        <header class="panel-heading">
                                                            <h2 class="panel-title">Credits</h2>
                                                        </header>
                                                        <div class="panel-body">
                                                            <div class="modal-wrapper">
                                                                <div class="modal-text" style="height: 500px;">
                                                                    <textarea style="height:100%; width: 100%;"><%if (tracks.get(i).getCredits() != null) {
                                                                            out.print(tracks.get(i).getCredits());
                                                                        }%></textarea>
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
                                                <button type="button" class="btn btn-default btn-block" onclick="javascript:getDownloadLink('<%=tracks.get(i).getId()%>');">View</button>
                                            </td>
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
