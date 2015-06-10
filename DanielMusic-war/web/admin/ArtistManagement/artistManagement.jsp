<%@page import="EntityManager.Artist"%>
<%@page import="EntityManager.Admin"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Admin admin = (Admin) (session.getAttribute("admin"));
    if (session.isNew()) {
        response.sendRedirect("../login.jsp?errMsg=Invalid Request. Please login.");
    } else if (admin == null) {
        response.sendRedirect("../login.jsp?errMsg=Session Expired.");
    } else {
        List<Artist> artists = (List<Artist>) (session.getAttribute("artists"));
%>
<!doctype html>
<html class="fixed">
    <head>
        <jsp:include page="../head.html" />
    </head>
    <body onload="alertFunc()">
        <jsp:include page="../displayNotification.jsp" />

        <script>
            function refresh() {
                window.location.href = "../../AccountManagementController?target=ListAllArtist";
            }
        </script>

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
                                <li><span>Artist Management &nbsp;&nbsp</span></li>
                            </ol>
                        </div>
                    </header>

                    <!-- start: page -->

                    <section class="panel">
                        <header class="panel-heading">
                            <h2 class="panel-title">Artist Management</h2>
                        </header>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-md-12"> 
                                    <button type="button" class="btn btn-default" onclick="javascript:refresh()"><i class="fa fa-refresh"></i> Refresh</button>
                                </div>
                            </div>
                            <br/>
                            <form name="artistManagement">
                                <table class="table table-bordered table-striped mb-none" id="datatable-default">
                                    <thead>
                                        <tr>
                                            <th>Name</th>
                                            <th>Email</th>
                                            <th style="width: 300px;">Status</th>
                                            <th style="width: 300px; text-align: center;">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            if (artists != null && artists.size() > 0) {
                                                for (int i = 0; i < artists.size(); i++) {
                                        %>
                                        <tr>        
                                            <td><%=artists.get(i).getName()%></td>
                                            <td><%=artists.get(i).getEmail()%></td>
                                            <td>
                                                <%
                                                    if (!artists.get(i).getIsDisabled()) {
                                                        out.print("<span class='label label-success' style='font-size: 100%;'>Active</span>");
                                                    } else {
                                                        out.print("<span class='label label-warning' style='font-size: 100%; background-color:#B8B8B8;'>Disabled</span>");
                                                    }
                                                    if (artists.get(i).getIsApproved() == 0) {
                                                        out.print("<span class='label label-success' style='font-size: 100%;'>New</span>");
                                                    } else if (artists.get(i).getIsApproved() == 1) {
                                                        out.print("<span class='label label-success' style='font-size: 100%;'>Approve</span>");
                                                    } else if (artists.get(i).getIsApproved() == -2) {
                                                        out.print("<span class='label label-success' style='font-size: 100%;'>Pending</span>");
                                                    } else {
                                                        out.print("<span class='label label-warning' style='font-size: 100%; background-color:#B8B8B8;'>Not approved</span>");
                                                    }

                                                    if (artists.get(i).getEmailIsVerified()) {
                                                        out.print("<span class='label label-success' style='font-size: 100%;'>Verified</span>");
                                                    } else {
                                                        out.print("<span class='label label-warning' style='font-size: 100%; background-color:#B8B8B8;'>Unverified</span>");
                                                    }
                                                %>
                                            </td>
                                            <td>
                                                <% if (!artists.get(i).getIsDisabled()) {%>
                                                <button type="button" class="modal-with-move-anim btn btn-default btn-block"  href="#modalRemove">Disable</button>
                                                <%}%>
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