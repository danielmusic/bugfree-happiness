<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="sounds.sg | shopping cart">
    <%@page import="EntityManager.ShoppingCart"%>
    <%@page import="EntityManager.Account"%>
    <%@page import="java.util.ArrayList"%>
    <%@page import="EntityManager.Album"%>
    <%@page import="java.util.Set"%>
    <%@page import="EntityManager.Music"%>
    <%@page import="java.util.List"%>
    <%@page import="java.text.NumberFormat"%>
    <%@page import="java.text.DecimalFormat"%>
    <%@page import="java.text.DecimalFormatSymbols"%>
    <style>
        input, select {
            width: 100%;
        }
    </style>
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
            checkboxes = document.getElementsByName('deleteTrack');
            var arr = new Array();
            $("input:checkbox[name=deleteTrack]:checked").each(function () {
                arr.push($(this).val());
            });
            var stringArr = "";
            for (var i = 0; i < arr.length; i++) {
                if (i != (arr.length - 1)) {
                    stringArr += arr[i] + " ";
                } else {
                    stringArr += arr[i];
                }
            }
            var numOfTicks = 0;
            for (var i = 0, n = checkboxes.length; i < n; i++) {
                if (checkboxes[i].checked) {
                    numOfTicks++;
                }
            }
            if (checkboxes.length == 0 || numOfTicks == 0) {
                //window.location.href = "./MusicManagementController?target=RemoveTrackFromShoppingCart";
            } else {
                url = "./MusicManagementController?target=RemoveTrackFromShoppingCart";
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    data: {'deleteTrack': stringArr},
                    success: function () {
                        window.location.href = "/#!/redirect";
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
            var arr = new Array();
            $("input:checkbox[name=deleteAlbum]:checked").each(function () {
                arr.push($(this).val());
            });
            var stringArr = "";
            for (var i = 0; i < arr.length; i++) {
                if (i != (arr.length - 1)) {
                    stringArr += arr[i] + ",";
                } else {
                    stringArr += arr[i];
                }
            }

            var numOfTicks = 0;
            for (var i = 0, n = checkboxes.length; i < n; i++) {
                if (checkboxes[i].checked) {
                    numOfTicks++;
                }
            }
            if (checkboxes.length == 0 || numOfTicks == 0) {
                //window.location.href = "./MusicManagementController?target=RemoveAlbumFromShoppingCart";
            } else {
                url = "./MusicManagementController?target=RemoveAlbumFromShoppingCart";
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    data: {'deleteAlbum': stringArr},
                    success: function (val) {
                        window.location.href = "#!/redirect";
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
                success: function () {
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
            this.disabled = true;
            //window.location.href = "./MusicManagementController?target=Checkout";
            url = "./MusicManagementController?target=Checkout";
            $.ajax({
                type: "GET",
                async: false,
                url: url,
                data: {},
                success: function () {
                    window.location.href = "#!/redirect";
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
                email = $('#email').val();
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    data: {'email': email},
                    success: function () {
                        window.location.href = "#!/redirect";
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

        function validateValidEmail(email) {
            var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            return re.test(email);
        }

        function validateEmail() {
            var email = document.getElementById("email").value;
            var reemail = document.getElementById("reemail").value;
            var ok = true;

            if (email == null || reemail == null) {
                document.getElementById("email").style.borderColor = "#E34234";
                document.getElementById("reemail").style.borderColor = "#E34234";
                alert("Please fill in your email.");
                return false;
            }

            if (email == "" || reemail == "") {
                document.getElementById("email").style.borderColor = "#E34234";
                document.getElementById("reemail").style.borderColor = "#E34234";
                alert("Please fill in your email.");
                return false;
            }

            if (!validateValidEmail(email) || !validateValidEmail(reemail)) {
                document.getElementById("email").style.borderColor = "#E34234";
                document.getElementById("reemail").style.borderColor = "#E34234";
                alert("Please enter a valid email.");
                return false;
            }

            if ((email != null && reemail != null) || (email != "" && reemail != "")) {
                if (email != reemail) {
                    document.getElementById("email").style.borderColor = "#E34234";
                    document.getElementById("reemail").style.borderColor = "#E34234";
                    alert("Email do not match. Please key again.");
                    ok = false;
                }
            }
            return ok;
        }
    </script>
    <section class="content section">
        <div class="container">
            <article>
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
                                <button type="button" class="md-trigger" data-modal="enter-email">Continue Checkout</button>
                                <button type="button" class="md-close">Close</button>
                            </div>
                        </div>
                        <%} else {%>
                        <div>
                            <p>Are you sure you want to checkout?</p>
                            <p>You will be directed to PayPal to complete your purchase.</p>
                            <div style="text-align:center;">
                                <button type="button" onclick="checkout();">Checkout</button>
                                <button type="button" class="md-close">Close</button>
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
                            <br>
                            <form class="form">
                                <div class="row clearfix">
                                    <div class="col-1-1">
                                        Email: <input type="email" id="email" name="email" required/>
                                    </div>
                                </div>
                                <div class="row clearfix">
                                    <div class="col-1-1">
                                        Re-enter email: <input type="email" id="reemail" required/>
                                    </div>
                                </div>
                            </form>
                            <div style="text-align:center;">
                                <button type="button" onclick="checkout2()">Checkout</button>
                                <button class="md-close" type="button">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
                <jsp:include page="./jspIncludePages/displayMessage.jsp" />
                <p class="error" id="errMsg" style="display:none;"></p>
                <h1>Shopping Cart</h1>
                <%
                    ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("ShoppingCart");
                    Double totalPrice = 0.0;
                    if (shoppingCart != null && shoppingCart.getListOfAlbums().size() + shoppingCart.getListOfMusics().size() != 0) {
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
                                <th>
                                    <input type="checkbox" onclick="checkAllTracks(this);" />
                                </th>     
                                <th>Track</th>
                                <th>Album</th>
                                <th>Artist</th>
                                <th>SGD</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (Music m : listOfMusics) {
                                    totalPrice = totalPrice + m.getPrice();
                            %>
                            <tr>
                                <td>
                                    <input type="checkbox" name="deleteTrack" value="<%=m.getId()%>" />
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
                                    <%
                                        NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
                                        decimalFormatSymbols.setCurrencySymbol("");
                                        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);
                                        out.print(formatter.format(m.getPrice()));
                                    %>
                                </td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                    <button type="button" class="medium invert" onclick="removeTrack()">Remove selected track(s)</button>
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
                                <th style="width: 30%">Album</th>
                                <th style="width: 30%">Artist</th>
                                <th style="width: 35%">SGD</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (Album a : listOfAlbums) {
                                    totalPrice = totalPrice + a.getPrice();
                            %>
                            <tr>
                                <td>
                                    <input type="checkbox" name="deleteAlbum" value="<%=a.getId()%>" />
                                </td>
                                <td class="table-date">
                                    <%=a.getName()%>
                                </td>
                                <td>
                                    <%=a.getArtistName()%>
                                </td>
                                <td>
                                    <%
                                        NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
                                        decimalFormatSymbols.setCurrencySymbol("");
                                        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);
                                        out.print(formatter.format(a.getPrice()));
                                    %>
                                </td>
                            </tr>
                            <%}%>
                        </tbody>
                    </table>
                    <button class="medium invert" onclick="removeAlbum()">Remove selected album(s)</button>
                    <hr class="divider2" style="margin-right: 0px;">
                    <%}%>
                </form>
                <p>
                    <button type="button" class="medium invert" onclick="javascript:window.history.back();" style="margin-right: 10px;">Back</button>
                    <button type="button" class="md-trigger medium invert" data-modal="checkout-confirm">Checkout</button>
                    <strong style="float: right;">
                        Total:                                    
                        <%
                            NumberFormat formatter = NumberFormat.getCurrencyInstance();
                            DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
                            decimalFormatSymbols.setCurrencySymbol("");
                            ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);
                            out.print(formatter.format(totalPrice));
                        %>
                    </strong>
                </p>                
                <%} else {%>
                <h2>The cart is empty.</h2>
                <%}%>
                <div class="md-overlay"></div>
                <script src="js/classie.js"></script>
                <script src="js/modalEffects.js"></script>
                <script src="js/cssParser.js"></script>
            </article>
        </div>
    </section>
</section>
<script type="text/javascript">
                        var _gaq = _gaq || [];
                        _gaq.push(['_setAccount', 'UA-66150326-1']);
                        var d = document.location.pathname + document.location.search + document.location.hash;
                        _gaq.push(['_trackPageview', d]);

                        (function () {
                            var ga = document.createElement('script');
                            ga.type = 'text/javascript';
                            ga.async = true;
                            ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                            var s = document.getElementsByTagName('script')[0];
                            s.parentNode.insertBefore(ga, s);
                        })();
</script>