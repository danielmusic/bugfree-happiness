<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Shopping Cart">
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/events-bg.jpg)">
        <h1 class="heading-l">Cart <span class="color">Checkout</span></h1>
    </section>
    <script>
        //JS to skip this page when no payment is required
        //or redirect back to cart if checkoutHelper.getPayKey() is null
        function redirectToDownload1() {
            //todo copy from dummy
        }
        function redirectToDownload2() {
            //todo copy from dummy
        }
    </script>
    <section class="content section">
        <div class="container">
            <article>
                <%@page import="EntityManager.CheckoutHelper"%>
                <%@page import="EntityManager.Account"%>
                <%@page import="java.util.ArrayList"%>
                <%@page import="EntityManager.Album"%>
                <%@page import="java.util.Set"%>
                <%@page import="EntityManager.Music"%>
                <%@page import="EntityManager.Payment"%>
                <%@page import="java.util.List"%>
                <%@page import="java.text.NumberFormat"%>
                <h1>Shopping Cart</h1>
                <%
                    CheckoutHelper checkoutHelper = (CheckoutHelper) session.getAttribute("checkoutHelper");
                    if (checkoutHelper != null) {
                        Payment payment = checkoutHelper.getPayment();
                        List<Music> listOfMusics = payment.getMusicPurchased();
                        List<Double> musicPrices = payment.getMusicPrices();
                        if (listOfMusics != null && !listOfMusics.isEmpty()) {
                %>
                <form name="cartManagement">
                    <h2>Tracks</h2>
                    <table class="layout display responsive-table">
                        <thead>
                            <tr>    
                                <th style="width: 30%">Track Name</th>
                                <th style="width: 30%">Album Name</th>
                                <th style="width: 30%">Artist Name</th>
                                <th style="width: 10%">Price</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int i = 0; i < listOfMusics.size(); i++) {
                                    Music m = listOfMusics.get(i);
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
                                    <%NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                        out.print(formatter.format(musicPrices.get(i)));%>
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
                        List<Album> listOfAlbums = payment.getAlbumPurchased();
                        List<Double> albumPrices = payment.getAlbumPrices();
                        if (listOfAlbums != null && !listOfAlbums.isEmpty()) {
                    %>
                    <h2>Albums</h2>
                    <table class="layout display responsive-table">
                        <thead>
                            <tr>  
                                <th style="width: 30%">Album Name</th>
                                <th style="width: 30%">Artist Name</th>
                                <th style="width: 30%">Price</th>
                                <th style="width: 10%">Price</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int i = 0; i < listOfAlbums.size(); i++) {
                                    Album a = listOfAlbums.get(i);
                            %>
                            <tr>
                                <td class="table-date">
                                    <%=a.getName()%>
                                </td>
                                <td>
                                    <%=a.getArtistName()%>
                                </td>
                                <td>
                                    <%NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                        out.print(formatter.format(albumPrices.get(i)));%>
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
                    %>
                </form>
                <p style="float: right;">
                    <strong>Subtotal: <%=payment.getTotalPaymentAmount()%></strong> 
                    <br/><br/>

                </p>
                <form action="https://www.sandbox.paypal.com/webapps/adaptivepayment/flow/pay" target="PPDGFrame" class="standard">
                    <input type="image" id="submitBtn" value="Pay with PayPal" src="https://www.paypalobjects.com/en_US/i/btn/btn_paynowCC_LG.gif">
                    <input id="type" type="hidden" name="expType" value="light">
                    <input id="paykey" type="hidden" name="paykey" value="<%=checkoutHelper.getPayKey()%>">
                </form>
                <script type="text/javascript" charset="utf-8">
                    var embeddedPPFlow = new PAYPAL.apps.DGFlow({trigger: 'submitBtn'});
                </script>

                <%} else {%>
                <h2>The checkout list is empty.</h2>
                <%}%>
            </article>
        </div>
    </section>
</section>
