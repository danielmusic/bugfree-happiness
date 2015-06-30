<%@page import="EntityManager.Band"%>
<%@page import="EntityManager.Member"%>
<%@page import="EntityManager.Artist"%>
<section id="main-nav-wrapper">
    <div id="main-nav">
        <!-- ############ search ############ -->
        <div id="search-wrap">
            <div class="container">
                <input type="text" placeholder="Search and hit enter..." name="s" id="search" />
                <span id="close-search"><i class="icon icon-close"></i></span>
            </div>
        </div>
        <!-- /search -->
        <!-- navigation container -->
        <div class="container">
            <a href="#!/home" id="logo">
                <img src="placeholders/logo.png" alt="Logo">
            </a>

            <!-- ############ icon navigation ############ -->
            <nav id="icon-nav">
                <ul>
                    <li><a href="#main-nav-wrapper" id="nav-up" class="smooth-scroll"><span class="icon icon-arrow-up"></span></a></li>
                    <li><a href="javascript:;" id="nav-search" class="external"><span class="icon icon-search"></span></a></li>
                </ul>
            </nav>
            <!-- /icon navigation -->

            <!-- ############ navigation ############ -->
            <nav id="nav">
                <ul>
                    <%
                        Artist artist = (Artist) (session.getAttribute("artist"));
                        Band band = (Band) (session.getAttribute("band"));
                        Member fan = (Member) (session.getAttribute("fan"));

                        if (artist != null) {
                    %>
                    <li>
                        <a href="ClientAccountManagementController?target=PageRedirect&source=albums">albums</a>
                    </li>
                    <li class="submenu">
                        <a href="ClientAccountManagementController?target=PageRedirect&source=profile">profile</a>
                        <ul>
                            <li>
                                <a href="ClientAccountManagementController?target=PageRedirect&source=profile">update profile</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="ClientAccountManagementController?target=AccountLogout">logout</a>
                    </li>
                    <%} else if (band != null) {%>
                    <li>
                        <a href="#!/band/albums">albums</a>
                    </li>
                    <li class="submenu">
                        <a href="#!/band/profile">profile</a>
                        <ul>
                            <li>
                                <a href='#!/band/profile'>update profile</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="ClientAccountManagementController?target=AccountLogout">logout</a>
                    </li>
                    <%} else if (fan != null) {%>

                    <%} else {%>
                    <li>
                        <a href="#!/artists">Single Artist (test)</a>
                    </li>
                    <li>
                        <a href="#!/explore">explore</a>
                    </li>
                    <li class="submenu">
                        <a href="#!/artist">artist</a>
                        <ul>
                            <li>
                                <a href='#!/artist/signup'>artist signup</a>
                            </li>
                        </ul>
                    </li>
                    <li class="submenu">
                        <a href="#!/fan">fan</a>
                        <ul>
                            <li>
                                <a href='#!/artist/signup'>fan signup</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="#!/login">login</a>
                    </li>
                    <%
                        }
                    %>
                </ul>
            </nav>
            <!-- /navigation -->

            <!-- responsive navigation -->
            <div id="dl-menu" class="dl-menuwrapper one-page-nav">
                <button class="dl-trigger">Open Menu</button>
                <!-- RESPONSIVE NAVIGATION HERE -->
            </div>
        </div>
    </div>
</section>