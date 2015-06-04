<section id="page" data-title="Account Login">
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <h1 class="heading-l">Account Login</h1>
        <span class="overlay dots"></span>
    </section>
    <section class="content section">
        <div class="container">
            <article>
                <jsp:include page="displayMessage.jsp" />

                <form action="ClientAccountManagementController" class="form">
                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="email"><strong>Email</strong> *</label>
                            <input type="email" name="email" id="email" required>
                        </div>
                    </div>
                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="password"><strong>Password</strong> *</label>
                            <input id="password" type="password" name="pwd" required>
                            <a href="">Forget password?</a>
                        </div>
                    </div>

                    <input type="submit" value="Login" class="large invert">
                    <input type="hidden" value="ArtistLogin" name="target">
                    <div class="clear"></div>
                </form>
            </article>
        </div>
    </section>
</section>
