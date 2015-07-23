<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Shopping Cart">
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/events-bg.jpg)">
        <h1 class="heading-l">Upcoming <span class="color">Events</span></h1>
    </section>
    <script>
        function checkAllTracks(source) {
            checkboxes = document.getElementsByName('deleteTrack');
            for (var i = 0, n = checkboxes.length; i < n; i++) {
                checkboxes[i].checked = source.checked;
            }
        }
        function checkAllAlbums(source) {
            checkboxes = document.getElementsByName('deleteAlbum');
            for (var i = 0, n = checkboxes.length; i < n; i++) {
                checkboxes[i].checked = source.checked;
            }
        }

        function removeTrack() {
            alert("removeTrack");
            checkboxes = document.getElementsByName('deleteTrack');
            
            var numOfTicks = 0;
            for (var i = 0, n = checkboxes.length; i < n; i++) {
                if (checkboxes[i].checked) {
                    numOfTicks++;
                }
            }
            if (checkboxes.length == 0 || numOfTicks == 0) {
                alert("removeTrack: 00");

                window.event.returnValue = true;
                //window.location.href = "./MusicManagementController?target=RemoveTrackFromShoppingCart";
            } else {
                alert("removeTrack: no 0");

                url = "./MusicManagementController?target=RemoveTrackFromShoppingCart";
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    data: {'deleteTrack': checkboxes},
                    success: function (val) {
                        window.event.returnValue = false;
                        window.location.href = "#!/dummy";
                    },
                    error: function (xhr, status, error) {
                        document.getElementById("errMsg").style.display = "block";
                        document.getElementById('errMsg').innerHTML = error;
                        hideLoader();
                        ajaxResultsError(xhr, status, error);
                    }
                });
            }
        }
        function removeAlbum() {
            checkboxes = document.getElementsByName('deleteAlbum');
            var numOfTicks = 0;
            for (var i = 0, n = checkboxes.length; i < n; i++) {
                if (checkboxes[i].checked) {
                    numOfTicks++;
                }
            }
            if (checkboxes.length == 0 || numOfTicks == 0) {
                window.event.returnValue = true;
                //window.location.href = "./MusicManagementController?target=RemoveAlbumFromShoppingCart";
            } else {
                url = "./MusicManagementController?target=RemoveAlbumFromShoppingCart";
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    data: {'deleteAlbum': checkboxes},
                    success: function (val) {
                        window.event.returnValue = false;
                        window.location.href = "#!/dummy";
                    },
                    error: function (xhr, status, error) {
                        document.getElementById("errMsg").style.display = "block";
                        document.getElementById('errMsg').innerHTML = error;
                        hideLoader();
                        ajaxResultsError(xhr, status, error);
                    }
                });
            }
        }

        function login() {
            url = "./ClientAccountManagementController?target=SaveCartAndLogin";
            $.ajax({
                type: "GET",
                async: false,
                url: url,
                data: {},
                success: function (val) {
                    window.event.returnValue = false;
                    window.location.href = "#!/login";
                },
                error: function (xhr, status, error) {
                    document.getElementById("errMsg").style.display = "block";
                    document.getElementById('errMsg').innerHTML = error;
                    hideLoader();
                    ajaxResultsError(xhr, status, error);
                }
            });
        }


        function checkout() {
            //window.location.href = "./MusicManagementController?target=Checkout";
            url = "./MusicManagementController?target=Checkout";
            $.ajax({
                type: "GET",
                async: false,
                url: url,
                data: {},
                success: function (val) {
                    window.event.returnValue = false;
                    window.location.href = "#!/checkout";
                },
                error: function (xhr, status, error) {
                    document.getElementById("errMsg").style.display = "block";
                    document.getElementById('errMsg').innerHTML = error;
                    hideLoader();
                    ajaxResultsError(xhr, status, error);
                }
            });
            
        }

        function checkout2() {
            if (validateEmail()) {
                //window.location.href = "./MusicManagementController?target=Checkout";
                url = "./MusicManagementController?target=Checkout";
                $.ajax({
                type: "GET",
                async: false,
                url: url,
                data: {},
                success: function (val) {
                    window.event.returnValue = false;
                    window.location.href = "#!/checkout";
                },
                error: function (xhr, status, error) {
                    document.getElementById("errMsg").style.display = "block";
                    document.getElementById('errMsg').innerHTML = error;
                    hideLoader();
                    ajaxResultsError(xhr, status, error);
                }
            });
            }
        }


        function validateEmail() {
            var email = document.getElementById("email").value;
            var reemail = document.getElementById("reemail").value;
            var ok = true;
            if ((email != null && reemail != null) || (email != "" && reemail != "")) {
                if (email != reemail) {
                    //alert("Passwords Do not match");
                    document.getElementById("email").style.borderColor = "#E34234";
                    document.getElementById("reemail").style.borderColor = "#E34234";
                    alert("Email do not match. Please key again.");
                    ok = false;
                }
            } else {
                return ok;
            }
            return ok;
        }
    </script>
    <section class="content section">
        <div class="container">
            <article>
                <%@page import="EntityManager.ShoppingCart"%>
                <%@page import="EntityManager.Account"%>
                <%@page import="java.util.ArrayList"%>
                <%@page import="EntityManager.Album"%>
                <%@page import="java.util.Set"%>
                <%@page import="EntityManager.Music"%>
                <%@page import="java.util.List"%>
                <h1>Shopping Cart</h1>
                <%
                    ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("ShoppingCart");
                    if (shoppingCart != null) {
                        Set<Music> setOfMusics = shoppingCart.getListOfMusics();
                        if (setOfMusics != null && !setOfMusics.isEmpty()) {
                            List<Music> listOfMusics = new ArrayList();
                            listOfMusics.addAll(setOfMusics);
                %>
                <form name="cartManagement">
                    <h2>Tracks</h2>
                    <table class="layout display responsive-table">
                        <thead>
                            <tr>
                                <th class="product-remove" style="width: 5%">
                                    <input type="checkbox" onclick="checkAllTracks(this)" />
                                </th>     
                                <th style="width: 20%">Track Name</th>
                                <th style="width: 20%">Album Name</th>
                                <th style="width: 20%">Artist Name</th>
                                <th style="width: 10%">Price</th>
                                <th colspan="2" style="width: 15%"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <%                                for (Music m : listOfMusics) {
                            %>
                            <tr>
                                <td>
                                    <input type="checkbox" name="deleteTrack" value="music:<%=m.getId()%>" />
                                </td>
                                <td class="table-date">
                                    <%=m.getName()%>
                                </td>
                                <td class="table-name">
                                    <%=m.getAlbum().getName()%>
                                </td>
                                <td>
                                    <%=m.getArtistName()%>
                                </td>
                                <td>
                                    $<%=m.getPrice()%>0
                                </td>
                                <td class="actions">
                                    <a href="javascript:;" class="buy-tickets" title="Buy Tickets">Buy Tickets</a>
                                </td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                    <button class="medium invert" onclick="removeTrack()">Remove track(s)</button>
                    <hr class="divider2" style="margin-right: 0px;">
                    <%
                        }
                        Set<Album> setOfAlbums = shoppingCart.getListOfAlbums();
                        if (setOfAlbums != null && !setOfAlbums.isEmpty()) {
                            List<Album> listOfAlbums = new ArrayList();
                            listOfAlbums.addAll(setOfAlbums);
                    %>
                    <h2>Albums</h2>
                    <table class="layout display responsive-table">
                        <thead>
                            <tr>
                                <th class="product-remove" style="width: 5%">
                                    <input type="checkbox" onclick="checkAllAlbums(this)" />
                                </th>     
                                <th style="width: 20%">Album Name</th>
                                <th style="width: 20%">Artist Name</th>
                                <th style="width: 10%">Price</th>
                                <th colspan="2" style="width: 15%"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (Album a : listOfAlbums) {
                            %>
                            <tr>
                                <td>
                                    <input type="checkbox" name="deleteAlbum" value="album:<%=a.getId()%>" />
                                </td>
                                <td class="table-date">
                                    <%=a.getName()%>
                                </td>
                                <td>
                                    <%=a.getArtistName()%>
                                </td>
                                <td>
                                    $<%=a.getPrice()%>0
                                </td>
                                <td class="actions">
                                    <a href="javascript:;" class="buy-tickets" title="Buy Tickets">Buy Tickets</a>
                                </td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                    <button class="medium invert" onclick="removeAlbum()">Remove album(s)</button>
                    <hr class="divider2" style="margin-right: 0px;">
                </form>
                <%                                }
                %>
                <p style="float: right;"><strong>Subtotal: $99</strong> </p>
                <br><br>
                <button type="button" class="md-trigger medium invert" data-modal="checkout-confirm">Checkout</button>
                <%} else {%>
                <h2>The cart is empty.</h2>
                <%}%>
                <div class="md-modal md-effect-1" id="checkout-confirm">
                    <div class="md-content">
                        <h3>Checkout Cart</h3>
                        <%
                            Account account = (Account) session.getAttribute("account");
                            if (account == null) {
                        %>
                        <div>
                            <p>*Warning: You are not logged in. Your purchases will be provided to you as download links and they will expire within an hour. If you wish to re-download your purchases in the future, please login before checking out.</p>
                            <p>Are you sure you want to checkout?</p>
                            <div style="text-align:center;">
                                <button type="button" onclick="login()">Login</button>
                                <button type="button" class="md-trigger medium" data-modal="enter-email">Continue Checkout</button>
                                <button class="md-close" type="button">Close</button>
                            </div>
                        </div>
                        <%} else {%>
                        <div>
                            <p>Are you sure you want to checkout?</p>
                            <p>You will be directed to PayPal to complete your purchase.</p>
                            <div style="text-align:center;">
                                <button type="button" onclick="checkout()">Checkout</button>
                                <button class="md-close" type="button">Close</button>
                            </div>
                        </div>
                        <%}%>

                    </div>
                </div>
                <div class="md-modal md-effect-1" id="enter-email">
                    <div class="md-content">
                        <h3>Checkout Cart</h3>
                        <div>
                            <p>You will be directed to PayPal to complete your purchase.</p>
                            <p>Enter the email to receives the download link(s):</p>
                            <form>
                                Email:<input type="email" id="email" required/><br/><br/>
                                Re-enter email:<input type="email" id="reemail" required/>
                            </form><br/><br/>
                            <div style="text-align:center;">
                                <button type="button" onclick="checkout2()">Checkout</button>
                                <button class="md-close" type="button">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </article>
            <div class="md-overlay"></div>

            <script src="js/classie.js"></script>
            <script src="js/modalEffects.js"></script>
            <script>var polyfilter_scriptpath = '/js/';</script> 
            <script src="js/cssParser.js"></script>
            <script src="js/css-filters-polyfill.js"></script>
        </div>
    </section>
</section>
