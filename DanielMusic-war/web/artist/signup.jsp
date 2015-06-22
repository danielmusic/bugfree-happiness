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
                    function loadAjax() {
                        var v = grecaptcha.getResponse();
                        if (v.length !== 0) {
                            var source;
                            if (document.getElementById('r1').checked) {
                                source = "ArtistSignup";
                            } else if (document.getElementById('r2').checked) {
                                source = "BandSignup";
                            }

                            var name = $('#name').val();
                            var email = $('#email').val();
                            var password = $('#password').val();
                            var chkAgree = $('#chkAgree').val();

                            url = "./ClientAccountManagementController?target=AccountSignup";
                            $.ajax({
                                type: "GET",
                                async: false,
                                url: url,
                                data: {'source': source, 'name': name, 'email': email, 'password': password, 'chkAgree': chkAgree, 'g-recaptcha-response': v},
                                dataType: "text",
                                success: function (val) {
                                    window.event.returnValue = true;
                                    var json = JSON.parse(val);
                                    if (json.result) {
                                        alert("json.result " + json.result);
                                        window.event.returnValue = false;
                                        window.location.href = "#!/login";
                                        document.loginForm.getElementById("errMsg").style.display = "block";
                                        document.loginForm.getElementById('errMsg').innerHTML = json.message;
                                    } else {
                                        alert("json.result " + json.result);
                                        window.event.returnValue = false;
                                        window.location.href = "#!/artist/signup";
                                        document.getElementById("chkAgree").reset();
                                        document.getElementById("grecaptcha").reset();
                                        document.getElementById("errMsg").style.display = "block";
                                        document.getElementById('errMsg').innerHTML = json.message;
                                    }
                                },
                                error: function (xhr, status, error) {
                                    document.getElementById("errMsg").style.display = "block";
                                    document.getElementById('errMsg').innerHTML = error;
                                    hideLoader();
                                    ajaxResultsError(xhr, status, error);
                                }
                            });
                        } else {
                            document.getElementById("errMsg").style.display = "block";
                            document.getElementById('errMsg').innerHTML = "You can't leave Captcha Code empty!";
                        }
                    }
                </script>

                <span id="displayMsg"></span>
                <p class="error" id="errMsg" style="display:none;"></p>
                <p class="success" id="goodMsg"  style="display:none;"></p>

                <h2>Sign up for an artist account</h2>

                <form name="AccountSignupForm" class="form">

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="name"><strong>I am signing up as a </strong></label>
                            <div id="options">
                                <label><input type="radio" id="r1" value="ArtistSignup" name="option" required> Artist</label>
                                <label><input type="radio" id="r2" value="BandSignup" name="option" required> Band</label>
                            </div>
                        </div>
                    </div>

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
                            <input id="password" type="password" title="Password must contain at least 6 characters, including UPPER/lowercase and numbers" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}"  name="password" id="password" required onchange="form.repassword.pattern = this.value;">
                        </div>
                        <div class="col-1-2 last">
                            <label for="repassword"><strong>Re-enter Password</strong> *</label>
                            <input id="repassword" type="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}" name="repassword" required>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label><input type="checkbox" id="chkAgree" value="chkAgree" name="option" required> I have read and agree to the Terms of Use.</label>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <div id="grecaptcha" class="g-recaptcha" data-sitekey="6LdjyvoSAAAAAL2m-7sPPZEtz0BNVRb-A_yY0BB_"></div>
                        </div>
                    </div>
                    <button class="large invert" onclick="loadAjax()">Sign up now!</button>
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