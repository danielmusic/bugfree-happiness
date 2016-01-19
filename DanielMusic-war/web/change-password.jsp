<section id="page" data-title="sounds.sg | change password">
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <h1 class="heading-l">change account password</h1>
        <span class="overlay dots"></span>
    </section>
    <section class="content section">
        <div class="container" style="width: 440px;">
            <%@page import="EntityManager.Account"%>
            <%
                Account account = (Account) session.getAttribute("account");
                if (account != null) {
            %>
            <article>
                <form class="form" name="resetPasswordForm" action="ClientAccountManagementController">
                    <jsp:include page="jspIncludePages/displayMessage.jsp" />
                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="oldpassword"><strong>Old Password</strong> *</label>
                            <input id="oldpassword" type="password" name="oldpassword" required>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="password"><strong>New Password</strong> *</label>
                            <input id="password" type="password" name="password" title="Password should contain at least 6 characters" onchange="form.repassword.pattern = this.value;" required>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="repassword"><strong>Re-enter Password</strong> *</label>
                            <input id="repassword" type="password" name="repassword" required>
                        </div>
                    </div>

                    <input type="hidden" value="ChangePassword" name="target">
                    <button type="submit" class="medium invert" style="width: 325px; margin-right: 10px;">Change Password</button>
                    <button type="button" class="medium invert" style="width: 100px; margin-right: 0px;" onclick="window.location.href = '#!/artist/profile'">Back</button>
                    <div class="clear"></div>
                </form>
            </article>
            <%} else {%>
            <p class="warning" id="errMsg">Your Session has timed out. <a href="#!/login">Click here to login again.</a></p>
            <%}%>
        </div>
    </section>
</section>

