<%@page import="EntityManager.Album"%>
<%@page import="EntityManager.Artist"%>
<%@page import="EntityManager.Admin"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Admin admin = (Admin) (session.getAttribute("admin"));
    Artist artist = (Artist) (session.getAttribute("artist"));
    if (session.isNew() || admin == null) {
        response.sendRedirect("../login.jsp?errMsg=Session Expired.");
    } else if (artist == null) {
        response.sendRedirect("../AccountManagement/artistManagement.jsp?errMsg=An error has occured.");
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
            function viewArtistTrack(id) {
                window.location.href = "artistTracks.jsp?id=" + id;
            }

            function back() {
                window.location.href = "artist.jsp";
            }
        </script>

        <section class="body">
            <jsp:include page="../jspIncludePages/header.jsp" />

            <div class="inner-wrapper">
                <jsp:include page="../jspIncludePages/sidebar.jsp" />
                <section role="main" class="content-body">
                    <header class="page-header">
                        <h2>Albums - <%=artist.getName()%></h2>
                        <div class="right-wrapper pull-right">
                            <ol class="breadcrumbs">
                                <li><a href="admin/workspace.jsp"><i class="fa fa-home"></i></a></li>
                                <li><a href="../../AccountManagementController?target=ListAllArtist">Artist Management</a></li>
                                <li><a href="artist.jsp"><%=artist.getName()%></a></li>
                                <li><span>Artist Albums &nbsp;&nbsp</span></li>
                            </ol>
                        </div>
                    </header>

                    <!-- start: page -->
                    <section class="panel">
                        <header class="panel-heading">
                            <h2 class="panel-title">Albums - <%=artist.getName()%></h2>
                        </header>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-md-12"> 
                                    <button type="button" class="btn btn-default" onclick="javascript:back()"> Back</button>
                                </div>
                            </div>
                            <br/>
                            <form name="artistAlbumManagement">
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
                                            if (artist != null) {
                                                List<Album> albums = artist.getListOfAlbums();
                                                for (int i = 0; i < albums.size(); i++) {
                                        %>
                                        <tr>        
                                            <td><%=albums.get(i).getName()%></td>
                                            <td><%=albums.get(i).getYearReleased()%></td>
                                            <td><%=albums.get(i).getPrice()%></td>

                                            <td>
                                                <%if (albums.get(i).getImageLocation() != null && !albums.get(i).getImageLocation().isEmpty()) {%>
                                                <a class="modal-with-move-anim btn btn-default btn-block" href="#modalAlbumArt<%=albums.get(i).getId()%>">View</a>
                                                <div id="modalAlbumArt<%=albums.get(i).getId()%>" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
                                                    <section class="panel">
                                                        <header class="panel-heading">
                                                            <h2 class="panel-title">Album Artwork</h2>
                                                        </header>
                                                        <div class="panel-body">
                                                            <div class="modal-wrapper">
                                                                <div class="modal-text">
                                                                    <img src="http://sounds.sg.storage.googleapis.com/<%=albums.get(i).getImageLocation()%>">
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
                                                <%if (albums.get(i).getDescription()!= null && !albums.get(i).getDescription().isEmpty()) {%>
                                                <a class="modal-with-move-anim btn btn-default btn-block" href="#modalDescription<%=albums.get(i).getId()%>">View</a>
                                                <div id="modalDescription<%=albums.get(i).getId()%>" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
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
                                                <a class="modal-with-move-anim btn btn-default btn-block" href="#modalCredits<%=albums.get(i).getId()%>">View</a>
                                                <div id="modalCredits<%=albums.get(i).getId()%>" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
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

                                            <td><button type="button" class="btn btn-default btn-block" onclick="javascript:viewArtistTrack('<%=albums.get(i).getId()%>');">View Tracks</button></td>
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
