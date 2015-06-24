<%@page import="EntityManager.Artist"%>
<%@page import="EntityManager.Genre"%>
<%@page import="EntityManager.Admin"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Admin admin = (Admin) (session.getAttribute("admin"));
    List<Artist> artists = (List<Artist>) (session.getAttribute("artists"));
    String id = request.getParameter("id");
    if (session.isNew() || admin == null) {
        response.sendRedirect("../login.jsp?errMsg=Session Expired.");
    } else if (id == null || id.isEmpty() || artists == null) {
        response.sendRedirect("../artistManagement.jsp?errMsg=An error has occured.");
    } else {
        Artist artist = new Artist();
        for (int i = 0; i < artists.size(); i++) {
            if (artists.get(i).getId() == Long.parseLong(id)) {
                artist = artists.get(i);
            }
        }
%>
<!doctype html>
<html class="fixed">
    <head>
        <jsp:include page="../head.html" />
    </head>
    <body onload="alertFunc()">
        <script>
            function back() {
                window.location.href = "../../AccountManagementController?target=ListAllArtist";
            }
        </script>
        <jsp:include page="../displayNotification.jsp" />
        <section class="body">
            <jsp:include page="../header.jsp" />

            <div class="inner-wrapper">
                <jsp:include page="../sidebar.jsp" />
                <section role="main" class="content-body">
                    <header class="page-header">
                        <h2>Artist Management</h2>
                        <div class="right-wrapper pull-right">
                            <ol class="breadcrumbs">
                                <li>
                                    <a href="admin/workspace.jsp">
                                        <i class="fa fa-home"></i>
                                    </a>
                                </li>
                                <li><span>Artist Management</span></li>
                                <li><span>Artist &nbsp;&nbsp</span></li>
                            </ol>
                        </div>
                    </header>

                    <!-- start: page -->
                    <div class="row">
                        <div class="col-lg-12">
                            <form class="form-horizontal form-bordered" action="../../GenreManagementController">
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
                                                           out.print(artist.getGenre());
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