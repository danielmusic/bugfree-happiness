<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Profile Page">
    <%@page import="EntityManager.Member"%>
    <%@page import="java.util.List"%>
    <%@page import="EntityManager.Account"%>
    <%@page import="java.text.SimpleDateFormat"%>
    <%@page import="EntityManager.Music"%>
    <%@page import="java.text.NumberFormat"%>
    <script>
        function generateDownloadLink(musicID, bitrateType) {
            window.open("./MusicManagementController?target=GenerateDownloadLink&musicID=" + musicID + "&bitrateType=" + bitrateType);
//            url = "./MusicManagementController?target=GenerateDownloadLink";
//            $.ajax({
//                type: "POST",
//                async: false,
//                url: url,
//                data: {'musicID': musicID, 'bitrateType': bitrateType},
//                success: function (val) {
//                    window.event.returnValue = false;
//                    alert("success!");
//                    //window.location.href = "#!/redirect";
//                    var json = JSON.parse(val);
//                    alert(json.downloadLink);
//                    window.open(json.downloadLink);
//                },
//                error: function (xhr, status, error) {
//                    alert("error!");
//                    document.getElementById("errMsg").style.display = "block";
//                    document.getElementById('errMsg').innerHTML = error;
//                    hideLoader();
//                    ajaxResultsError(xhr, status, error);
//                }
//            });
        }
    </script>

    <section class="content section">
        <div class="container">
            <article>
                <div class="md-modal md-effect-1" id="modal-name">
                    <div class="md-content">
                        <h3>Modal Dialog</h3>
                        <div>
                            <p>This is a modal window. You can do the following things with it:</p>
                            <ul>
                                <li><strong>Read:</strong> modal windows will probably tell you something important so don't forget to read what they say.</li>
                                <li><strong>Look:</strong> a modal window enjoys a certain kind of attention; just look at it and appreciate its presence.</li>
                                <li><strong>Close:</strong> click on the button below to close the modal.</li>
                            </ul>
                            <div style="text-align:center;">
                                <button class="md-close" type="button">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="md-modal md-effect-1" id="modal-change-email">
                    <div class="md-content">
                        <h3>Account Email</h3>
                        <div>
                            <p>This is the email address that you will use to:</p>
                            <ul>
                                <li><strong>Login:</strong> into Sounds.SG</li>
                                <li><strong>Reset:</strong> your password if the need arises</li>
                                <li><strong>Receive:</strong> invoices about your purchases on Sounds.SG</li>
                            </ul>
                            <p>Update this field only if you are changing your email. A verification code will be sent to the new email.</p>
                            <div style="text-align:center;">
                                <button class="md-close" type="button">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="md-modal md-effect-1" id="modal-paypal">
                    <div class="md-content">
                        <h3>Payment Details</h3>
                        <div>
                            <p>When a fan buys your music, the money will go directly to the above address. Please also be sure to follow the instructions on the Sell Your Music on ?? page!:</p>
                            <div style="text-align:center;">
                                <button class="md-close" type="button">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <%
                    Account account = (Account) session.getAttribute("account");
                    Member member = (Member) (session.getAttribute("member"));
                    List<Music> listOfMusics = (List<Music>) session.getAttribute("ListOfPurchasedMusics");

                    if (account != null && member != null) {
                        if (!account.getEmailIsVerified()) {
                            out.print("<p class='warning'>Your email address has not been verified. Click here to <a href='#!/verify-email'>enter or resend your verification code</a>.</p>");
                        }
                %>

                <form action="ClientAccountManagementController" class="form" method="POST" enctype="multipart/form-data">
                    <jsp:include page="../jspIncludePages/displayMessage.jsp" />
                    <h2>My Past Purchases</h2>
                    <hr class="divider">
                    <form name="cartManagement">
                        <h2>Tracks</h2>
                        <table class="layout display responsive-table">
                            <thead>
                                <tr>
                                    <th style="width: 30%">Track Name</th>
                                    <th style="width: 30%">Album Name</th>
                                    <th style="width: 25%">Artist Name</th>
                                    <th style="width: 10%">Price</th>
                                    <th>128kbps</th>
                                    <th>320kbps</th>
                                    <th>wav</th>
                                </tr>
                            </thead>
                            <tbody>

                                <%
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
                                        <%NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                            out.print(formatter.format(m.getPrice()));%>
                                    </td>
                                    <td>
                                        <span class="icon icon-download"  style="cursor: pointer;" onclick="generateDownloadLink(<%=m.getId()%>, '128')"></span>  
                                    </td>
                                    <td>
                                        <span class="icon icon-download"  style="cursor: pointer;" onclick="generateDownloadLink(<%=m.getId()%>, '320')"></span>  
                                    </td>
                                    <td>
                                        <span class="icon icon-download"  style="cursor: pointer;" onclick="generateDownloadLink(<%=m.getId()%>, 'wav')"></span>  
                                    </td>
                                </tr>
                                <%}%>

                            </tbody>
                        </table>
                        <hr class="divider2" style="margin-right: 0px;">

                        <!--                        <h2>Albums</h2>
                                                <table class="layout display responsive-table">
                                                    <thead>
                                                        <tr>
                                                            <th class="product-remove" style="width: 5%">
                                                                <input type="checkbox" onclick="checkAllAlbums(this)" />
                                                            </th>     
                                                            <th style="width: 30%">Album Name</th>
                                                            <th style="width: 30%">Artist Name</th>
                                                            <th style="width: 35%">Price</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="deleteAlbum" value="" />
                                                            </td>
                                                            <td class="table-date">
                                                            </td>
                                                            <td>
                                                                <span class="icon icon-download"style="cursor: pointer;" onclick=""></span>  
                                                            </td>
                                                            <td>
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                                <button class="medium invert" onclick="">Remove album(s)</button>
                                                <hr class="divider2" style="margin-right: 0px;">-->
                    </form>

                    <%} else {%>
                    <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                    <%}%>
                    <div class="md-overlay"></div><!-- the overlay element -->
                    <script src="js/classie.js"></script>
                    <script src="js/modalEffects.js"></script>
                    <script src="js/cssParser.js"></script>
            </article>
        </div>
    </section>
</section>
