<section id="page" data-title="sounds.sg | login">
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <h1 class="heading-l">account login</h1>
    </section>
    <section class="content section">
        <div class="container" style="width: 440px;">
            <article>
                <form class="form" name="loginForm" action="ClientAccountManagementController">
                    <p class="error" id="errMsg" style="display:none;"></p>
                    <p class="success" id="goodMsg"  style="display:none;"></p>

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

                    <div class="row clearfix">
                        <div class="col-1-1" style="text-align: center;">
                            <button type="submit" class="medium invert">Login</button>
                        </div>
                    </div>

                    <input type="hidden" value="AccountLogin" name="target">

                    <div class="clear"></div>
                </form>
            </article>
        </div>
    </section>
</section>
<script type="text/javascript">
    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-66150326-1']);
    var d = document.location.pathname + document.location.search + document.location.hash;
    _gaq.push(['_trackPageview', d]);

    (function () {
        var ga = document.createElement('script');
        ga.type = 'text/javascript';
        ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(ga, s);
    })();
</script>