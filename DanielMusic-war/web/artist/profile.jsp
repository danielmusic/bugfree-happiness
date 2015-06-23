<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Profile Page">

    <section class="content section">
        <div class="container">
            <article>
                <%@page import="EntityManager.Artist"%>
                <%
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    if (artist != null) {
                %>

                <form action="../ClientAccountManagementController" class="form" method="POST" enctype="multipart/form-data">
                    <h2>Account Details</h2>

                    <div class="row clearfix">
                        <div class="col-1-3">
                            <label for="name"><strong>Name</strong> (?)</label>
                            <input type="text" id="name" name="name" value="<%=artist.getName()%>">
                        </div>
                        <div class="col-1-3">
                            <label for="email"><strong>Email address</strong> </label>
                            <input type="email" id="email" name="email" value="<%=artist.getEmail()%>">
                        </div>

                        <div class="col-1-3 last">
                            <label for="ppEmail"><strong>PayPay email address:</strong> (?)</label>
                            <input type="text" value="<%=artist.getEmail()%>" name="paypalEmail" id="ppEmail">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-3">
                            <label for="oldpassword"><strong>Old Password</strong> *</label>
                            <input id="oldpassword" type="password" name="oldpassword" placeholder="Leave blank unless setting a new password">
                        </div>
                        <div class="col-1-3">
                            <label for="password"><strong>New Password</strong> *</label>
                            <input id="password" type="password" title="Password must contain at least 6 characters, including UPPER/lowercase and numbers" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}"  name="pwd" onchange="form.repassword.pattern = this.value;">
                        </div>
                        <div class="col-1-3 last">
                            <label for="repassword"><strong>Re-enter Password</strong> *</label>
                            <input id="repassword" type="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}" name="repassword">
                        </div>
                    </div>

                    <hr class="divider">

                    <h2>Artist Profile</h2>
                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="bio"><strong>Biography</strong> </label>
                            <textarea name="bio" id="bio" required><%if (artist.getBiography() != null) {
                                    out.print(artist.getBiography());
                                } %></textarea>
                            <label><strong>200 words max</strong> </label>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="genre"><strong>Genre</strong></label>
                            <select name="genre" id="genre">
                                <option value="">Select</option>
                                <option value="volvo">Volvo</option>
                                <option value="saab">Saab</option>
                                <option value="opel">Opel</option>
                                <option value="audi">Audi</option>
                            </select>
                        </div>
                    </div>


                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="influences"><strong>Influences</strong> </label>
                            <textarea name="influences" id="influences" required style="min-height:120px;"><%if (artist.getBiography() != null) {
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
                            <label for="ws"><strong>Website</strong></label>
                            <input type="url" id="ws" name="ws" placeholder="http://">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="pic"><strong>Profile Picture</strong></label>
                            <input type="file" id="pic" name="picture">
                        </div>
                    </div>

                    <input type="submit" value="Save" class="large invert">
                    <input type="hidden" value="ArtistProfileUpdate" name="target">
                    <div class="clear"></div>
                </form>

                <%} else {%>
                <h1>Bye world</h1>
                <%}%>
            </article>
        </div>
    </section>
</section>
