<%@page import="EntityManager.Account"%>
<%@page import="EntityManager.Member"%>
<%@page import="EntityManager.Artist"%>
<section id="main-nav-wrapper">
    <div id="main-nav">
        <!-- ############ search ############ -->
        <div id="search-wrap">
            <div class="container">
                <input type="text" placeholder="Search and hit enter..." name="s" id="search" onkeydown="javascript:showUser();"/>
                <input type="hidden" id="btnSearch" value="Search" onclick="searchAjax()" />
                <span id="close-search"><i class="icon icon-close"></i></span>
            </div>
        </div>
        <!-- /search -->
        <!-- navigation container -->
        <div class="container">
            <script>
                function showUser(e) {
                    var keycode = (window.event) ? window.event.keyCode : e.keyCode;
                    if (keycode == 13) {
                        document.getElementById('btnSearch').click();
                    }
                }

                function retrieveGenre() {
                    url = "./MusicController?target=ListGenreArtist";
                    $.ajax({
                        type: "GET",
                        async: false,
                        url: url,
                        dataType: "text",
                        success: function (val) {
                            //  window.event.returnValue = true;
                            var json = JSON.parse(val);
                            if (json.result) {
                                //  window.event.returnValue = false;
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
                                window.location.href = "#!/redirect";
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
                            //urlrewrite push state before redirecting
                            window.history.pushState("", "", "/#!/");
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
                <img src="placeholders/logo.png" alt="Sounds.sg" style="padding-top: 5px;">
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
                    <li>
                        <a style="cursor: pointer;" onclick="javascript:retrieveGenre();">explore</a>
                    </li>
                    <%
                        Account account = (Account) (session.getAttribute("artist"));
                        Artist artist = (Artist) (session.getAttribute("artist"));
                        Member fan = (Member) (session.getAttribute("member"));

                        if (artist != null) {
                    %>
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
                    <li>
                        <a href="ClientAccountManagementController?target=PageRedirect&source=transactionHistory">transaction history</a>
                    </li> 
                    <li>
                        <a href="ClientAccountManagementController?target=AccountLogout">logout</a>
                    </li>
                    <%} else {%>
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
                        <%
                            String ua = request.getHeader("User-Agent").toLowerCase();
                            if (ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows ce|xda|xiino).*") || ua.substring(0, 4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")) {
                                out.print("<a href='#!/login'>login</a>");
                            } else {
                        %>
                        <a>login</a>
                        <ul>
                            <li>
                                <form class="form" action="ClientAccountManagementController" style="margin-bottom: 10px;">
                                    <div class="row clearfix">
                                        <div class="col-1-1" style="width: 90%; margin: 20px 10px 20px 10px;">
                                            <input type="email" name="email" placeholder="email" required >
                                        </div>
                                    </div>
                                    <div class="row clearfix">
                                        <div class="col-1-1" style="width: 90%; margin: 0 10px 20px 10px;">
                                            <input type="password" name="password" placeholder="password" required>
                                        </div>
                                    </div>
                                    <div class="row clearfix">
                                        <div class="col-1-1" style="margin: 0 0 0 10px;">
                                            <input type='submit' value="Login" class="medium invert">
                                        </div>
                                    </div>
                                    <input type="hidden" value="AccountLogin" name="target">
                                </form>
                            </li>
                        </ul>
                        <%}%>
                    </li>
                    <%}%>
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
