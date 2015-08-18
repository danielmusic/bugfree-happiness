<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Sounds.sg">
    <!-- ############################# Intro ############################# -->
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <div class="container">
            <div class="col-1-2" style="text-align: left;">
                <h1 class="heading-l">Thank you</h1>
            </div>
            <div class="col-1-2 last" style="text-align: right;"></div>
        </div>
    </section>

    <section class="content section">
        <div class="container">
            <jsp:include page="jspIncludePages/displayMessage.jsp" />
            <article>
                    <p>
                        Thank you so much for your purchase. Do note and read the terms and conditions.
                        <br/><br/>
                        <a href="#!/home">Click here to return to home</a>
                    </p>
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
