<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Profile Page">
    <%@page import="java.util.List"%>
    <%@page import="EntityManager.Account"%>
    <%@page import="java.text.SimpleDateFormat"%>
    <%@page import="EntityManager.Music"%>
    <%@page import="java.text.NumberFormat"%>
    <script>
        function generateDownloadLink(musicID, bitrateType) {
            window.open("./MusicManagementController?target=GenerateDownloadLink&musicID=" + musicID + "&bitrateType=" + bitrateType);
        }
    </script>

    <section class="content section">
        <div class="container">
            <article>
                <%
                    Account account = (Account) session.getAttribute("account");
                    List<Music> listOfMusics = (List<Music>) session.getAttribute("ListOfPurchasedMusics");

                    if (account != null) {
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
