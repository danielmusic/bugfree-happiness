<section id="page" data-title="sounds.sg | password reset">
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <h1 class="heading-l">Password Reset</h1>
        <span class="overlay dots"></span>
    </section>
    <section class="content section">
        <div class="container" style="width: 50%;">
            <article>
                <form class="form" name="resetPasswordForm" action="ClientAccountManagementController">
                    <jsp:include page="jspIncludePages/displayMessage.jsp" />
                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="email"><strong>Email</strong> *</label>
                            <input type="email" name="email" id="email" required>
                        </div>
                    </div>
                    <input type="hidden" value="SendResetPasswordEmail" name="target">
                    <button type="submit" class="medium invert">Reset Password</button>
                    <div class="clear"></div>
                </form>
            </article>
        </div>
    </section>
</section>

