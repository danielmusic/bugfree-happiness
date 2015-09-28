<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Fan Signup">
    <!-- ############################# Intro ############################# -->
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <h1 class="heading-l">Fan Signup</h1>
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
                                    document.getElementById('errMsg').innerHTML = "You can't leave Captcha Code empty!";
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
                        if ((password !== null && repassword !== null) && (password !== "" && repassword !== "")) {
                            if (password !== repassword) {
                                document.getElementById("chkAgree").checked = false;
                                document.getElementById("grecaptcha").reset();
                                document.getElementById("errMsg").style.display = "block";
                                document.getElementById('errMsg').innerHTML = "Passwords do not match. Please key again.";
                                return false;
                            } else if (password === repassword) {
                                if (password.length < 8) {
                                    document.getElementById("chkAgree").checked = false;
                                    document.getElementById("grecaptcha").reset();
                                    document.getElementById("errMsg").style.display = "block";
                                    document.getElementById('errMsg').innerHTML = "Passwords too short. At least 8 characters. Please key again.";
                                    return false;
                                }
                            }
                        }
                        return true;
                    }
                </script>

                <form name="AccountSignupForm" class="form">
                    <p class="error" id="errMsg" style="display:none;"></p>
                    <p class="success" id="goodMsg"  style="display:none;"></p>

                    <h2>Sign up for a fah account</h2>

                    <div class="row clearfix">
                        <div class="col-1-1">
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
                    <button type="button" class="large invert" onclick="loadAjaxSignupFan()">Sign up now!</button>
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