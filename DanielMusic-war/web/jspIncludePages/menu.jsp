<%@page import="EntityManager.Account"%>
<%@page import="EntityManager.Member"%>
<%@page import="EntityManager.Artist"%>
<section id="main-nav-wrapper">
    <div id="main-nav">
        <!-- ############ search ############ -->
        <div id="search-wrap">
            <div class="container">
                <input type="text" placeholder="Search and hit enter..." name="s" id="search" onkeydown="if (event.keyCode == 13)
                            document.getElementById('btnSearch').click()"/>
                <input type="hidden" id="btnSearch" value="Search" onclick="searchAjax()" />
                <span id="close-search"><i class="icon icon-close"></i></span>
            </div>
        </div>
        <!-- /search -->
        <!-- navigation container -->
        <div class="container">
            <script>
                function loadAjax() {
                    url = "./MusicController?target=ListGenreArtist";
                    $.ajax({
                        type: "GET",
                        async: false,
                        url: url,
                        data: {},
                        dataType: "text",
                        success: function (val) {
                            window.event.returnValue = true;
                            var json = JSON.parse(val);
                            if (json.result) {
                                window.event.returnValue = false;
                                window.location.href = "#!/explore";
                            }
                        },
                        error: function (xhr, status, error) {
                            document.getElementById("errMsg").style.display = "block";
                            document.getElementById('errMsg').innerHTML = error;
                            hideLoader();
                            ajaxResultsError(xhr, status, error);
                        }
                    });
                }

                function searchAjax() {
                    var text = document.getElementById("search").value;
                    url = "./MusicController?target=Search";
                    $.ajax({
                        type: "GET",
                        async: false,
                        url: url,
                        data: {'text': text},
                        dataType: "text",
                        success: function (val) {
                            window.event.returnValue = true;
                            var json = JSON.parse(val);
                            if (json.result) {
                                window.event.returnValue = false;
                                window.location.href = "#!/search";
                            }
                        },
                        error: function (xhr, status, error) {
                            document.getElementById("errMsg").style.display = "block";
                            document.getElementById('errMsg').innerHTML = error;
                            hideLoader();
                            ajaxResultsError(xhr, status, error);
                        }
                    });
                }

                function getShoppingCart() {
                    url = "./MusicManagementController?target=GetShoppingCart";
                    $.ajax({
                        type: "GET",
                        async: false,
                        url: url,
                        data: {},
                        success: function (val) {
                            window.event.returnValue = false;
                            window.location.href = "#!/cart";
                        },
                        error: function (xhr, status, error) {
                            document.getElementById("errMsg").style.display = "block";
                            document.getElementById('errMsg').innerHTML = error;
                            hideLoader();
                            ajaxResultsError(xhr, status, error);
                        }
                    });
                }
            </script>


            <a href="#!/home" id="logo">
                <img src="placeholders/logo.png" alt="Logo">
            </a>

            <!-- ############ icon navigation ############ -->
            <nav id="icon-nav">
                <ul>
                    <li><a onclick="javascript:getShoppingCart();" id="nav-up"><span class="icon icon-cart"></span></a></li>
                    <li><a onclick="javascript:;" id="nav-search" class="external"><span class="icon icon-search"></span></a></li>
                </ul>
            </nav>
            <!-- /icon navigation -->

            <!-- ############ navigation ############ -->
            <nav id="nav">
                <ul>
                    <%
                        Account account = (Account) (session.getAttribute("artist"));
                        Artist artist = (Artist) (session.getAttribute("artist"));
                        Member fan = (Member) (session.getAttribute("fan"));

                        if (artist != null) {
                    %>
                    <li>
                        <a style="cursor: pointer;" onclick="javascript:loadAjax();">explore</a>
                    </li>
                    <li class="submenu">
                        <a href="ClientAccountManagementController?target=PageRedirect&source=profile">manage</a>
                        <ul>
                            <li>
                                <a href="ClientAccountManagementController?target=PageRedirect&source=profile">profile</a>
                            </li> 
                            <li>
                                <a href="ClientAccountManagementController?target=PageRedirect&source=albums">albums</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="MusicManagementController?target=GetArtistByID&id=<%=account.getId()%>">my page</a>
                    </li>
                    <li>
                        <a href="ClientAccountManagementController?target=AccountLogout">logout</a>
                    </li>
                    <%} else if (fan != null) {%>

                    <%} else {%>
                    <li>
                        <a style="cursor: pointer;" onclick="javascript:loadAjax();">explore</a>
                    </li>
                    <li>
                        <a href="#!/artist">artist</a>
                    </li>
                    <li>
                        <a href="#!/fan">fan</a>
                    </li>
                    <li class="submenu">
                        <a>sign up</a>
                        <ul>
                            <li>
                                <a href='#!/artist/signup'>Artist signup</a>
                            </li>
                            <li>
                                <a href='#!/fan/signup'>Fan signup</a>
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