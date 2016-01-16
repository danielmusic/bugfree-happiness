<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="sounds.sg">
    <!-- REVOLUTION BANNER CSS SETTINGS -->
    <link rel="stylesheet" type="text/css" href="js/rs-plugin/css/settings.css" media="screen" />

    <style>
        a, a > * {
            color: white;
        }

        #page {}
        .index-page {
            min-height: 100px;
        }

        @media only screen and (min-width: 480px) and (max-width: 767px) {
            #footer {
                padding: 0px 0 20px 0;
            }
        }
    </style>

    <!-- ############################# Sections ############################# -->
    <section class="revoslider section border-bottom">
        <div class="fullscreenbanner-container fullwidthbanner">
            <ul>
                <li data-transition="fade" data-slotamount="7" data-masterspeed="100">
                    <img src="placeholders/revoslider/video-cover.jpg" alt="" data-bgfit="cover" data-bgposition="left top" data-bgrepeat="no-repeat" />
                    <div class="tp-static-layers">
                        <div class="tp-caption lfb customin ltl tp-resizeme" 
                             data-x="100" 
                             data-y="360" 
                             data-customin="x:0;y:0;z:0;rotationX:90;rotationY:0;rotationZ:0;scaleX:1;scaleY:1;skewX:0;skewY:0;opacity:0;transformPerspective:200;transformOrigin:50% 0%;"
                             data-speed="1500"
                             data-start="100"
                             data-easing="Power4.easeInOut"
                             data-splitin="none"
                             data-splitout="none"
                             data-elementdelay="0.01"
                             data-endelementdelay="0.1"
                             data-endspeed="1000"
                             data-endeasing="Power4.easeIn">
                        </div>
                    </div>
                </li>

                <!-- 
                <li data-transition="fade" data-slotamount="7" data-masterspeed="100">
                    <img src="placeholders/revoslider/slide01-bg.jpg" alt="" data-bgfit="cover" data-bgposition="left top" data-bgrepeat="no-repeat" />
                </li>
                -->

            </ul>
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

