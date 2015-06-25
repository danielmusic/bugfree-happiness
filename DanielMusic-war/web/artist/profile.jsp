<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Profile Page">

    <section class="content section">
        <div class="container">
            <article>
                <%@page import="EntityManager.Genre"%>
                <%@page import="java.util.List"%>
                <%@page import="EntityManager.Artist"%>
                <%
                    List<Genre> genres = (List<Genre>) (session.getAttribute("genres"));
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    if (artist != null && genres != null) {
                %>

                <form action="../ClientAccountManagementController" class="form" method="POST" enctype="multipart/form-data">
                    <jsp:include page="../jspIncludePages/displayMessage.jsp" />
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

                        <div class="col-1-3">
                            <label for="genre"><strong>Genre</strong> *</label>
                            <select name="genre" id="genre" style="width: 100%; height:40px;">
                                <option value="">Select</option>
                                <%
                                    for (int i = 0; i < genres.size(); i++) {
                                        out.write("<option value='" + genres.get(i).getId() + "'>" + genres.get(i).getName() + "</option>");
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
                            <label for="pic"><strong>Profile Picture</strong></label>
                            <input type="file" id="pic" name="picture" style="height: 40px;padding-top: 8px;padding-bottom: 8px;">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="bio"><strong>Biography</strong> </label>
                            <textarea name="bio" id="bio" required style="min-height:120px;"><%if (artist.getBiography() != null) {
                                    out.print(artist.getBiography());
                                } %></textarea>
                            <label><strong>200 words max</strong> </label>
                        </div>

                        <div class="col-1-2 last">
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

                    <input type="submit" value="Save" class="small invert">
                    <input type="hidden" value="ArtistProfileUpdate" name="target">
                    <div class="clear"></div>
                </form>

                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>
        </div>
    </section>
</section>
