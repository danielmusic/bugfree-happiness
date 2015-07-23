<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Profile Page">
    <%@page import="EntityManager.Genre"%>
    <%@page import="java.util.List"%>
    <%@page import="EntityManager.Member"%>
    <%@page import="EntityManager.Account"%>
    <%@page import="java.text.SimpleDateFormat"%>
    <section class="content section">
        <div class="container">
            <article>
                <%
                    List<Genre> genres = (List<Genre>) (session.getAttribute("listOfGenres"));
                    Account account = (Account) session.getAttribute("account");
                    if (account != null) {
                        if (!account.getEmailIsVerified()) {
                            out.print("<p class='warning'>Your email address has not been verified. Click here to <a href='#!/verify-email'>resend verification code</a>.</p>");
                        }
                %>

                <p>Transaction history write here</p>
           

                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>
        </div>
    </section>
</section>
