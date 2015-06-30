<%@page import="EntityManager.Artist"%>
<%@page import="EntityManager.Admin"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Admin admin = (Admin) (session.getAttribute("admin"));
    if (session.isNew() || admin == null) {
        response.sendRedirect("../login.jsp?errMsg=Session Expired.");
    } else {
        List<Artist> artists = (List<Artist>) (session.getAttribute("artists"));
%>
<!doctype html>
<html class="fixed">
    <head>
        <jsp:include page="../jspIncludePages/head.html" />
    </head>
    <body onload="alertFunc()">
        <jsp:include page="../jspIncludePages/displayNotification.jsp" />
        <script>
            function refresh() {
                window.location.href = "../../AccountManagementController?target=ListAllArtist";
            }
            function viewArtist(id) {
                window.location.href = "../../AccountManagementController?target=ListArtistbyID&id=" + id;
            }
            function disableAccount(id) {
                artistManagement.id.value = id;
                artistManagement.target.value = "DisableAccount";
                document.artistManagement.action = "../../AccountManagementController";
                document.artistManagement.submit();
            }
        </script>

        <section class="body">
            <jsp:include page="../jspIncludePages/header.jsp" />

            <div class="inner-wrapper">
                <jsp:include page="../jspIncludePages/sidebar.jsp" />
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
                                            <th style="width: 100px;">Status</th>
                                            <th style="width: 100px;">Email</th>
                                            <th style="width: 100px;">Disabled?</th>
                                            <th style="width: 200px; text-align: center;">Action</th>
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
                                                    if (artists.get(i).getIsApproved() == 0) {
                                                        out.print("<span class='label label-info' style='font-size: 100%;'>New</span>");
                                                    } else if (artists.get(i).getIsApproved() == 1) {
                                                        out.print("<span class='label label-success' style='font-size: 100%;'>Approved</span>");
                                                    } else if (artists.get(i).getIsApproved() == -2) {
                                                        out.print("<span class='label label-warning' style='font-size: 100%;'>Pending</span>");
                                                    } else {
                                                        out.print("<span class='label label-danger' style='font-size: 100%;'>Rejected</span>");
                                                    }
                                                %>
                                            </td>
                                            <td>
                                                <%
                                                    if (artists.get(i).getEmailIsVerified()) {
                                                        out.print("<span class='label label-success' style='font-size: 100%;'>Verified</span>");
                                                    } else {
                                                        out.print("<span class='label label-success' style='font-size: 100%; background-color:#B8B8B8;'>Not verified</span>");
                                                    }
                                                %>
                                            </td>
                                            <td>
                                                <%
                                                    if (!artists.get(i).getIsDisabled()) {
                                                        out.print("<span class='label label-success' style='font-size: 100%;'>Active</span>");
                                                    } else {
                                                        out.print("<span class='label label-danger' style='font-size: 100%;'>Disabled</span>");
                                                    }
                                                %>
                                            </td>
                                            <td>
                                                <% if (!artists.get(i).getIsDisabled()) {%>
                                                <div class='btn-group'>
                                                    <button type="button" class="btn btn-default" onclick="viewArtist(<%=artists.get(i).getId()%>)">View</button>
                                                    <button type="button" class="modal-with-move-anim btn btn-default"  href="#modalRemove">Disable</button>
                                                </div>
                                                <div id="modalRemove" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
                                                    <section class="panel">
                                                        <header class="panel-heading">
                                                            <h2 class="panel-title">Are you sure?</h2>
                                                        </header>
                                                        <div class="panel-body">
                                                            <div class="modal-wrapper">
                                                                <div class="modal-icon">
                                                                    <i class="fa fa-question-circle"></i>
                                                                </div>
                                                                <div class="modal-text">
                                                                    <p>Are you sure that you want to disable this account?</p>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <footer class="panel-footer">
                                                            <div class="row">
                                                                <div class="col-md-12 text-right">
                                                                    <input class="btn btn-primary modal-confirm" name="btnRemove" type="submit" value="Confirm" onclick="disableAccount(<%=artists.get(i).getId()%>)"  />
                                                                    <button class="btn btn-default modal-dismiss">Cancel</button>
                                                                </div>
                                                            </div>
                                                        </footer>
                                                    </section>
                                                </div>
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
                                <input type="hidden" name="source" value="artistManagement"> 
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
<%    }
%>