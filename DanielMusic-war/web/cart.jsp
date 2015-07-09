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
                            dataType: "text",
                            success: function (val) {
                                window.event.returnValue = true;
                                var json = JSON.parse(val);
                                if (json.result) {
                                    window.event.returnValue = false;
                                    window.location.href = "#!/cart";
                                }
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
                <%
                    }
                    ShoppingCart cart = (ShoppingCart) session.getAttribute("ShoppingCart");
                %>
                <form name="albumManagement">
                    <h2>Shopping Cart</h2>
                    <table class="layout display responsive-table">
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Album Name</th>
                                <th>Artist Name</th>
                                <th>Price</th>
                                <th>Type</th>
                                <th colspan="2"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (cart != null) {
                                    Set<Album> setOfAlbums = cart.getListOfAlbums();
                                    List<Album> listOfAlbums = new ArrayList();
                                    listOfAlbums.addAll(setOfAlbums);

                                    Set<Music> setOfMusics = cart.getListOfMusics();
                                    List<Music> listOfMusics = new ArrayList();
                                    listOfMusics.addAll(setOfMusics);
                                    
                                    for (Album a : listOfAlbums) {
                            %>
                            <tr>
                                <td class="table-date">
                                    <%=a.getName()%>
                                </td>
                                <td class="table-name">
                                    <%=a.getDescription()%>
                                </td>
                                <td>
                                    <%=a.getArtistName()%>
                                </td>
                                <td>
                                    $<%=a.getPrice()%>0
                                </td>
                                <td>
                                    Album
                                </td>
                                <td class="actions">
                                    <a href="javascript:;" class="buy-tickets" title="Buy Tickets">Buy Tickets</a>
                                </td>
                            </tr>
                            <%                                }
                                for (Music m : listOfMusics) {
                            %>
                            <tr>
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
                                <td>
                                    Track
                                </td>
                                <td class="actions">
                                    <a href="javascript:;" class="buy-tickets" title="Buy Tickets">Buy Tickets</a>
                                </td>
                            </tr>
                            <%                                }
                            } else {
                            %>
                            <tr>
                                <td class="table-date">
                                    Flower hearts
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
                                <td>
                                    Track
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
                    <p style="float: right;"><strong>Subtotal: $99</strong> </p>
                    <br><br>
                    <button type="submit" class="medium invert" style="float: right;">Next</button>
                </form>
            </article>
        </div>
    </section>
</section>
