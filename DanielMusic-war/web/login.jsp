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
                url = "./ClientAccountManagementController?target=ArtistLogin";
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    data: {'email': email, 'pwd': password},
                    dataType: "text",
                    success: function (val) {
                        alert("hello");
                        alert(val);
                        var json = JSON.parse(val);
                        alert(json.id);
                        alert(json.result);

                        if (json.result) {
                            window.location.href = "#!/artist/profile";
                        } else {
                            alert("failed");
                            window.location.href = "#!/login";
                        }
                    },
                    error: function (xhr, status, error) {
                        alert("Error: " + error);
                        hideLoader();
                        ajaxResultsError(xhr, status, error);
                    }
                });
            }
        </script>
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
                    <input type="button" onclick="loadAjax()" value="Login" class="large invert">
                    <input type="hidden" value="ArtistLogin" name="target">
                    <div class="clear"></div>
                </form>
            </article>
        </div>
    </section>
</section>

