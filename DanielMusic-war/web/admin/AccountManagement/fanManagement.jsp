<%@page import="EntityManager.Member"%>
<%@page import="EntityManager.Artist"%>
<%@page import="EntityManager.Admin"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Admin admin = (Admin) (session.getAttribute("admin"));
    if (session.isNew() || admin == null) {
        response.sendRedirect("../login.jsp?errMsg=Session Expired.");
    } else {
        List<Member> fans = (List<Member>) (session.getAttribute("fans"));
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
                window.location.href = "../../AccountManagementController?target=ListAllFan";
            }
            function enableAccount(id) {
                fanManagement.id.value = id;
                fanManagement.target.value = "EnableAccount";
                document.fanManagement.action = "../../AccountManagementController";
                document.fanManagement.submit();
            }
            function disableAccount(id) {
                fanManagement.id.value = id;
                fanManagement.target.value = "DisableAccount";
                document.fanManagement.action = "../../AccountManagementController";
                document.fanManagement.submit();
            }
            function deleteAccount(id) {
                fanManagement.id.value = id;
                fanManagement.target.value = "DeleteAccount";
                document.fanManagement.action = "../../AccountManagementController";
                document.fanManagement.submit();
            }
        </script>

        <section class="body">
            <jsp:include page="../jspIncludePages/header.jsp" />

            <div class="inner-wrapper">
                <jsp:include page="../jspIncludePages/sidebar.jsp" />
                <section role="main" class="content-body">
                    <header class="page-header">
                        <h2>Fan Management</h2>
                        <div class="right-wrapper pull-right">
                            <ol class="breadcrumbs">
                                <li>
                                    <a href="admin/workspace.jsp">
                                        <i class="fa fa-home"></i>
                                    </a>
                                </li>
                                <li><span>Fan Management &nbsp;&nbsp</span></li>
                            </ol>
                        </div>
                    </header>

                    <!-- start: page -->

                    <section class="panel">
                        <header class="panel-heading">
                            <h2 class="panel-title">Fan Management</h2>
                        </header>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-md-12"> 
                                    <button type="button" class="btn btn-default" onclick="javascript:refresh()"><i class="fa fa-refresh"></i> Refresh</button>
                                </div>
                            </div>
                            <br/>
                            <form name="fanManagement">
                                <table class="table table-bordered table-striped mb-none" id="datatable-default">
                                    <thead>
                                        <tr>
                                            <th>Name</th>
                                            <th>Email</th>
                                            <th>Status</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            if (fans != null && fans.size() > 0) {
                                                for (int i = 0; i < fans.size(); i++) {
                                                    if (!fans.get(i).getIsDeleted()) {
                                        %>
                                        <tr>        
                                            <td><%=fans.get(i).getName()%></td>
                                            <td><%=fans.get(i).getEmail()%></td>
                                            <td>
                                                <%
                                                    if (!fans.get(i).getIsDisabled()) {
                                                        out.print("<span class='label label-success' style='font-size: 100%;'>Enabled</span>");
                                                    } else {
                                                        out.print("<span class='label label-danger' style='font-size: 100%;'>Disabled</span>");
                                                    }

                                                    if (fans.get(i).getEmailIsVerified()) {
                                                        out.print("<span class='label label-success' style='font-size: 100%;'>Verified</span>");
                                                    } else {
                                                        out.print("<span class='label label-warning' style='font-size: 100%; background-color:#B8B8B8;'>Unverified</span>");
                                                    }
                                                %>
                                            </td>
                                            <td>
                                                <% if (!fans.get(i).getIsDisabled()) {%>
                                                <button type="button" class="modal-with-move-anim btn btn-default" href="#modalRemove<%=fans.get(i).getId()%>">Disable</button>
                                                <div id="modalRemove<%=fans.get(i).getId()%>" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
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
                                                                    <input class="btn btn-primary modal-confirm" name="btnRemove" type="submit" value="Confirm" onclick="disableAccount(<%=fans.get(i).getId()%>)"  />
                                                                    <button class="btn btn-default modal-dismiss">Cancel</button>
                                                                </div>
                                                            </div>
                                                        </footer>
                                                    </section>
                                                </div>
                                                <%} else {%>
                                                <button type="button" class="btn btn-default" onclick="enableAccount(<%=fans.get(i).getId()%>)">Enable</button> 
                                                <button type="button" class="modal-with-move-anim btn btn-default"  href="#modalDelete<%=fans.get(i).getId()%>">Delete</button>
                                                <div id="modalDelete<%=fans.get(i).getId()%>" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
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
                                                                    <p>Are you sure that you want to delete this account?</p>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <footer class="panel-footer">
                                                            <div class="row">
                                                                <div class="col-md-12 text-right">
                                                                    <input class="btn btn-primary modal-confirm" name="btnRemove" type="submit" value="Confirm" onclick="deleteAccount(<%=fans.get(i).getId()%>)"  />
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
                                            }
                                        %>

                                    </tbody>
                                </table>
                                <input type="hidden" name="id" value="">
                                <input type="hidden" name="target" value="">    
                                <input type="hidden" name="source" value="fanManagement">    
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