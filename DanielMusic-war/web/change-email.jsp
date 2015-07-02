<section id="page" data-title="Change Account Email">
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <h1 class="heading-l">Change Account Email</h1>
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
                <script>
                    function sendCurrEmailVerification() {
                        //var customerID = document.getElementById("customerList").value;
                        window.location.href = "ClientAccountManagementController?target=SendEmailVerification";
                    }
                </script>
                <form class="form" name="resetPasswordForm" action="ClientAccountManagementController">
                    <jsp:include page="jspIncludePages/displayMessage.jsp" />
                    <div class="row clearfix">
                        <div class="col-1-3">
                            <label for="email"><strong>New Email</strong>*</label>
                            <input type="email" name="email" id="email" value="<%=account.getNewEmail()%>" required disabled>
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
                            <%if (account.getNewEmailIsVerified()) {%>
                            
                            <%} else {%>
                            New Email Unverified
                            <%}%>
                        </div>
                    </div>
                    <input type="hidden" value="ChangeEmail" name="target">
                    <button type="submit" class="medium invert">Change Email / Submit Code</button>
                    <div class="clear"></div>
                </form>
            </article>
            <%} else {%>
            <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
            <%}%>
        </div>
    </section>
</section>

