<%@page import="EntityManager.Artist"%>
<%@page import="EntityManager.Genre"%>
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
        <script>
            function back() {
                window.location.href = "../../AccountManagementController?target=ListAllBand";
            }

            function viewBandAlbum() {
                window.location.href = "bandAlbums.jsp";
            }

            function approveBand(id) {
                window.location.href = "../../AccountManagementController?target=ApproveArtist&source=band&id=" + id;
            }

            function rejectBand(id) {
                window.location.href = "../../AccountManagementController?target=RejectArtist&source=band&id=" + id;
            }
        </script>
        <jsp:include page="../jspIncludePages/displayNotification.jsp" />
        <section class="body">
            <jsp:include page="../jspIncludePages/header.jsp" />

            <div class="inner-wrapper">
                <jsp:include page="../jspIncludePages/sidebar.jsp" />
                <section role="main" class="content-body">
                    <header class="page-header">
                        <h2>Band</h2>
                        <div class="right-wrapper pull-right">
                            <ol class="breadcrumbs">
                                <li><a href="admin/workspace.jsp"><i class="fa fa-home"></i></a></li>
                                <li><a href="../../AccountManagementController?target=ListAllBand">Band Management</a></li>
                                <li><span>Band &nbsp;&nbsp</span></li>
                            </ol>
                        </div>
                    </header>

                    <!-- start: page -->
                    <div class="row">
                        <div class="col-lg-12">
                            <form class="form-horizontal form-bordered">
                                <section class="panel">
                                    <header class="panel-heading">
                                        <h2 class="panel-title">Band</h2>
                                    </header>
                                    <div class="panel-body">

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Profile Pic</label>
                                            <div class="col-md-6">
                                                <img src="<%=band.getImageURL()%>">
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Name</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%=band.getName()%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Account Email</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (band.getEmail() != null) {
                                                        out.print(band.getEmail());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Contact Email</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (band.getContactEmail() != null) {
                                                        out.print(band.getContactEmail());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">PayPal Email</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (band.getPaypalEmail() != null) {
                                                        out.print(band.getPaypalEmail());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Biography</label>
                                            <div class="col-md-6">
                                                <textarea class="form-control" rows="3" name="address" disabled><%if (band.getBiography() != null) {
                                                        out.print(band.getBiography());
                                                    }%></textarea>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Genre</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (band.getGenre() != null) {
                                                        out.print(band.getGenre().getName());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Influences</label>
                                            <div class="col-md-6">
                                                <textarea class="form-control" rows="3" name="address" disabled><%if (band.getInfluences() != null) {
                                                        out.print(band.getInfluences());
                                                    }%></textarea>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Members</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%=band.getBandMembers()%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Date Formed</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%=band.getBandDateFormed()%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Facebook URL</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (band.getFacebookURL() != null) {
                                                        out.print(band.getFacebookURL());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Twitter URL</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (band.getTwitterURL() != null) {
                                                        out.print(band.getTwitterURL());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Instagram URL</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (band.getInstagramURL() != null) {
                                                        out.print(band.getInstagramURL());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Website</label>
                                            <div class="col-md-6">
                                                <input type="text" class="form-control" value="<%if (band.getWebsiteURL() != null) {
                                                        out.print(band.getWebsiteURL());
                                                    }%>" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label"></label>
                                            <div class="col-md-6">
                                                <button type="button" class="btn btn-default btn-block" onclick="javascript:viewBandAlbum();">View Albums</button>
                                            </div>
                                        </div>   
                                    </div>

                                    <footer class="panel-footer">
                                        <div class="row">
                                            <div class="col-sm-9 col-sm-offset-3">
                                                <button type="button" class="btn btn-success" onclick="javascript:approveBand('<%=band.getId()%>');">Approve</button>
                                                <button type="button" class="btn btn-danger" onclick="javascript:rejectBand('<%=band.getId()%>');">Reject</button>
                                                <button type="button" class="btn btn-default" onclick="javascript:back();">Back</button>
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
<%
    }
%>