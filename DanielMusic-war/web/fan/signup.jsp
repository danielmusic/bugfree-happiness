<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="sounds.sg | fan signup">
    <style>
        .heading-l{
            font-size: 80px;
            margin-bottom: 0px;
        }

        @media only screen and (min-width: 768px) and (max-width: 980px) {
            .container{
                width: 100%;
            }
        }

        @media only screen and (max-width: 767px) {
            .container{
                width: 100%;
            }
        }

        @media only screen and (min-width: 480px) and (max-width: 767px) {
            .container{
                width: 100%;
            }
        }
    </style>

    <!-- ############################# Intro ############################# -->
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <h1 class="heading-l">fan signup</h1>

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
                    function loadAjaxSignupFan() {
                        if (validatePassword()) {
                            if (document.getElementById('chkAgree').checked) {
                                var v = grecaptcha.getResponse();
                                if (v.length !== 0) {
                                    var source = "FanSignup";
                                    var name = $('#name').val();
                                    var email = $('#email').val();
                                    var password = $('#password').val();
                                    var chkAgree = document.getElementById('chkAgree').value;

                                    url = "./ClientAccountManagementController?target=AccountSignup";
                                    $.ajax({
                                        method: "POST",
                                        async: false,
                                        url: url,
                                        data: {'source': source, 'name': name, 'email': email, 'password': password, 'chkAgree': chkAgree, 'g-recaptcha-response': v},
                                        dataType: "text",
                                        success: function (val) {
                                            var json = JSON.parse(val);
                                            if (json.result) {
                                                window.location.href = "#!/login";
                                            } else {
                                                document.getElementById("chkAgree").checked = false;
                                                document.getElementById("grecaptcha").reset();
                                                document.getElementById("errMsg").style.display = "block";
                                                document.getElementById('errMsg').innerHTML = json.message;
                                                window.location.href = "#!/fan/signup";
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
                                    document.getElementById('errMsg').innerHTML = "Sorry, Password can not be empty.";
                                    document.getElementById("errMsg").style.display = "block";
                                    document.getElementById('errMsg').innerHTML = "Plase tick the reCAPTCHA box to verify that you are a human!";
                                }
                            }
                        }
                    }

                    function validatePassword() {
                        var password = document.getElementById("password").value;
                        var repassword = document.getElementById("repassword").value;
                        if (repassword == null || repassword == "", password == null || password == "") {
                            document.getElementById("errMsg").style.display = "block";
                            document.getElementById('errMsg').innerHTML = "Sorry, Password can not be empty.";
                            document.getElementById("chkAgree").checked = false;
                            document.getElementById("grecaptcha").reset();
                            return false;
                        }

                        var name = document.getElementById("name").value;
                        if (name == null || name == "") {
                            document.getElementById("errMsg").style.display = "block";
                            document.getElementById('errMsg').innerHTML = "Sorry, Name can not be empty.";
                            document.getElementById("chkAgree").checked = false;
                            document.getElementById("grecaptcha").reset();
                            return false;
                        }

                        var email = document.getElementById("email").value;
                        if (email == null || email == "") {
                            document.getElementById("errMsg").style.display = "block";
                            document.getElementById('errMsg').innerHTML = "Sorry, Email can not be empty.";
                            document.getElementById("chkAgree").checked = false;
                            document.getElementById("grecaptcha").reset();
                            return false;
                        }

                        if ((password !== null && repassword !== null) && (password !== "" && repassword !== "")) {
                            if (password !== repassword) {
                                document.getElementById("chkAgree").checked = false;
                                document.getElementById("grecaptcha").reset();
                                document.getElementById("errMsg").style.display = "block";
                                document.getElementById('errMsg').innerHTML = "Passwords do not match. Please key again.";
                                return false;
                            } else if (password === repassword) {
                                if (password.length < 6) {
                                    document.getElementById("chkAgree").checked = false;
                                    document.getElementById("grecaptcha").reset();
                                    document.getElementById("errMsg").style.display = "block";
                                    document.getElementById('errMsg').innerHTML = "Password must be longer than 6 characters.";
                                    return false;
                                }
                            }
                        }
                        return true;
                    }
                </script>

                <form name="AccountSignupForm" class="form">
                    <div class="container" style="width: 50%;">
                        <p class="error" id="errMsg" style="display:none;"></p>
                        <p class="success" id="goodMsg"  style="display:none;"></p>

                        <div class="row clearfix">
                            <div class="col-1-1" style="text-align: center;">
                                <label for="name"><strong>Sign up for a fan account</strong></label>
                            </div>
                        </div>

                        <div class="row clearfix">
                            <div class="col-1-1">
                                <label for="name"><strong>Name</strong> *</label>
                                <input type="text" name="name" id="name" required>
                            </div>
                        </div>

                        <div class="row clearfix">
                            <div class="col-1-1">
                                <label for="email"><strong>Email</strong> *</label>
                                <input type="email" name="email" id="email" required>
                            </div>
                        </div>

                        <div class="row clearfix">
                            <div class="col-1-1">
                                <label for="password"><strong>Password</strong> *</label>
                                <input id="password" type="password" title="Password should contain at least 6 characters, including UPPER/lowercase and numbers" name="password" id="password" required onchange="form.repassword.pattern = this.value;">
                                <div style="font-size: 12px; color: rgb(234, 66, 51);">Password should contain at least 6 characters, including Upper/lowercase and numbers</div>
                            </div>
                        </div>

                        <div class="row clearfix">
                            <div class="col-1-1">
                                <label for="repassword"><strong>Re-enter Password</strong> *</label>
                                <input id="repassword" type="password" name="repassword" required>
                            </div>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1" style="text-align: center;">
                            <label><input type="checkbox" id="chkAgree" name="option" required> I have read and agree to the <a href="#!/terms-of-use">Terms of Use.</a></label>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <div align="center" id="grecaptcha" name="grecaptcha" class="g-recaptcha" data-sitekey="6LfmfQoTAAAAAMud4GA01cFMlPc4HPG3NFKvc8XA"></div>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1" style="text-align: center;">
                            <button type="button" class="large invert" onclick="loadAjaxSignupFan()">create account</button>
                        </div>
                    </div>

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