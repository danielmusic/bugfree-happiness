<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Artist Signup">
    <!-- ############################# Intro ############################# -->
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <h1 class="heading-l">Artist Signup</h1>
        <h2 class="heading-m">It's now or <span class="color">Never</span></h2>
        <!-- Overlay -->
        <span class="overlay dots"></span>
    </section>
    <!-- /intro -->

    <!-- ############################# Content ############################# -->
    <section class="content section">
        <!-- container -->
        <div class="container">
            <!-- Article -->
            <article>
                <script>
                    function signup() {
                        var v = grecaptcha.getResponse();
                        if (v.length != 0) {
                            window.event.returnValue = true;
                            document.artistSignupForm.action = "ClientAccountManagementController";
                            document.artistSignupForm.submit();
                        } else {
                            document.getElementById('displayMsg').innerHTML = "<p class='error'>You can't leave Captcha Code empty</p>";
                        }
                    }
                </script>

                <span id="displayMsg"></span>
                <jsp:include page="../displayMessage.jsp" />

                <h2>Sign up for an artist account</h2>

                <form name="artistSignupForm" class="form">
                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="name"><strong>Artist/Band Name</strong> *</label>
                            <input type="text" name="name" id="name" required>
                        </div>
                        <div class="col-1-2 last">
                            <label for="email"><strong>Email</strong> *</label>
                            <input type="email" name="email" id="email" required>
                        </div>
                    </div>
                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="password"><strong>Password</strong> *</label>
                            <input id="password" type="password" title="Password must contain at least 6 characters, including UPPER/lowercase and numbers" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}"  name="pwd" required onchange="form.repassword.pattern = this.value;">
                        </div>
                        <div class="col-1-2 last">
                            <label for="repassword"><strong>Re-enter Password</strong> *</label>
                            <input id="repassword" type="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}" name="repassword" required>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <input type="checkbox" name="chkAgree" required> I have read and agree to the Terms of Use.
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <div id="grecaptcha" class="g-recaptcha" data-sitekey="6LdjyvoSAAAAAL2m-7sPPZEtz0BNVRb-A_yY0BB_"></div>
                        </div>
                    </div>
                    <button class="large invert" onclick="javascript:signup()">Sign up now!</button>
                    <input type="hidden" value="ArtistSignup" name="target">
                    <div class="clear"></div>
                </form>
            </article>
            <!-- /article -->

        </div>
        <!-- /container -->
    </section>
    <!-- /Content -->

</section>
<!-- /page -->
<script src='https://www.google.com/recaptcha/api.js'></script>