<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Profile Page">

    <section class="content section">
        <div class="container">
            <article>
                <%@page import="EntityManager.Band"%>
                <%
                    Band band = (Band) (session.getAttribute("band"));
                    if (band != null) {
                %>
                <form action="../ClientAccountManagementController" class="form">
                    <h2>Account Details</h2>

                    <div class="row clearfix">
                        <div class="col-1-3">
                            <label for="name"><strong>Name</strong> </label>
                            <input type="text" id="name" name="name" value="<%=band.getName()%>" disabled>
                        </div>
                        <div class="col-1-3">
                            <label for="email"><strong>Email address</strong> </label>
                            <input type="email" id="email" name="email" value="<%=band.getEmail()%>">
                        </div>
                        <div class="col-1-3 last">
                            <label for="ppEmail"><strong>PayPay email address:</strong></label>
                            <input type="text" value="<%=band.getEmail()%>" name="paypalEmail" id="ppEmail">
                        </div>
                    </div>
                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="password"><strong>Password</strong> *  (Leave blank unless setting a new password)</label>
                            <input id="password" type="password" title="Password must contain at least 6 characters, including UPPER/lowercase and numbers" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}"  name="pwd" onchange="form.repassword.pattern = this.value;">
                        </div>
                        <div class="col-1-2 last">
                            <label for="repassword"><strong>Re-enter Password</strong> *</label>
                            <input id="repassword" type="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}" name="repassword">
                        </div>
                    </div>

                    <hr class="divider">

                    <h2>Band Profile</h2>
                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="name"><strong>Date formed</strong> </label>
                            <input type="text" id="name" name="name" value="<%=band.getName()%>" disabled>
                        </div>

                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="bio"><strong>Biography</strong> </label>
                            <textarea name="bio" id="bio" required><%if (band.getBiography() != null) {
                                    out.print(band.getBiography());
                                }%></textarea>
                            <label><strong>200 words max</strong> </label>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="name"><strong>Members</strong> </label>
                            <textarea name="members" id="members" style="min-height:50px;" required><%if (band.getMembers() != null) {
                                    out.print(band.getMembers());
                                }%></textarea>
                        </div>
                        <div class="col-1-2 last">
                            <label for="influences"><strong>Influences</strong> </label>
                            <textarea name="influences" id="influences" style="min-height:50px;" required><%if (band.getMembers() != null) {
                                    out.print(band.getInfluences());
                                }%></textarea>
                        </div>

                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="fb"><strong>Facebook URL</strong></label>
                            <input type="url" id="fb" name="facebookURL" placeholder="http://" value="<%if (band.getFacebookURL() != null) {
                                    out.print(band.getFacebookURL());
                                }%>">
                        </div>

                        <div class="col-1-2 last">
                            <label for="twitter"><strong>Twitter URL</strong></label>
                            <input type="url" id="twitter" name="twitterURL" placeholder="http://" value="<%if (band.getTwitterURL() != null) {
                                    out.print(band.getTwitterURL());
                                }%>">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="ig"><strong>Instagram URL</strong></label>
                            <input type="url" id="ig" name="instagramURL" placeholder="http://" value="<%if (band.getInstagramURL() != null) {
                                    out.print(band.getInstagramURL());
                                }%>">
                        </div>

                        <div class="col-1-2 last">
                            <label for="ws"><strong>Website</strong></label>
                            <input type="url" id="ws" name="ws" placeholder="http://">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="pic"><strong>Profile Picture</strong></label>
                            <input type="file" id="pic" name="profilePicURL">
                        </div>
                    </div>

                    <input type="submit" value="Save" class="large invert">
                    <input type="hidden" value="ArtistUpdateProfile" name="target">
                    <div class="clear"></div>
                </form>

                <%} else {%>
                <h1>Bye world</h1>
                <%}%>
            </article>
        </div>
    </section>
</section>
