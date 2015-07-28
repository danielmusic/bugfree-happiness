<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Profile Page">
    <%@page import="EntityManager.Member"%>
    <%@page import="java.util.List"%>
    <%@page import="EntityManager.Account"%>
    <%@page import="java.text.SimpleDateFormat"%>
    <script>
        function checkAllTracks(source) {
            checkboxes = document.getElementsByName('deleteTrack');
            for (var i = 0, n = checkboxes.length; i < n; i++) {
                checkboxes[i].checked = source.checked;
            }
        }
        function checkAllAlbums(source) {
            checkboxes = document.getElementsByName('deleteAlbum');
            for (var i = 0, n = checkboxes.length; i < n; i++) {
                checkboxes[i].checked = source.checked;
            }
        }

        function removeTrack() {
            alert("removeTrack");
            checkboxes = document.getElementsByName('deleteTrack');
            var arr = new Array();
            $("input:checkbox[name=deleteTrack]:checked").each(function () {
                arr.push($(this).val());
            });
            var stringArr = "";
            for (var i = 0; i < arr.length; i++) {
                alert(arr[i]);
                if (i != (arr.length - 1)) {
                    stringArr += arr[i] + ",";
                } else {
                    stringArr += arr[i];
                    alert("strArr" + stringArr);
                }
            }

            var numOfTicks = 0;
            for (var i = 0, n = checkboxes.length; i < n; i++) {
                if (checkboxes[i].checked) {
                    numOfTicks++;
                }
            }
            if (checkboxes.length == 0 || numOfTicks == 0) {
                window.event.returnValue = true;
                //window.location.href = "./MusicManagementController?target=RemoveTrackFromShoppingCart";
            } else {
                url = "./MusicManagementController?target=RemoveTrackFromShoppingCart";
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    data: {'deleteTrack': stringArr},
                    success: function (val) {
                        window.event.returnValue = false;
                        window.location.href = "#!/redirect";
                    },
                    error: function (xhr, status, error) {
                        document.getElementById("errMsg").style.display = "block";
                        document.getElementById('errMsg').innerHTML = error;
                        hideLoader();
                        ajaxResultsError(xhr, status, error);
                    }
                });
            }
        }
        function removeAlbum() {
            checkboxes = document.getElementsByName('deleteAlbum');
            var arr = new Array();
            $("input:checkbox[name=deleteAlbum]:checked").each(function () {
                arr.push($(this).val());
            });
            var stringArr = "";
            for (var i = 0; i < arr.length; i++) {
                alert(arr[i]);
                if (i != (arr.length - 1)) {
                    stringArr += arr[i] + ",";
                } else {
                    stringArr += arr[i];
                    alert("strArr" + stringArr);
                }
            }


            var numOfTicks = 0;
            for (var i = 0, n = checkboxes.length; i < n; i++) {
                if (checkboxes[i].checked) {
                    numOfTicks++;
                }
            }
            if (checkboxes.length == 0 || numOfTicks == 0) {
                window.event.returnValue = true;
                //window.location.href = "./MusicManagementController?target=RemoveAlbumFromShoppingCart";
            } else {
                url = "./MusicManagementController?target=RemoveAlbumFromShoppingCart";
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    data: {'deleteAlbum': stringArr},
                    success: function (val) {
                        window.event.returnValue = false;
                        window.location.href = "#!/redirect";
                    },
                    error: function (xhr, status, error) {
                        document.getElementById("errMsg").style.display = "block";
                        document.getElementById('errMsg').innerHTML = error;
                        hideLoader();
                        ajaxResultsError(xhr, status, error);
                    }
                });
            }
        }

        function login() {
            url = "./ClientAccountManagementController?target=SaveCartAndLogin";
            $.ajax({
                type: "GET",
                async: false,
                url: url,
                data: {},
                success: function (val) {
                    window.event.returnValue = false;
                    window.location.href = "#!/login";
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
    <section class="content section">
        <div class="container">
            <article>
                <div class="md-modal md-effect-1" id="modal-name">
                    <div class="md-content">
                        <h3>Modal Dialog</h3>
                        <div>
                            <p>This is a modal window. You can do the following things with it:</p>
                            <ul>
                                <li><strong>Read:</strong> modal windows will probably tell you something important so don't forget to read what they say.</li>
                                <li><strong>Look:</strong> a modal window enjoys a certain kind of attention; just look at it and appreciate its presence.</li>
                                <li><strong>Close:</strong> click on the button below to close the modal.</li>
                            </ul>
                            <div style="text-align:center;">
                                <button class="md-close" type="button">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="md-modal md-effect-1" id="modal-change-email">
                    <div class="md-content">
                        <h3>Account Email</h3>
                        <div>
                            <p>This is the email address that you will use to:</p>
                            <ul>
                                <li><strong>Login:</strong> into Sounds.SG</li>
                                <li><strong>Reset:</strong> your password if the need arises</li>
                                <li><strong>Receive:</strong> invoices about your purchases on Sounds.SG</li>
                            </ul>
                            <p>Update this field only if you are changing your email. A verification code will be sent to the new email.</p>
                            <div style="text-align:center;">
                                <button class="md-close" type="button">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="md-modal md-effect-1" id="modal-paypal">
                    <div class="md-content">
                        <h3>Payment Details</h3>
                        <div>
                            <p>When a fan buys your music, the money will go directly to the above address. Please also be sure to follow the instructions on the Sell Your Music on ?? page!:</p>
                            <div style="text-align:center;">
                                <button class="md-close" type="button">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <%
                    Account account = (Account) session.getAttribute("account");
                    Member member = (Member) (session.getAttribute("member"));
                    if (account != null && member != null) {
                        if (!account.getEmailIsVerified()) {
                            out.print("<p class='warning'>Your email address has not been verified. Click here to <a href='#!/verify-email'>resend verification code</a>.</p>");
                        }
                %>

                <form action="ClientAccountManagementController" class="form" method="POST" enctype="multipart/form-data">
                    <jsp:include page="../jspIncludePages/displayMessage.jsp" />
                    <h2>My Past Purchases</h2>
                    <hr class="divider">
                    <form name="cartManagement">
                        <h2>Tracks</h2>
                        <table class="layout display responsive-table">
                            <thead>
                                <tr>
                                    <th class="product-remove" style="width: 5%">
                                        <input type="checkbox" onclick="checkAllTracks(this);" />
                                    </th>     
                                    <th style="width: 30%">Track Name</th>
                                    <th style="width: 30%">Album Name</th>
                                    <th style="width: 25%">Artist Name</th>
                                    <th style="width: 10%">Price</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="deleteTrack" value="" />
                                    </td>
                                    <td class="table-date">
                                    </td>
                                    <td class="table-name">
                                    </td>
                                    <td>
                                    </td>
                                    <td>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <button class="medium invert" onclick="removeTrack()">Remove track(s)</button>
                        <hr class="divider2" style="margin-right: 0px;">

                        <h2>Albums</h2>
                        <table class="layout display responsive-table">
                            <thead>
                                <tr>
                                    <th class="product-remove" style="width: 5%">
                                        <input type="checkbox" onclick="checkAllAlbums(this)" />
                                    </th>     
                                    <th style="width: 30%">Album Name</th>
                                    <th style="width: 30%">Artist Name</th>
                                    <th style="width: 35%">Price</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="deleteAlbum" value="" />
                                    </td>
                                    <td class="table-date">
                                    </td>
                                    <td>
                                    </td>
                                    <td>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <button class="medium invert" onclick="">Remove album(s)</button>
                        <hr class="divider2" style="margin-right: 0px;">
                    </form>

                    <hr class="divider">

                    <h2>Account Profile</h2>

                    <div class="row clearfix">

                        <div class="col-1-3">
                            <label for="genre"><strong>Genre</strong> *</label>
                            <select name="genre" id="genre" style="width: 100%; height:40px;" required>
                                <option value="">Select</option>

                            </select>
                        </div>

                        <div class="col-1-3"> 
                            <label for="contactEmail"><strong>Contact Email</strong></label>
                            <input type="email" id="contactEmail" name="contactEmail" value="">
                        </div>

                        <div class="col-1-3 last">  
                            <label for="pic"><strong>Change Profile Picture</strong></label>
                            <input type="file" id="pic" name="picture" style="height: 40px;padding-top: 8px;padding-bottom: 8px;">
                        </div>
                    </div>


                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="dateFormed"><strong>Date Formed</strong> </label>

                            <input type="date" id="dateFormed" name="dateFormed" value="">
                        </div>
                        <div class="col-1-2 last">
                            <label for="bandMembers"><strong>Members</strong> </label>
                            <input type="text" id="bandMembers" name="bandMembers" value="">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2" style="margin-bottom: 24px;">
                            <label for="bio"><strong>Biography</strong> </label>
                            <textarea name="bio" id="bio" style="min-height:120px;"></textarea>
                            <label>200 words max</label>
                        </div>

                        <div class="col-1-2 last">
                            <label for="influences"><strong>Influences</strong> </label>
                            <textarea name="influences" id="influences" style="min-height:120px;"></textarea>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="fb"><strong>Facebook URL</strong></label>
                            <input type="url" id="fb" name="facebookURL" placeholder="http://" value="">
                        </div>

                        <div class="col-1-2 last">
                            <label for="twitter"><strong>Twitter URL</strong></label>
                            <input type="url" id="twitter" name="twitterURL" placeholder="http://" value="">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="ig"><strong>Instagram URL</strong></label>  
                            <input type="url" id="ig" name="instagramURL" placeholder="http://" value="">
                        </div>

                        <div class="col-1-2 last">
                            <label for="websiteURL"><strong>Website</strong></label>
                            <input type="url" id="websiteURL" name="websiteURL" placeholder="http://" value="">
                        </div>
                    </div>

                    <input type="submit" value="Save" class="small invert">
                    <input type="hidden" value="ArtistProfileUpdate" name="target">
                    <div class="clear"></div>
                </form>

                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
                <div class="md-overlay"></div><!-- the overlay element -->
                <script src="js/classie.js"></script>
                <script src="js/modalEffects.js"></script>
                <script src="js/cssParser.js"></script>
            </article>
        </div>
    </section>
</section>
