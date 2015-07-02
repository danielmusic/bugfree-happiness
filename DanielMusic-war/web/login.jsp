<section id="page" data-title="Account Login">

    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <h1 class="heading-l">Account Login</h1>
        <span class="overlay dots"></span>
    </section>
    <section class="content section">
        <div class="container">
            <article>
                <form class="form" name="loginForm" action="ClientAccountManagementController">
                    <jsp:include page="jspIncludePages/displayMessage.jsp" />
                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="email"><strong>Email</strong> *</label>
                            <input type="email" name="email" id="email" required>
                        </div>
                    </div>
                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="password"><strong>Password</strong> *</label>
                            <input type="password" name="password" id="password" required>
                            <a href="#!/reset-password">Forget password?</a>
                        </div>
                    </div>
                    <input type="hidden" value="AccountLogin" name="target">
                    <button type="submit" class="medium invert">Login</button>
                    <div class="clear"></div>
                </form>
            </article>
        </div>
    </section>
</section>

