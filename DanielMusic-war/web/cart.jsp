<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Shopping Cart">
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/events-bg.jpg)">
        <h1 class="heading-l">Upcoming <span class="color">Events</span></h1>
    </section>
    <script>
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
                <%
                    Account account = (Account) session.getAttribute("account");
                    if (account != null) {
                %>
                <input type="hidden" name="accountID" id="accountID" value="<%=account.getId()%>" />
                <script>
                    $(function () {
                        accountID = $("accountID").val();
                        url = "./MusicManagementController?target=GetShoppingCart";
                        $.ajax({
                            type: "GET",
                            async: false,
                            url: url,
                            data: {'id': accountID},
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
                    });
                </script>
                <%}%>
                <h1>Shopping Cart</h1>
                <%
                    ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("ShoppingCart");
                    if (shoppingCart != null) {

                        Set<Music> setOfMusics = shoppingCart.getListOfMusics();
                        if (setOfMusics != null) {
                            List<Music> listOfMusics = new ArrayList();
                            listOfMusics.addAll(setOfMusics);

                %>
                <form name="trackManagement">
                    <h2>Tracks</h2>
                    <table class="layout display responsive-table">
                        <thead>
                            <tr>
                                <th class="product-remove" style="width: 5%">
                                    <input type="checkbox" onclick="checkAll(this)" />
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
                                    <input type="checkbox" name="delete" value="music:<%=m.getId()%>" />
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
                            <!--tr>
                                <td>
                                    <input type="checkbox" name="delete" value="music123" />
                                </td>
                                <td class="table-name">
                                    Herarts
                                </td>
                                <td class="table-name">
                                    Legend of the tales
                                </td>
                                <td>
                                    Lee Siao Long
                                </td>
                                <td>
                                    $5.00
                                </td>
                                <td class="actions">
                                    <a href="javascript:;" class="buy-tickets" title="Buy Tickets">Buy Tickets</a>
                                </td>
                            </tr-->
                        </tbody>
                    </table>
                    <hr class="divider2" style="margin-right: 0px;">
                </form>
                <%
                    }

                    Set<Album> setOfAlbums = shoppingCart.getListOfAlbums();
                    if (setOfAlbums != null) {
                        List<Album> listOfAlbums = new ArrayList();
                        listOfAlbums.addAll(setOfAlbums);
                %>
                <form name="albumManagement">
                    <h2>Albums</h2>
                    <table class="layout display responsive-table">
                        <thead>
                            <tr>
                                <th class="product-remove" style="width: 5%">
                                    <input type="checkbox" onclick="checkAll(this)" />
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
                                    <input type="checkbox" name="delete" value="album:<%=a.getId()%>" />
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
                            <!--tr>
                                <td>
                                    <input type="checkbox" name="delete" value="music123" />
                                </td>
                                <td class="table-date">
                                    Legend of the tales
                                </td>
                                <td>
                                    Lee Siao Long
                                </td>
                                <td>
                                    $5.00
                                </td>
                                <td class="actions">
                                    <a href="javascript:;" class="buy-tickets" title="Buy Tickets">Buy Tickets</a>
                                </td>
                            </tr-->
                        </tbody>
                    </table>
                    <hr class="divider2" style="margin-right: 0px;">
                </form>
                <%                                }
                %>
                <p style="float: right;"><strong>Subtotal: $99</strong> </p>
                <br><br>
                <button type="submit" class="medium invert" style="float: right;">Next</button>
                <%}%>
                <h2>The cart is empty.</h2>
            </article>
        </div>
    </section>
</section>
