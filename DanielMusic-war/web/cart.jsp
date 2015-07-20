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

        function checkout() {
            window.location.href = "./MusicManagementController?target=Checkout";
            //window.open("./MusicManagementController?target=Checkout","_blank","width=400, height=600");
            //window.open("./MusicManagementController?target=Checkout","_blank");
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
                        if (setOfMusics != null) {
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
                    <hr class="divider2" style="margin-right: 0px;">
                    <%
                        }
                        Set<Album> setOfAlbums = shoppingCart.getListOfAlbums();
                        if (setOfAlbums != null) {
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
                        <div>
                            <p>This is a modal window. You can do the following things with it:</p>
                            <ul>
                                <li><strong>Read:</strong> modal windows will probably tell you something important so don't forget to read what they say.</li>
                                <li><strong>Look:</strong> a modal window enjoys a certain kind of attention; just look at it and appreciate its presence.</li>
                                <li><strong>Close:</strong> click on the button below to close the modal.</li>
                            </ul>
                            <div style="text-align:center;">
                                <button class="md-close" type="button" onclick="checkout()">Checkout</button>
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
