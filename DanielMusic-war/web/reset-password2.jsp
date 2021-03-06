<section id="page" data-title="sounds.sg | password reset">
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <h1 class="heading-l">Password Reset</h1>
        <span class="overlay dots"></span>
    </section>
    <section class="content section">
        <div class="container" style="width: 50%;">
            <%
                String email = (String) session.getAttribute("resetPasswordEmail");
            %>
            <article>
                <form class="form" name="resetPasswordForm" action="ClientAccountManagementController">
                    <jsp:include page="jspIncludePages/displayMessage.jsp" />
                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="email"><strong>Email</strong> *</label>
                            <input type="email" name="email" id="email" value="<%=email%>" required>
                        </div>
                        <div class="col-1-2 last">
                            <label for="email"><strong>Verification Code</strong> *</label>
                            <input name="resetPasswordCode" id="resetPasswordCode" required>
                        </div>
                    </div>
                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="password"><strong>New Password</strong> *</label>
                            <input id="password" type="password" title="Password should contain at least 6 characters"  name="password" id="password" required onchange="form.repassword.pattern = this.value;">
                        </div>
                        <div class="col-1-2 last">
                            <label for="repassword"><strong>Re-enter Password</strong> *</label>
                            <input id="repassword" type="password" name="repassword" required>
                        </div>
                    </div>
                    <input type="hidden" value="VerifyResetCode" name="target">
                    <button type="submit" class="medium invert">Reset Password</button>
                    <div class="clear"></div>
                </form>
            </article>
        </div>
    </section>
</section>

