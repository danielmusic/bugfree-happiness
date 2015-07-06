<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Shopping Cart">
<script>
                function loadAjax(id) {
                    exploreForm.id.value = id;
                    url = "./MusicController?target=GetArtistByID";
                    $.ajax({
                        type: "GET",
                        async: false,
                        url: url,
                        data: {'id': id},
                        dataType: "text",
                        success: function (val) {
                            window.event.returnValue = true;
                            var json = JSON.parse(val);
                            if (json.result) {
                                window.event.returnValue = false;
                                window.location.href = "#!/artists";
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
            </script>
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/events-bg.jpg)">
        <h1 class="heading-l">Upcoming <span class="color">Events</span></h1>
    </section>
    <script>
    </script>
    <section class="content section">
        <div class="container">
            <article>

                <form name="albumManagement">

                    <h2>Shopping Cart</h2>
                    <table class="layout display responsive-table">
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Description</th>
                                <th>Quantity</th>
                                <th>Price</th>
                                <th colspan="2"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td class="table-date">
                                    One night
                                </td>
                                <td class="table-name">
                                    romantic very very
                                </td>
                                <td>
                                    2
                                </td>
                                <td>
                                    $88.00
                                </td>
                                <td class="actions">
                                    <a href="javascript:;" class="buy-tickets" title="Buy Tickets">Buy Tickets</a>
                                </td>
                            </tr>

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
