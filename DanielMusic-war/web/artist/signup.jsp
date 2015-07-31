<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Artist Signup">
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <div class="container">
            <h1 class="heading-l">Artist Signup</h1>
            <h2 class="heading-m">It's now or <span class="color">Never</span></h2>
        </div>
    </section>

    <section class="content section">
        <div class="container">
            <article>
                <script>
                    function loadAjaxSignupArtist() {
                        if (validatePassword()) {
                            if (document.getElementById('chkAgree').checked) {
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
                                    var chkAgree = "checked";

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
                                                window.event.returnValue = false;
                                                document.loginForm.getElementById("goodMsg").style.display = "block";
                                                document.loginForm.getElementById('goodMsg').innerHTML = json.message;
                                                window.location.href = "#!/login";
                                            } else {
                                                window.event.returnValue = false;
                                                document.getElementById("chkAgree").checked = false;
                                                document.getElementById("grecaptcha").reset();
                                                document.getElementById("errMsg").style.display = "block";
                                                document.getElementById('errMsg').innerHTML = json.message;
                                                window.location.href = "#!/artist/signup";
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
                                    window.event.returnValue = false;
                                    document.getElementById("errMsg").style.display = "block";
                                    document.getElementById('errMsg').innerHTML = "You can't leave Captcha Code empty!";
                                }
                            }
                        }
                    }

                    function validatePassword() {
                        var password = document.getElementById("password").value;
                        var repassword = document.getElementById("repassword").value;
                        var ok = true;
                        if ((password !== null && repassword !== null) && (password !== "" && repassword !== "")) {
                            if (password !== repassword) {
                                document.getElementById("password").style.borderColor = "#E34234";
                                document.getElementById("repassword").style.borderColor = "#E34234";
                                document.getElementById("errMsg").style.display = "block";
                                document.getElementById('errMsg').innerHTML = "Passwords do not match. Please key again.";
                                ok = false;
                            } else if (password === repassword) {
                                if (password.length < 8) {
                                    document.getElementById("errMsg").style.display = "block";
                                    document.getElementById('errMsg').innerHTML = "Passwords too short. At least 8 characters. Please key again.";
                                    ok = false;
                                }
                            }
                        } else {
                            return ok;
                        }
                        return ok;
                    }
                </script>

                <form name="AccountSignupForm" class="form">
                    <p class="error" id="errMsg" style="display:none;"></p>
                    <p class="success" id="goodMsg"  style="display:none;"></p>

                    <h2>Sign up for an artist account</h2>

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
                            <label><input type="checkbox" id="chkAgree" name="option" required> I have read and agree to the Terms of Use.</label>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <div id="grecaptcha" name="grecaptcha" class="g-recaptcha" data-sitekey="6LfmfQoTAAAAAMud4GA01cFMlPc4HPG3NFKvc8XA"></div>
                        </div>
                    </div>
                    <button class="large invert" onclick="loadAjaxSignupArtist()">Sign up now!</button>
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