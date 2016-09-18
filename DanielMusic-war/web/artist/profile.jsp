<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="sounds.sg | profile">
    <%@page import="EntityManager.Genre"%>
    <%@page import="java.util.List"%>
    <%@page import="EntityManager.Artist"%>
    <%@page import="EntityManager.Account"%>
    <%@page import="java.text.SimpleDateFormat"%>
    <section class="content section">
        <link rel="stylesheet" href="js/crop/crop.css" type="text/css" />
        <script src="js/crop/crop.js" type="text/javascript"></script>
        <div class="container">
            <article>
                <script type="text/javascript">
                    var fd = new FormData();
                    var dataURL;
                    var image = document.querySelector('.img-container > img');
                    var cropper;

                    function submitForm() {
                        var email = $('#email').val();
                        var contactEmail = $('#contactEmail').val();
                        var paypalEmail = $('#paypalEmail').val();
                        var genreID = $('#genre').val();
                        var bio = $('#bio').val();
                        var influences = $('#influences').val();
                        var dateFormed = $('#dateFormed').val();
                        var bandMembers = $('#bandMembers').val();
                        var facebookURL = $('#facebookURL').val();
                        var instagramURL = $('#instagramURL').val();
                        var twitterURL = $('#twitterURL').val();
                        var websiteURL = $('#websiteURL').val();
                        var target = $('#target').val();
                        if (dataURL != null) {
                            fd.append('picture', dataURL);
                        }
                        fd.append('email', email);
                        fd.append('contactEmail', contactEmail);
                        fd.append('paypalEmail', paypalEmail);
                        fd.append('genreID', genreID);
                        fd.append('bio', bio);
                        fd.append('influences', influences);
                        fd.append('dateFormed', dateFormed);
                        fd.append('bandMembers', bandMembers);
                        fd.append('facebookURL', facebookURL);
                        fd.append('instagramURL', instagramURL);
                        fd.append('twitterURL', twitterURL);
                        fd.append('websiteURL', websiteURL);
                        fd.append('target', target);

                        var xhr = new XMLHttpRequest();
                        xhr.open("POST", 'ClientAccountManagementController?target=ArtistProfileUpdate');
                        xhr.onreadystatechange = function () {
                            if (xhr.readyState == 4) {
                                window.location.href = "/redirect.jsp";
                            }
                        };
                        xhr.send(fd);
                    }

                    function cancel() {
                        window.cancel = function (e) {
                            e.wrap('<form>').closest('form').get(0).reset();
                            e.unwrap();
                        }
                        document.getElementById("cropper-area").innerHTML = "<img id='image' src=''>";
                        document.getElementById("change-picture").disabled = true;
                    }

                    function confirm() {
                        canvas = cropper.getCroppedCanvas();
                        dataURL = canvas.toDataURL();
                    }


                    (function ($) {
                        var inputImage = document.getElementById('inputImage');
                        var URL = window.URL || window.webkitURL;
                        var blobURL;

                        if (URL) {
                            inputImage.onchange = function () {
                                var files = this.files;
                                var file, img;

                                if (!cropper) {
                                    cropper = new Cropper(image, {
                                        aspectRatio: 1 / 1,
                                        zoomable: false,
                                        minContainerWidth: 300,
                                        minContainerHeight: 300,
                                        maxContainerWidth: 300,
                                        maxContainerHeight: 300,
                                        minCanvasWidth: 300,
                                        minCanvasHeight: 300,
                                        minCropBoxWidth: 300,
                                        minCropBoxHeight: 300
                                    });
                                }
                                if (cropper && files && files.length) {
                                    file = files[0];
                                    img = new Image();
                                    img.src = URL.createObjectURL(file);
                                    img.onload = function () {
                                        if (this.width >= 300 && this.height >= 300) {
                                            if (/^image\/\w+/.test(file.type)) {
                                                blobURL = URL.createObjectURL(file);
                                                cropper.reset().replace(blobURL);
                                                inputImage.value = null;
                                            } else {
                                                window.alert('Please choose an image file.');
                                            }
                                        } else {
                                            window.alert("Image needs to be at least 300px in width and height.");
                                        }
                                    };
                                }
                            };
                        } else {
                            inputImage.disabled = true;
                            inputImage.parentNode.className += ' disabled';
                        }




                        /**
                         * jQuery.textareaCounter
                         * Version 1.0
                         * Copyright (c) 2011 c.bavota - http://bavotasan.com
                         * Dual licensed under MIT and GPL.
                         * Date: 10/20/2011
                         **/
                        $.fn.textareaCounter = function (options) {
                            // setting the defaults
                            // $("textarea").textareaCounter({ limit: 100 });
                            var defaults = {
                                limit: 100
                            };
                            var options = $.extend(defaults, options);

                            // and the plugin begins
                            return this.each(function () {
                                var obj, text, wordcount, limited;

                                obj = $(this);
                                obj.after('<span style="font-size: 11px; clear: both; margin-top: 3px; display: block;" id="counter-text"></span>');

                                obj.keyup(function () {
                                    text = obj.val();
                                    if (text === "") {
                                        wordcount = 0;
                                    } else {
                                        wordcount = $.trim(text).split(" ").length;
                                    }
                                    if (wordcount > options.limit) {
                                        $("#counter-text").html('<span style="color: #DD0000;">0 words left</span>');
                                        limited = $.trim(text).split(" ", options.limit);
                                        limited = limited.join(" ");
                                        $(this).val(limited);
                                    } else {
                                        $("#counter-text").html((options.limit - wordcount) + ' words left');
                                    }
                                });
                            });
                        };
                    })(jQuery);
                </script>
                <div class="md-modal md-effect-1" id="modal-change-profilepic">
                    <div class="md-content">
                        <h3>Change Profile Picture</h3>
                        <div style="text-align:center;">
                            <input type="file" class="sr-only" id="inputImage" name="file" accept="image/*"/>&nbsp;&nbsp;
                            <button class="md-close" onclick="confirm()" type="button">Confirm and Close</button>&nbsp;&nbsp;
                            <!--button onclick="cancel($('#inputImage'))" type="button">Cancel</button-->
                        </div>
                        <div class="img-container" id="cropper-area" style="max-width:550px;min-height:600px;">
                            <img id="image" src="../img/blank.png">
                        </div>
                    </div>
                </div>

                <div class="md-modal md-effect-1" id="modal-profilePic"><label for="music"><strong>Music * (WAV format, 44.1kHz, 16bit/24bit)</strong></label>
                    <div class="md-content">
                        <h3>Standard Requirement</h3>
                        <div>
                            <p>Profile picture file requirement:</p>
                            <ul>
                                <li><strong>Ratio:</strong> Image must be in a ratio of 1:1</li>
                                <li><strong>Pixel:</strong> Image must be at least 500px x 500px</li>
                            </ul>
                            <div style="text-align:center;">
                                <button class="md-close" type="button">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="md-modal md-effect-1" id="modal-change-email">
                    <div class="md-content">
                        <h3><strong>Email</strong></h3>
                        <div>
                            <p>This email address will be used for:</p>
                            <ul>
                                <li>Logging in to sounds.sg</li>
                                <li>Resetting your password</li>
                                <li>Receiving transaction information</li>
                            </ul>
                            <p>Your email will not be displayed on your biography. Update this field only if you are changing your email. A verification code will be sent to the new email.</p>
                            <br>
                            <div style="text-align:center;">
                                <button class="md-close" type="button">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="md-modal md-effect-1" id="modal-paypal">
                    <div class="md-content">
                        <h3><strong>PayPal Email</strong></h3>
                        <div>
                            <p>Please ensure that the email supplied is linked to your PayPal account. This will be the only means of monetary transaction on sounds.sg.</p>
                            <br>
                            <div style="text-align:center;">
                                <button class="md-close" type="button">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <script>
                    $(document).ready(function () {
                        $("#facebookURL").change(function () {
                            if (!/^(?:ht)tps?\:\/\//.test(this.value)) {
                                this.value = "http://" + this.value;
                            }
                        });
                        $("#twitterURL").change(function () {
                            if (!/^(?:ht)tps?\:\/\//.test(this.value)) {
                                this.value = "http://" + this.value;
                            }
                        });
                        $("#instagramURL").change(function () {
                            if (!/^(?:ht)tps?\:\/\//.test(this.value)) {
                                this.value = "http://" + this.value;
                            }
                        });
                        $("#websiteURL").change(function () {
                            if (!/^(?:ht)tps?\:\/\//.test(this.value)) {
                                this.value = "http://" + this.value;
                            }
                        });
                    });

                    if ($('#paypalEmail').val() == '') {
                        $("html, body").animate({scrollTop: -100}, "slow");
                    }
                </script>

                <%
                    List<Genre> genres = (List<Genre>) (session.getAttribute("listOfGenres"));
                    Account account = (Account) session.getAttribute("account");
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    if (account != null && artist != null && genres != null) {
                        if (!account.getEmailIsVerified()) {
                            out.print("<p class='warning'>Your email address has not been verified. Click here to <a href='#!/verify-email'>enter or resend your verification code</a>.</p>");
                        }
                %>

                <form name="profileForm" class="form" method="POST" enctype="multipart/form-data" action="javascript:submitForm();" accept-charset="utf-8" >
                    <jsp:include page="../jspIncludePages/displayMessage.jsp" />

                    <h2>Account Details</h2>

                    <div class="sidebar main-left main-medium">
                        <div class="widget details-widget">
                            <%if (artist.getImageURL() != null && !artist.getImageURL().isEmpty()) {%>
                            <a href="http://sounds.sg.storage.googleapis.com/<%=artist.getImageURL()%>" title="<%=artist.getName()%>" data-lightbox="lightbox<%=artist.getImageURL()%>">
                                <span class="img">
                                    <img src="http://sounds.sg.storage.googleapis.com/<%=artist.getImageURL()%>" />
                                </span>
                            </a>
                            <%} else {%>
                            <a href="placeholders/artist01.jpg" title="<%=artist.getName()%>" class="thumb-glitch" data-lightbox="lightbox">
                                <span class="img">
                                    <img src="placeholders/artist01.jpg" />
                                </span>
                            </a>
                            <%}%>


                            <div class="details-meta">
                                <ul class="details-list">
                                    <li>
                                        <span class="label">URL</span>
                                        <input type="text" value="http://sounds.sg/music/<%=artist.getName()%>" disabled style="width:100%;"/>
                                    </li>
                                </ul>
                            </div>
                        </div>

                    </div>

                    <div id="main" class="release main-left main-medium">
                        <div class="row clearfix">
                            <div class="col-1-3" style="margin: 0 20px 24px 0;">
                                <label for="name"><strong>Name</strong></label>
                                <input type="text" id="name" name="name" value="<%=account.getName()%>" disabled>
                                <a href="#!/change-name">Change Artist/Band Name</a>
                            </div>

                            <div class="col-1-3" style="margin: 0 0 0 0;">
                                <label for="email"><strong>Email</strong> <a class="md-trigger" data-modal="modal-change-email">(?)</a></label>
                                <input type="email" id="email" name="email" value="<%=account.getEmail()%>">
                                <%if (account.getNewEmail() != null && account.getNewEmail().length() > 0) {%><a href="#!/change-email">Email change in progress...</a><%}%>
                            </div>

                            <div class="col-1-3 last"></div>
                        </div>

                        <div class="row clearfix">
                            <div class="col-1-3" style="margin: 0 20px 24px 0;">
                                <label for="paypalEmail"><strong>PayPal Email Address:</strong> * <a class="md-trigger" data-modal="modal-paypal">(?)</a></label>
                                <input type="email" value="<%if (artist.getPaypalEmail() != null) {
                                        out.print(artist.getPaypalEmail());
                                    }%>" name="paypalEmail" id="paypalEmail" required>
                            </div>

                            <div class="col-1-3" style="margin: 0 0 0 0;">
                                <label>&nbsp;</label>
                                <button type="button" class="invert" style="width: 100%;" onclick="javascript:window.location.href = '#!/change-password'" >Change Password</button>
                            </div>

                            <div class="col-1-3 last"></div>
                        </div>

                        <!--
                    <hr class="divider">

                    <div class="row clearfix">
                        <div class="col-1-3" style="margin: 0 20px 24px 0;">
                            <label for="oldpassword"><strong>Old Password</strong> *</label>
                            <input id="oldpassword" type="password" name="oldpassword" placeholder="Leave blank unless setting a new password">
                        </div>
                        <div class="col-1-3" style="margin: 0 0 0 0;">
                            <label for="password"><strong>New Password</strong> *</label>
                            <input id="password" type="password" name="password" title="Password should contain at least 6 characters" onchange="form.repassword.pattern = this.value;">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-3 last">
                            <label for="repassword"><strong>Re-enter Password</strong> *</label>
                            <input id="repassword" type="password" name="repassword">
                        </div>
                    </div> -->

                    </div>

                    <hr class="divider">

                    <h2>Account Profile</h2>

                    <div class="row clearfix">
                        <div class="col-1-3">
                            <label for="genre"><strong>Genre</strong> *</label>
                            <select name="genre" id="genre" style="width: 100%; height:38px;" required>
                                <option value="">Select</option>
                                <%
                                    for (int i = 0; i < genres.size(); i++) {
                                        if (artist.getGenre() != null && artist.getGenre().getId().equals(genres.get(i).getId())) {
                                            out.write("<option selected value='" + genres.get(i).getId() + "'>" + genres.get(i).getName() + "</option>");
                                        } else {
                                            out.write("<option value='" + genres.get(i).getId() + "'>" + genres.get(i).getName() + "</option>");
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <div class="col-1-3"> 
                            <label for="contactEmail"><strong>Contact Email</strong></label>
                            <input type="email" id="contactEmail" name="contactEmail" value="<%if (artist.getContactEmail() != null) {
                                    out.print(artist.getContactEmail());
                                }%>">
                        </div>

                        <div class="col-1-3 last">  
                            <label><strong>Change Profile Picture</strong> <a class="md-trigger" data-modal="modal-profilePic">(?)</a></label>
                            <button type="button" class="md-trigger medium invert" data-modal="modal-change-profilepic" id="change-picture">Change Profile Picture</button>&nbsp;
                        </div>
                    </div>

                    <%if (artist.getIsBand()) {%>
                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="dateFormed"><strong>Date Formed</strong> </label>

                            <select name="dateFormed" id="dateFormed" style="width: 100%; height:42px;"></select>
                            <script>
                                var start = 1900;
                                var end = new Date().getFullYear();
                                var options = "";
                                var previouslySelectedDate = <%
                                    String date = "";
                                    if (artist.getBandDateFormed() != null) {
                                        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy");
                                        date = DATE_FORMAT.format(artist.getBandDateFormed());
                                        out.print(date);
                                    } else {
                                        out.print("\"\"");
                                    }
                                %>;
                                for (var year = end; year >= start; year--) {
                                    if (year === previouslySelectedDate) {
                                        options += "<option selected value='" + year + "-01-01'>" + year + "</option>";
                                    } else {
                                        options += "<option value='" + year + "-01-01'>" + year + "</option>";
                                    }
                                }
                                document.getElementById("dateFormed").innerHTML = options;
                            </script>

                        </div>
                        <div class="col-1-2 last">
                            <label for="bandMembers"><strong>Members</strong> (separated with commas)</label>
                            <input type="text" placeholder="e.g. daniel, john" id="bandMembers" name="bandMembers" value="<%if (artist.getBandMembers() != null) {
                                    out.print(artist.getBandMembers());
                                }%>">
                        </div>
                    </div>
                    <%}%>

                    <div class="row clearfix">
                        <div class="col-1-2" style="margin-bottom: 24px;">
                            <label for="bio"><strong>About</strong> </label>
                            <textarea name="bio" id="bio" style="min-height:120px;"><%if (artist.getBiography() != null) {
                                    out.print(artist.getBiography());
                                } %></textarea>
                            <script type="text/javascript">
                                $("#bio").textareaCounter({limit: 200});
                            </script>
                        </div>

                        <div class="col-1-2 last">
                            <label for="influences"><strong>Influences</strong> </label>
                            <textarea name="influences" id="influences" style="min-height:120px;"><%if (artist.getBiography() != null) {
                                    out.print(artist.getInfluences());
                                } %></textarea>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="facebookURL"><strong>Facebook URL</strong></label>
                            <input type="url" id="facebookURL" name="facebookURL" placeholder="http://" value="<%if (artist.getFacebookURL() != null) {
                                    out.print(artist.getFacebookURL());
                                }%>">
                        </div>

                        <div class="col-1-2 last">
                            <label for="twitterURL"><strong>Twitter URL</strong></label>
                            <input type="url" id="twitterURL" name="twitterURL" placeholder="http://" value="<%if (artist.getTwitterURL() != null) {
                                    out.print(artist.getTwitterURL());
                                }%>">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="instagramURL"><strong>Instagram URL</strong></label>  
                            <input type="url" id="instagramURL" name="instagramURL" placeholder="http://" value="<%if (artist.getInstagramURL() != null) {
                                    out.print(artist.getInstagramURL());
                                }%>">
                        </div>

                        <div class="col-1-2 last">
                            <label for="websiteURL"><strong>Website</strong></label>
                            <input type="url" id="websiteURL" name="websiteURL" placeholder="http://" value="<%if (artist.getWebsiteURL() != null) {
                                    out.print(artist.getWebsiteURL());
                                }%>">
                        </div>
                    </div>

                    <button type="submit" class="medium invert" id="submitBtn">Submit</button>
                    <input type="hidden" value="ArtistProfileUpdate" name="target" id="target">
                    <div class="clear"></div>
                </form>

                <%} else {%>
                <p class="warning" id="errMsg">Your Session has timed out. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
                <div class="md-overlay"></div>
                <script src="js/classie.js"></script>
                <script src="js/modalEffects.js"></script>
            </article>
        </div>
    </section>
</section>
