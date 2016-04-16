<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="sounds.sg">
    <!-- ############################# Sections ############################# -->
    <ul class="anim-slider">

        <!-- Slide No1 -->
        <li class="anim-slide">
            <img src="placeholders/Terrestrea1.jpg">
        </li>

        <!-- Slide No2 -->
        <li class="anim-slide">
            <h1 id="slide1">Slide 2</h1>
        </li>

        <!-- Slide No3 -->
        <li class="anim-slide">
            <h1 id="slide1">Slide 3</h1>
        </li>

        <!-- Arrows -->
        <nav class="anim-arrows">
            <span class="anim-arrows-prev">
                <i class="fa fa-angle-left fa-3x"></i>
            </span>
            <span class="anim-arrows-next">
                <i class="fa fa-angle-right fa-3x"></i>
            </span>
        </nav>
        <!-- Dynamically created dots -->

    </ul>
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

<link rel="stylesheet" href="../js/Vchouliaras-jquery.animateSlider/jquery.animateSlider.css"/>
<script src="../js/Vchouliaras-jquery.animateSlider/jquery.animateSlider.min.js" type="text/javascript"></script>
<script>
    //Slider
    $(".anim-slider").animateSlider(
            {
                autoplay: true,
                interval: 5500,
                animations:
                        {
                            0: //Slide No1
                                    {
                                        li:
                                                {
                                                    show: "fadeIn",
                                                    hide: "fadeOutLeftBig",
                                                    delayShow: "delay0.5s"
                                                }
                                    },
                            1: //Slide No2
                                    {
                                        li:
                                                {
                                                    show: "fadeInLeft",
                                                    hide: "fadeOutLeftBig",
                                                    delayShow: "delay0-5s"
                                                }
                                    },
                            2:
                                    {
                                        li:
                                                {
                                                    show: "fadeInUp",
                                                    hide: "fadeOutDownBig",
                                                    delayShow: "delay0-5s"
                                                }
                                    }
                        }
            });
</script>