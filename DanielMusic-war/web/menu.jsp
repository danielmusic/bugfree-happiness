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
                            out.print("<li><a href='#!/artist/albums'>albums</a><li>");
                            out.print("<li><a href='#!/artist/profile'>profile</a><li>");
                            out.print("<li><a href='ClientAccountManagementController?target=AccountLogout'>logout</a><li>");
                        } else if (band != null) {
                            out.print("<li><a href='#!/band/albums'>albums</a><li>");
                            out.print("<li><a href='#!/band/profile'>profile</a><li>");
                            out.print("<li><a href='ClientAccountManagementController?target=AccountLogout'>logout</a><li>");
                        } else if (fan != null) {

                        } else {
                            out.print("<li><a href='#!/discover'>discover</a><li>");
                            out.print("<li class='submenu'><a href='#!/artist'>artist</a>");
                            out.print("<ul><li><a href='#!/artist/signup'>artist signup</a></li></ul></li>");
                            out.print("<li class='submenu'><a href='#!/fan'>fan</a>");
                            out.print("<ul><li><a href='#!/fan/signup'>fan signup</a></li></ul></li>");
                            out.print("<li><a href='#!/login'>login</a><li>");
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