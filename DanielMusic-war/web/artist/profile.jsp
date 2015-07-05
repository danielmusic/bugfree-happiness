<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Profile Page">
    <%@page import="EntityManager.Genre"%>
    <%@page import="java.util.List"%>
    <%@page import="EntityManager.Artist"%>
    <%@page import="EntityManager.Account"%>
    <%@page import="java.text.SimpleDateFormat"%>
    <section class="content section">
        <div class="container">
            <article>
                <%
                    List<Genre> genres = (List<Genre>) (session.getAttribute("genres"));
                    Account account = (Account) session.getAttribute("account");
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    if (account != null && artist != null && genres != null) {
                        if (!account.getEmailIsVerified()) {
                            out.print("<p class='warning'>Your email address has not been verified. Click here to <a href='#!/verify-email'>resend verification code</a>.</p>");
                        }
                %>

                <form action="ClientAccountManagementController" class="form" method="POST" enctype="multipart/form-data">
                    <jsp:include page="../jspIncludePages/displayMessage.jsp" />
                    <h2>Account Details</h2>

                    <div class="row clearfix">
                        <div class="col-1-3">
                            <label for="name"><strong>Name</strong> <a class="md-trigger" data-modal="modal-name">(?)</a></label>
                            <input type="text" id="name" name="name" value="<%=account.getName()%>" disabled>
                            <a href="#!/change-name">Change Name</a>
                        </div>

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

                        <div class="col-1-3">
                            <label for="email"><strong>Current Email</strong> <a class="md-trigger" data-modal="modal-change-email">(?)</a></label>
                            <input type="email" id="email" name="email" value="<%=account.getEmail()%>">
                            <%if (account.getNewEmail() != null && account.getNewEmail().length() > 0) {%><a href="#!/change-email">Email change in progress...</a><%}%>
                        </div>

                        <div class="col-1-3 last">
                            <label for="ppEmail"><strong>PayPay email address:</strong> * <a class="md-trigger" data-modal="modal-paypal">(?)</a></label>
                            <input type="email" value="<%if (artist.getPaypalEmail() != null) {
                                    out.print(artist.getPaypalEmail());
                                }%>" name="paypalEmail" id="ppEmail" required>
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
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-3">
                            <label for="oldpassword"><strong>Old Password</strong> *</label>
                            <input id="oldpassword" type="password" name="oldpassword" placeholder="Leave blank unless setting a new password">
                        </div>
                        <div class="col-1-3">
                            <label for="password"><strong>New Password</strong> *</label>
                            <input id="password" type="password" name="password"  title="Password must contain at least 6 characters, including UPPER/lowercase and numbers" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}"  onchange="form.repassword.pattern = this.value;">
                        </div>
                        <div class="col-1-3 last">
                            <label for="repassword"><strong>Re-enter Password</strong> *</label>
                            <input id="repassword" type="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}" name="repassword">
                        </div>
                    </div>

                    <hr class="divider">

                    <h2>Account Profile</h2>

                    <div class="row clearfix">

                        <div class="col-1-3">
                            <label for="genre"><strong>Genre</strong> *</label>
                            <select name="genre" id="genre" style="width: 100%; height:40px;" required>
                                <option value="">Select</option>
                                <%
                                    for (int i = 0; i < genres.size(); i++) {
                                        if (artist.getGenre() != null && artist.getGenre().getId() == genres.get(i).getId()) {
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
                            <label for="pic"><strong>Change Profile Picture</strong></label>
                            <input type="file" id="pic" name="picture" style="height: 40px;padding-top: 8px;padding-bottom: 8px;">
                        </div>
                    </div>

                    <%if (artist.getIsBand()) {%>
                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="dateFormed"><strong>Date Formed</strong> </label>
                            <%
                                String date = "";
                                if (artist.getBandDateFormed() != null) {
                                    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
                                    date = DATE_FORMAT.format(artist.getBandDateFormed());
                                }
                            %>
                            <input type="date" id="dateFormed" name="dateFormed" value="<%=date%>">
                        </div>
                        <div class="col-1-2 last">
                            <label for="bandMembers"><strong>Members</strong> </label>
                            <input type="text" id="bandMembers" name="bandMembers" value="<%if (artist.getBandMembers() != null) {
                                           out.print(artist.getBandMembers());
                                       }%>">
                        </div>
                    </div>
                    <%}%>

                    <div class="row clearfix">
                        <div class="col-1-2" style="margin-bottom: 24px;">
                            <label for="bio"><strong>Biography</strong> </label>
                            <textarea name="bio" id="bio" style="min-height:120px;"><%if (artist.getBiography() != null) {
                                    out.print(artist.getBiography());
                                } %></textarea>
                            <label>200 words max</label>
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
                            <label for="fb"><strong>Facebook URL</strong></label>
                            <input type="url" id="fb" name="facebookURL" placeholder="http://" value="<%if (artist.getFacebookURL() != null) {
                                    out.print(artist.getFacebookURL());
                                }%>">
                        </div>

                        <div class="col-1-2 last">
                            <label for="twitter"><strong>Twitter URL</strong></label>
                            <input type="url" id="twitter" name="twitterURL" placeholder="http://" value="<%if (artist.getTwitterURL() != null) {
                                    out.print(artist.getTwitterURL());
                                }%>">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="ig"><strong>Instagram URL</strong></label>
                            <input type="url" id="ig" name="instagramURL" placeholder="http://" value="<%if (artist.getInstagramURL() != null) {
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

                    <input type="submit" value="Save" class="small invert">
                    <input type="hidden" value="ArtistProfileUpdate" name="target">
                    <input type="hidden" value="ArtistProfileUpdate" name="target">
                    <div class="clear"></div>
                </form>

                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>

            <div class="md-overlay"></div>

            <script src="js/classie.js"></script>
            <script src="js/modalEffects.js"></script>
            <script>var polyfilter_scriptpath = '/js/';</script> 
            <script src="js/cssParser.js"></script>
            <script src="js/css-filters-polyfill.js"></script>
        </div>
    </section>
</section>
