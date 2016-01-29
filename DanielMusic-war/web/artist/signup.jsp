<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="sounds.sg | artist signup">
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
                                    var chkAgree = document.getElementById('chkAgree').value;

                                    url = "./ClientAccountManagementController?target=AccountSignup";
                                    $.ajax({
                                        method: "POST",
                                        async: false,
                                        url: url,
                                        data: {'source': source, 'name': name, 'email': email, 'password': password, 'chkAgree': chkAgree, 'g-recaptcha-response': v},
                                        dataType: "text",
                                        cache: false,
                                        success: function (val) {
                                            var json = JSON.parse(val);
                                            if (json.result) {
                                                window.location.href = "#!/login";
                                            } else {
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
                                    document.getElementById("chkAgree").checked = false;
                                    document.getElementById("grecaptcha").reset();
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
                            document.getElementById('errMsg').innerHTML = "Please fill in your password.";
                            document.getElementById("chkAgree").checked = false;
                            document.getElementById("grecaptcha").reset();
                            return false;
                        }

                        var name = document.getElementById("name").value;
                        if (name == null || name == "") {
                            document.getElementById("errMsg").style.display = "block";
                            document.getElementById('errMsg').innerHTML = "Please fill in your name.";
                            document.getElementById("chkAgree").checked = false;
                            document.getElementById("grecaptcha").reset();
                            return false;
                        }

                        var email = document.getElementById("email").value;
                        if (email == null || email == "") {
                            document.getElementById("errMsg").style.display = "block";
                            document.getElementById('errMsg').innerHTML = "Please fill in your email.";
                            document.getElementById("chkAgree").checked = false;
                            document.getElementById("grecaptcha").reset();
                            return false;
                        }

                        if ((password !== null && repassword !== null) && (password !== "" && repassword !== "")) {
                            if (password !== repassword) {
                                document.getElementById("chkAgree").checked = false;
                                document.getElementById("grecaptcha").reset();
                                document.getElementById("errMsg").style.display = "block";
                                document.getElementById('errMsg').innerHTML = "Passwords do not match. Please try again.";
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
                        <h1 style="font-size: 56px; text-align: center; margin-bottom: 60px;">artist signup</h1>

                        <p class="error" id="errMsg" style="display:none;"></p>
                        <p class="success" id="goodMsg"  style="display:none;"></p>

                        <div class="row clearfix">
                            <div class="col-1-1" style="text-align: center;">
                                <label for="name"><strong>I am signing up as a </strong></label>

                                <div id="options">
                                    <label style="display: inline; margin-right: 22px;"><input type="radio" id="r1" value="ArtistSignup" name="option" style="margin-right: 5px;" required> Solo Artist</label>
                                    <label style="display: inline;"><input type="radio" id="r2" value="BandSignup" name="option" style="margin-right: 5px;" required> Band</label>
                                </div>
                            </div>
                        </div>

                        <div class="row clearfix">
                            <div class="col-1-1">
                                <label for="name"><strong>Artist/Band Name</strong> *</label>
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
                                <input id="password" type="password" title="Password should contain at least 6 characters, including Upper/lowercase and numbers"  name="password" id="password" required onchange="form.repassword.pattern = this.value;">
                                <div style="font-size: 12px; color: rgb(234, 66, 51);">Password should contain at least 6 characters</div>
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

                    <div class="row clearfix" >
                        <div class="col-1-1">
                            <div align="center" id="grecaptcha" name="grecaptcha" class="g-recaptcha" data-sitekey="6LfmfQoTAAAAAMud4GA01cFMlPc4HPG3NFKvc8XA"></div>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1" style="text-align: center;">
                            <button align="center"  type="button" class="large invert" onclick="loadAjaxSignupArtist()" style="width: 304px; margin-right: 0px;">create account</button>
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