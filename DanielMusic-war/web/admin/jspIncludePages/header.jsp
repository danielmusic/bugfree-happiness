<%@page import="EntityManager.Admin"%>
<header class="header">
    <div class="logo-container">
        <a href="../workspace.jsp" class="logo">
            <img src="../assets/images/logo.png" height="35" />
        </a>
        <div class="visible-xs toggle-sidebar-left" data-toggle-class="sidebar-left-opened" data-target="html" data-fire-event="sidebar-left-opened">
            <i class="fa fa-bars" aria-label="Toggle sidebar"></i>
        </div>
    </div>

    <!-- start: search & user box -->
    <div class="header-right">
        <span class="separator"></span>
        <div id="userbox" class="userbox">
            <a href="#" data-toggle="dropdown">
                <figure class="profile-picture">
                    <img src="../assets/images/!logged-user.jpg" alt="" class="img-circle" data-lock-picture="assets/images/!logged-user.jpg" />
                </figure>
                <div class="profile-info">
                    <%Admin admin = (Admin) (session.getAttribute("admin"));%>
                    <span class="name"><%=admin.getName()%></span>
                    <span class="role"><%=admin.getEmail()%></span>
                </div>

                <i class="fa custom-caret"></i>
            </a>

            <div class="dropdown-menu">
                <ul class="list-unstyled">
                    <li class="divider"></li>
                    <!-- 
                    <li>
                        <a role="menuitem" tabindex="-1" href="pages-user-profile.html"><i class="fa fa-user"></i> My Profile</a>
                    </li>
                    -->
                    <li>
                        <a role="menuitem" tabindex="-1" href="../changePassword.jsp"><i class="fa fa-unlock-alt"></i> Change Password</a>
                    </li>
                    <li>
                        <a role="menuitem" tabindex="-1" href="../../AccountManagementController?target=Logout"><i class="fa fa-power-off"></i> Logout</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <!-- end: search & user box -->
</header>