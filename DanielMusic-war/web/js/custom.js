jQuery("html").removeClass("no-js").addClass("js"), jQuery(document).ready(function(e) {
        function t(t) {
            function a(t) {
                e(t).find(":input").each(function() {
                    switch (this.type) {
                        case "password":
                        case "select-multiple":
                        case "select-one":
                        case "text":
                        case "email":
                        case "textarea":
                            e(this).val("");
                            break;
                        case "checkbox":
                        case "radio":
                            this.checked = !1
                    }
                })
            }

            function n() {
                e("#ticker li:first").slideUp({
                    duration: 600,
                    easing: "easeOutSine",
                    complete: function() {
                        e(this).appendTo(e("#ticker")).slideDown()
                    }
                })
            }

            function s() {
                e("#scroll-arrows").find("img").stop().animate({
                    marginTop: "15px"
                }, 1e3, "easeOutSine", function() {
                    e("#scroll-arrows").find("img").stop().animate({
                        marginTop: "-5px"
                    }, 1e3, "easeInOutSine", function() {
                        s()
                    })
                })
            }

            function r(e, t, a, n) {
                return '<div id="fancybox-title">' + (e && e.length ? e : "") + "<span>(" + (a + 1) + " / " + t.length + ")</span></div>"
            }
            if (null != o && (o.update_content(), o.update_events("body")), e(".contact-form").length) {
                var l = e(".contact-form");
                l.append('<div id="ajax-message" style="display:none;margin-top:20px;"></div>');
                var c = e("#ajax-message");
                l.on("click", "input[type=submit]", function(t) {
                    c.hide(), NProgress.start(), e.post("plugins/contact-form.php", l.serialize(), function(e) {
                        c.html(e).show(), NProgress.done(), -1 != e.indexOf("success") && a(l)
                    }), t.preventDefault()
                })
            }
            if (e(".intro-resize").length > 0) {
                var d = function() {
                    var t = e(".intro-resize"),
                        a = (e(window).width(), e(window).height()),
                        n = a;
                    t.css({
                        height: n + "px"
                    });
                    var i = e(".container", t),
                        o = i.height(),
                        s = t.height(),
                        r = (s - o) / 2;
                    i.css({
                        "margin-top": r + "px"
                    })
                };
                d(), e(window).on("resize", d);
                setInterval(function() {
                    n()
                }, 4e3), s()
            }
            if (e(".tweets", t).each(function() {
                    var t = e(this),
                        a = t.data("tweets-count"),
                        n = "plugins/tweets.php";
                    (void 0 == a || "" == a) && (a = 1), t.html("Please wait..."), e.ajax({
                        url: n,
                        dataType: "html",
                        timeout: 1e4,
                        type: "GET",
                        error: function(e, a, n) {
                            t.html("An error occured: " + n)
                        },
                        success: function(n, i, o) {
                            t.html(n).hide(), e("li:nth-child(" + a + " ) ~ li", t).remove(), e("li:first-child", t).addClass("first-item"), t.show()
                        }
                    })
                }), e(".toggle").length > 0 && e(".toggle").each(function() {
                    e(".active-toggle", this).next().show();
                    var t = e(this);
                    e(".toggle-title", this).click(function() {
                        return e(this).is(".active-toggle") ? (e(this).removeClass("active-toggle"), e(".toggle-content", t).slideUp(400)) : (e(this).addClass("active-toggle"), e(".toggle-content", t).slideDown(400)), !1
                    })
                }), e(".tabs-wrap").length > 0 && e(".tabs-wrap").each(function() {
                    var t = e(this);
                    e(".tab-content", this).hide(), e(".tab-content:first", this).css("display", "block"), e("ul.tabs li:first a", this).addClass("active-tab"), e("ul.tabs li", this).click(function() {
                        if (!e(this).is("tab-active")) {
                            var a = e(this).find("a").attr("href");
                            e("ul.tabs li a", t).removeClass("active-tab"), e("a", this).addClass("active-tab"), e(".tab-content:not(:eq(" + a + "))", t).css("display", "none"), e(a, t).css("display", "block")
                        }
                        return !1
                    })
                }), e.fn.ResVid && e(t).ResVid(), e(".imagebox", t).fancybox({
                    overlayOpacity: .9,
                    overlayColor: "#000",
                    padding: 0,
                    titleFormat: r
                }), e(".mediabox", t).fancybox({
                    type: "iframe",
                    centerOnScroll: !0,
                    autoScale: !0,
                    overlayOpacity: .9,
                    padding: 0,
                    overlayColor: "#000",
                    titleFormat: r,
                    onStart: function(t) {
                        var a = e(t);
                        "auto" != a.data("width") && (this.width = a.data("width")), "auto" != a.data("height") && (this.height = a.data("height"))
                    }
                }), !Modernizr.touch && e.fn.thumbSlider && e(".thumb-slide").length > 0 && e(".thumb-slide").thumbSlider(), !Modernizr.touch && e.fn.topTip && e(".tip", t).topTip(), e.fn.countdown && e(".countdown-wrap", t).each(function(t) {
                    var a = e(this).data("event-date");
                    e(this).countdown(a, function(t) {
                        var a = e(this);
                        switch (t.type) {
                            case "seconds":
                            case "minutes":
                            case "hours":
                            case "days":
                            case "weeks":
                            case "daysLeft":
                                a.find("." + t.type).html(t.value);
                                break;
                            case "finished":
                        }
                    })
                }), e(".fullwidthbanner").length > 0 && (e(".fullwidthbanner .iframe-fullscreen-video").length > 0 && Modernizr.touch && e(".fullwidthbanner .iframe-fullscreen-video").remove(), void 0 != e.fn.cssOriginal && (e.fn.css = e.fn.cssOriginal), i = e(".fullwidthbanner", t).revolution({
                    delay: 9e3,
                    startwidth: 1170,
                    startheight: 620,
                    onHoverStop: "on",
                    thumbWidth: 100,
                    thumbHeight: 50,
                    thumbAmount: 4,
                    hideThumbs: 200,
                    navigationType: "both",
                    navigationArrows: "verticalcentered",
                    navigationStyle: "round",
                    touchenabled: "on",
                    navOffsetHorizontal: 0,
                    navOffsetVertical: 20,
                    fullWidth: "on",
                    shadow: 0
                })), e(".nice-select", t).niceselect(), e(".masonry", t).isotope({
                    portfolioelector: ".item"
                }), setTimeout(function() {
                    e(".masonry", t).isotope("layout")
                }, 1e3), e(".filters", t).length && e(".filters", t).each(function() {
                    var a = e(this).data("id");
                    void 0 != a && e(".filters .filter", t).each(function() {
                        var t = e(this).find("input").attr("name");
                        void 0 != t && (e("#" + a + " .item").each(function() {
                            var a = e(this);
                            a.addClass(a.data(t))
                        }), e(this).addClass("ready-filter").attr("data-id", a).attr("data-filter", t))
                    })
                }), e(".carousel", t).each(function() {
                    var t = e(this).attr("id");
                    void 0 != t && e("#" + t).owlCarousel({
                        items: 4,
                        itemsCustom: !1,
                        itemsDesktop: [1199, 4],
                        itemsDesktopSmall: [980, 4],
                        itemsTablet: [768, 2],
                        itemsTabletSmall: [600, 2],
                        itemsMobile: [479, 1],
                        singleItem: !1,
                        itemsScaleUp: !1,
                        slideSpeed: 200,
                        paginationSpeed: 800,
                        rewindSpeed: 1e3,
                        autoPlay: !1,
                        stopOnHover: !1,
                        navigation: !1,
                        navigationText: ["prev", "next"],
                        rewindNav: !0,
                        scrollPerPage: !1,
                        pagination: !0,
                        paginationNumbers: !1,
                        responsive: !0,
                        responsiveRefreshRate: 200,
                        responsiveBaseWidth: window,
                        baseClass: "owl-carousel",
                        theme: "owl-theme",
                        lazyLoad: !1,
                        lazyFollow: !0,
                        lazyEffect: "fade",
                        autoHeight: !1,
                        jsonPath: !1,
                        jsonSuccess: !1,
                        dragBeforeAnimFinish: !0,
                        mouseDrag: !0,
                        touchDrag: !0,
                        transitionStyle: !1,
                        addClassActive: !1
                    })
                }), e(".carousel-slider", t).each(function() {
                    var t = e(this).attr("id"),
                        a = e(this).data("effect"),
                        n = e(this).data("nav"),
                        i = e(this).data("pagination");
                    void 0 != t && e("#" + t).owlCarousel({
                        navigation: n,
                        pagination: i,
                        navigationText: ["<i class='icon icon-chevron-left'></i>", "<i class='icon icon-chevron-right'></i>"],
                        singleItem: !0,
                        transitionStyle: a
                    })
                }), e.fn.gMap && e(".gmap").length && e(".gmap", t).each(function() {
                    var t = e(this),
                        a = t.data("address"),
                        n = t.data("zoom"),
                        i = t.data("zoom_control"),
                        o = t.data("scrollwheel");
                    t.gMap({
                        address: a,
                        zoom: n,
                        zoomControl: i,
                        scrollwheel: o,
                        markers: [{
                            address: a
                        }],
                        icon: {
                            image: "img/map-marker.png",
                            iconsize: [48, 56],
                            iconanchor: [20, 56]
                        }
                    })
                }), e("#jumpToAlbum").length > 0) {
                var h = e(".tabs-wrap"),
                    u = "#tab-releases";
                e("ul.tabs li a", h).removeClass("active-tab"), e('ul.tabs li a[href*="' + u + '"]').addClass("active-tab"), e(".tab-content:not(:eq(" + u + "))", h).css("display", "none"), e(u, h).css("display", "block");
                var f = e("#jumpToAlbum").val();
                e("#album_" + f)[0].scrollIntoView(!0)
            }
        }

        function a() {
            e("#ajax-container .carousel, #ajax-container .carousel-slider").each(function() {
                var t = e(this).attr("id");
                void 0 != t && void 0 != e("#" + t).data("owlCarousel") && e("#" + t).data("owlCarousel").destroy()
            }), e("#ajax-container .masonry").isotope("destroy"), (void 0 != i || null != i) && (i.revpause(), i = null)
        }
        var n = {
            nav_height: e("#main-nav").css("height").replace("px", ""),
            text_pause_time: 3e3,
            one_loop: !0,
            auto_response: !0,
            animation_content: !0,
            homepage: "home.jsp",
            document_title: document.title
        };
        NProgress.configure({
            showSpinner: !1
        });
        var i, o = null;
        "undefined" != Modernizr && Modernizr.touch && e("body").addClass("touch-device"),
            function() {
                var t = e("#nav").children("ul");
                if (e("li", t).on("mouseenter", function() {
                        var t = e(this),
                            a = t.children("ul");
                        a.length && t.addClass("active"), a.stop(!0, !0).addClass("show-list")
                    }).on("mouseleave", function() {
                        e(this).removeClass("active").children("ul").stop(!0, !0).removeClass("show-list")
                    }), n.auto_response) {
                    var a = e("#nav").clone();
                    e("ul:not(:first-child)", a).addClass("dl-submenu"), e("> ul", a).addClass("dl-menu"), a = a.children("ul"), e("#dl-menu").append(a)
                }
                e.fn.dlmenu && e("#dl-menu").dlmenu({
                    onLinkClick: function(t, a) {
                        return e("#dl-menu").dlmenu("closeMenu"), !1
                    }
                }), e("#dl-menu ul").css("max-height", e(window).height() - n.nav_height + "px"), e(window).resize(function() {
                    e("#dl-menu ul").css("max-height", e(window).height() - n.nav_height + "px")
                }), e("#nav-search, #close-search").on("click", function(t) {
                    e("#search-wrap").toggleClass("show-search"), t.preventDefault()
                })
            }(), t("body"),
            function() {
                o = new e.ScampPlayer(e("#scamp_player"), {
                    volume: 100,
                    autoplay: !1,
                    no_track_image: "js/scamp_player/img/no-track-image.png",
                    loop: !1,
                    random: !1,
                    titlebar: !0,
                    check_files: !1,
                    client_id: "23f5c38e0aa354cdd0e1a6b4286f6aa4",
                    onReady: function() {},
                    sm_options: {
                        url: "js/scamp_player/js/swf/",
                        flashVersion: 9,
                        preferFlash: !1,
                        useHTML5Audio: !0,
                        allowScriptAccess: "always",
                        debugMode: !1,
                        debugFlash: !1,
                        useConsole: !1
                    }
                })
            }(),
            function() {
                e("body").on("click", ".smooth-scroll", function(t) {
                    var a = e(this).attr("href");
                    e(a).length && e.scrollTo(a, 750, {
                        easing: "swing",
                        offset: {
                            top: -n.nav_height,
                            left: 0
                        }
                    }), t.preventDefault()
                }), !Modernizr.touch && e.fn.thumbGlitch && e("body").thumbGlitch(), e("body").on("change", ".ready-filter", function() {
                    var t = e(this).data("id"),
                        a = "";
                    return void 0 == t ? !1 : (e('.filters [data-id="' + t + '"]').each(function() {
                        var t = e(this).find(".value_wrapper > .active input").val();
                        void 0 != t && "*" != t && (t = t.replace(t, "." + t), a += t)
                    }), "" == a && (a = "*"), e("#" + t).isotope({
                        filter: a
                    }), void setTimeout(function() {
                        e("#" + t).isotope("layout")
                    }, 500))
                }), e(window).on("resize", function() {
                    setTimeout(function() {
                        e(".masonry").isotope("layout")
                    }, 500)
                })
            }(),
            function() {
                function i() {
                    var e = window.location.hash,
                        t = e.replace("#!/", "");
                    if (t = t.split("/"), 1 == t.length) return "" == e || "#!/" == e || "#!/index" == e || "#!/home" == e ? (l = !0, d = n.homepage, void o()) : -1 === e.indexOf("#!") ? !1 : (e = e.replace("#!/", ""), e += ".jsp", d = e, void o());
                    if (-1 === e.indexOf("#!")) return !1;
                    var a = t.join("/");
                    return a += ".jsp", d = a, void o()
                }

                function o() {
                    e("body").append('<div id="loading-layer"></div>'), NProgress.start(), e.ajax({
                        type: "POST",
                        url: d,
                        timeout: 5e3,
                        data: null,
                        async: !1,
                        dataType: "html",
                        success: function(t) {
                            if (0 != parseInt(t)) {
                                var a = e(t);
                                return e("img", a).length > 0 ? s(a) : r(a), !1
                            }
                            return NProgress.done(), e("#loading-layer").remove(), !1
                        },
                        error: function(t, a, n) {
                            return console.log(t.status), console.log(t.statusText), console.log(t.responseText), NProgress.done(), e("#loading-layer").remove(), !1
                        }
                    })
                }

                function s(t) {
                    var a = e("img", t).length,
                        n = 0;
                    e("img", t).load(function() {
                        return n++, n >= a ? (r(t), !1) : void 0
                    }).error(function() {
                        return a--, n >= a ? (r(t), !1) : void 0
                    })
                }

                function r(i) {
                    a();
                    var o = window.location.hash;
                    l && (o = n.homepage, o = o.replace(".jsp", ""), o = "#!/" + o), e("#main-nav-wrapper a.selected").removeClass("selected"), e('#main-nav-wrapper a[href="' + o + '"]').addClass("selected");
                    var s = e('#nav a[href="' + o + '"]').addClass("selected");
                    s.parents("li.submenu").length > 0 && s.parents("li.submenu").children("a:eq(0)").addClass("selected"), window.scrollTo(0, 0), e("#ajax-container").removeClass("loaded"), e("#ajax-container").empty(), pag_content = i, e("#ajax-container").append(i), t(i), n.animation_content ? e("#ajax-container").addClass("loaded animation") : e("#ajax-container").addClass("loaded");
                    var r = e("#page").data("title");
                    void 0 == r || "" == r ? document.title = n.document_title : document.title = r, NProgress.done(), l = !1, e("#loading-layer").remove()
                }
                var l = !1,
                    c = null,
                    d = null,
                    h = function(t) {
                        var a = e(t.currentTarget).attr("href");
                        if (l = !1, c = e(t.currentTarget), "javascript:;" == a) return !1;
                        if (c.hasClass("selected")) return !1;
                        var n = a.split("/");
                        return 1 == n.length && "#!" == n[0] ? window.location.hash = "#!/home" : 2 == n.length && n[1].indexOf("index") >= 0 ? window.location.hash = "#!/home" : window.location.hash = a, t.preventDefault, !1
                    };
                e("body").on("click", 'a[href$="#!"]', h), e(window).bind("hashchange", i), i()
            }()
    }),
    function(e) {
        e.extend(e, {
            placeholder: {
                browser_supported: function() {
                    return void 0 !== this._supported ? this._supported : this._supported = !!("placeholder" in e('<input type="text">')[0])
                },
                shim: function(t) {
                    var a = {
                        color: "#888",
                        cls: "placeholder",
                        selector: "input[placeholder], textarea[placeholder]"
                    };
                    return e.extend(a, t), !this.browser_supported() && e(a.selector)._placeholder_shim(a)
                }
            }
        }), e.extend(e.fn, {
            _placeholder_shim: function(t) {
                function a(t) {
                    var a = e(t).offsetParent().offset(),
                        n = e(t).offset();
                    return {
                        top: n.top - a.top,
                        left: n.left - a.left,
                        width: e(t).width()
                    }
                }

                function n(t) {
                    var i = t.data("target");
                    "undefined" != typeof i && (t.css(a(i)), e(window).one("resize", function() {
                        n(t)
                    }))
                }
                return this.each(function() {
                    var i = e(this);
                    if (i.is(":visible")) {
                        if (i.data("placeholder")) {
                            var o = i.data("placeholder");
                            return o.css(a(i)), !0
                        }
                        var s = {};
                        i.is("textarea") || "auto" == i.css("height") || (s = {
                            lineHeight: i.css("height"),
                            whiteSpace: "nowrap"
                        });
                        var r = e("<label />").text(i.attr("placeholder")).addClass(t.cls).css(e.extend({
                            position: "absolute",
                            display: "inline",
                            "float": "none",
                            overflow: "hidden",
                            textAlign: "left",
                            color: t.color,
                            cursor: "text",
                            paddingTop: 0,
                            paddingRight: i.css("padding-right"),
                            paddingBottom: 0,
                            paddingLeft: i.css("padding-left"),
                            fontSize: i.css("font-size"),
                            fontFamily: i.css("font-family"),
                            fontStyle: i.css("font-style"),
                            fontWeight: i.css("font-weight"),
                            textTransform: i.css("text-transform"),
                            backgroundColor: "transparent",
                            zIndex: 99
                        }, s)).css(a(this)).attr("for", this.id).data("target", i).click(function() {
                            e(this).data("target").focus()
                        }).insertBefore(this);
                        i.data("placeholder", r).focus(function() {
                            r.hide()
                        }).blur(function() {
                            r[i.val().length ? "hide" : "show"]()
                        }).triggerHandler("blur"), e(window).one("resize", function() {
                            n(r)
                        })
                    }
                })
            }
        })
    }(jQuery), jQuery(document).add(window).bind("ready load", function() {
        jQuery.placeholder && jQuery.placeholder.shim()
    });