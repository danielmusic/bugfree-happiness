<%@page import="EntityManager.Admin"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Admin admin = (Admin) (session.getAttribute("admin"));
    if (session.isNew() || admin == null) {
        response.sendRedirect("login.jsp?errMsg=Session Expired.");
    } else {
%>
<!doctype html>
<html class="fixed">
    <head>
        <jsp:include page="jspIncludePages/head-outsidefolder.html" />
    </head>
    <body onload="alertFunc()">
        <jsp:include page="jspIncludePages/displayNotification.jsp" />
        <jsp:include page="jspIncludePages/header-outsidefolder.jsp" />

        <div class="inner-wrapper">

            <jsp:include page="jspIncludePages/siderbar-outsidefolder.jsp" />

            <section role="main" class="content-body">
                <header class="page-header">
                    <h2>Workspace</h2>
                    <div class="right-wrapper pull-right">
                        <ol class="breadcrumbs">
                            <li><i class="fa fa-home"></i></li>
                            <li><span>Workspace &nbsp;&nbsp</span></li>
                        </ol>
                    </div>
                </header>

                <form method="POST" class="form-horizontal form-bordered" action="../AccountManagementController">
                    <section class="panel">
                        <header class="panel-heading">
                            <h2 class="panel-title">Change Password</h2>
                        </header>

                        <div class="panel-body">
                            <div class="form-group">
                                <label class="col-md-3 control-label">Old Password</label>
                                <div class="col-md-6">
                                    <input id="oldpassword" type="password" name="oldPwd" class="form-control" onchange="form.repassword.pattern = this.value;" required>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-3 control-label">New Password</label>
                                <div class="col-md-6">
                                    <input id="password" type="password" title="Password should contain at least 6 characters, including UPPER/lowercase and numbers" name="pwd" class="form-control" onchange="form.repassword.pattern = this.value;" required>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-3 control-label">Re-enter Password</label>
                                <div class="col-md-6">
                                    <input id="repassword" type="password" name="repassword" class="form-control" required>
                                </div>
                            </div>

                            <input type="hidden" name="id" value="<%=admin.getId()%>">   
                            <input type="hidden" name="email" value="<%=admin.getEmail()%>">   
                            <input type="hidden" name="target" value="ChangePassword"> 
                        </div>

                        <footer class="panel-footer">
                            <div class="row">
                                <div class="col-sm-9 col-sm-offset-3">
                                    <button class="btn btn-primary" type="submit">Submit</button>
                                </div>
                            </div>
                        </footer>
                    </section>
                </form>

            </section>
        </div>

        <jsp:include page="jspIncludePages/foot-outsidefolder.html" />
    </section>

</body>
</html>
<%
    }
%>