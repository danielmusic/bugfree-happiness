<section id="page" data-title="Account Email Verification">
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <h1 class="heading-l">Account Email Verification</h1>
        <span class="overlay dots"></span>
    </section>
    <section class="content section">
        <div class="container">
            <%@page import="EntityManager.Account"%>
            <%
                Account account = (Account) session.getAttribute("account");
                if (account != null) {
            %>


            <article>
                <form class="form" name="resetPasswordForm" action="ClientAccountManagementController">
                    <jsp:include page="jspIncludePages/displayMessage.jsp" />
                    <div class="row clearfix">
                        <div class="col-1-3">
                            <label for="email"><strong>Current Email</strong></label>
                            <input type="email" name="email" id="email" value="<%=account.getEmail()%>" required disabled>
                        </div>
                        <div class="col-1-3">
                            <label for="email"><strong>Verification Code</strong></label>
                            <input name="verifyEmailCode" id="verifyEmailCode" required>
                            <%if (!account.getEmailIsVerified()) {%>
                            <a href="ClientAccountManagementController?target=SendEmailVerification">Resend Verification Code</a>
                            <%}%>
                        </div>
                        <div class="col-1-3 last">
                            <label for="email"><strong>Verification Status</strong></label>
                            <%if (account.getEmailIsVerified()) {%>
                            Verified
                            <%} else {%>
                            Unverified
                            <%}%>
                        </div>
                    </div>
                    <input type="hidden" value="VerifyEmail" name="target">
                    <%if (account.getEmailIsVerified()!= true) {%>
                    <button type="submit" class="medium invert">Submit Code</button>
                    <%} else {%>
                    <button type="button" class="medium invert" onclick="window.location.href = ''">Back to Home</button>
                    <%}%>
                    <div class="clear"></div>
                </form>
            </article>
            <%} else {%>
            <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
            <%}%>
        </div>
    </section>
</section>

