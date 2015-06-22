<section id="page" data-title="Account Login">

    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <h1 class="heading-l">Account Login</h1>
        <span class="overlay dots"></span>
    </section>
    <section class="content section">
        <script>
            function loadAjax() {
                var email = $('#email').val();
                var password = $('#password').val();
                url = "./ClientAccountManagementController?target=AccountLogin";
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    data: {'email': email, 'password': password},
                    dataType: "text",
                    success: function (val) {
                        var json = JSON.parse(val);
                        if (json.result) {
                            window.location.href = "#!/artist/profile";
                        } else {
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
            }
        </script>
        <div class="container">
            <article>

                <form class="form" name="loginForm">
                    <jsp:include page="displayMessage.jsp" />
                    <p class="error" id="errMsg" style="display:none;"></p>
                    <p class="success" id="goodMsg"  style="display:none;"></p>


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
                            <a href="">Forget password?</a>
                        </div>
                    </div>
                    <button type="button" onclick="loadAjax()"  class="large invert">Login</button>
                    <div class="clear"></div>
                </form>
            </article>
        </div>
    </section>
</section>

