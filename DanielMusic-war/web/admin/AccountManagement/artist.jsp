<%@page import="EntityManager.Artist"%>
<%@page import="EntityManager.Genre"%>
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
        <script>
            function back() {
                window.location.href = "../../AccountManagementController?target=ListAllArtist";
            }

            function viewArtistAlbum() {
                window.location.href = "artistAlbums.jsp";
            }

            function approveArtist(id) {
                window.location.href = "../../AccountManagementController?target=ApproveArtist&source=artist&id=" + id;
            }

            function rejectArtist(id) {
                window.location.href = "../../AccountManagementController?target=RejectArtist&source=artist&id=" + id;
            }
        </script>
        <jsp:include page="../jspIncludePages/displayNotification.jsp" />
        <section class="body">
            <jsp:include page="../jspIncludePages/header.jsp" />

            <div class="inner-wrapper">
                <jsp:include page="../jspIncludePages/sidebar.jsp" />
                <section role="main" class="content-body">
                    <header class="page-header">
                        <h2>Artist</h2>
                        <div class="right-wrapper pull-right">
                            <ol class="breadcrumbs">
                                <li><a href="admin/workspace.jsp"><i class="fa fa-home"></i></a></li>
                                <li><a href="../../AccountManagementController?target=ListAllArtist">Artist Management</a></li>
                                <li><span>Artist &nbsp;&nbsp</span></li>
                            </ol>
                        </div>
                    </header>

                    <!-- start: page -->
                    <div class="row">
                        <div class="col-lg-12">
                            <form class="form-horizontal form-bordered">
                                <section class="panel">
                                    <header class="panel-heading">
                                        <h2 class="panel-title">Artist</h2>
                                    </header>
                                    <div class="panel-body">

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Profile Pic</label>
                                            <div class="col-md-6">
                                                <img src="<%=artist.getImageURL()%>">
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Name</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%=artist.getName()%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Account Email</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (artist.getEmail() != null) {
                                                        out.print(artist.getEmail());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Contact Email</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (artist.getContactEmail() != null) {
                                                        out.print(artist.getContactEmail());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">PayPal Email</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (artist.getPaypalEmail() != null) {
                                                        out.print(artist.getPaypalEmail());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Biography</label>
                                            <div class="col-md-6">
                                                <textarea class="form-control" rows="3" name="address" disabled><%if (artist.getBiography() != null) {
                                                        out.print(artist.getBiography());
                                                    }%></textarea>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Genre</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (artist.getGenre() != null) {
                                                        out.print(artist.getGenre().getName());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Influences</label>
                                            <div class="col-md-6">
                                                <textarea class="form-control" rows="3" name="address" disabled><%if (artist.getInfluences() != null) {
                                                        out.print(artist.getInfluences());
                                                    }%></textarea>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Facebook URL</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (artist.getFacebookURL() != null) {
                                                        out.print(artist.getFacebookURL());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Twitter URL</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (artist.getTwitterURL() != null) {
                                                        out.print(artist.getTwitterURL());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Instagram URL</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (artist.getInstagramURL() != null) {
                                                        out.print(artist.getInstagramURL());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Website</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (artist.getWebsiteURL() != null) {
                                                        out.print(artist.getWebsiteURL());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label"></label>
                                            <div class="col-md-6">
                                                <button type="button" class="btn btn-default btn-block" onclick="javascript:viewArtistAlbum();">View Albums</button>
                                            </div>
                                        </div>
                                    </div>

                                    <footer class="panel-footer">
                                        <div class="row">
                                            <div class="col-sm-9 col-sm-offset-3">
                                                <button type="button" class="btn btn-success" onclick="javascript:approveArtist('<%=artist.getId()%>');">Approve</button>
                                                <button type="button" class="btn btn-danger" onclick="javascript:rejectArtist('<%=artist.getId()%>');">Reject</button>
                                                <button type="button"  class="btn btn-default" onclick="javascript:back();">Back</button>
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

        <jsp:include page="../jspIncludePages/foot.html" />
    </body>
</html>
<%    }
%>