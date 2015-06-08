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
                <form action="../ClientAccountManagementController" class="form">
                    <h2>Account Details</h2>
                    <div class="row clearfix">
                        <div class="col-1-3">
                            <label for="name"><strong>Artist / band name</strong> </label>
                            <input type="text" id="name" name="name" value="<%=artist.getName()%>" disabled>
                        </div>

                        <div class="col-1-3">
                            <label for="email"><strong>Email address</strong> </label>
                            <input type="email" id="email" name="email" value="<%=artist.getEmail()%>">
                        </div>

                        <div class="col-1-3 last">
                            <label class="tip"><strong>PayPay email address <a href="#!/artist" class="thumb thumb-slide mediabox tip">(?)</a></strong></label>
                            <input type="text" value="<%=artist.getEmail()%>" disabled>
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

                    <h2>Artist Profile</h2>
                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="bio"><strong>Biography</strong> </label>
                            <textarea name="bio" id="bio" cols="88" rows="6" required><%=artist.getDescription()%></textarea>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="fb"><strong>Facebook URL</strong></label>
                            <input type="url" id="fb" name="fb" placeholder="http://" value="<%if (artist.getFacebookURL() != null) {
                                    out.print(artist.getFacebookURL());
                                }%>">
                        </div>

                        <div class="col-1-2 last">
                            <label for="fb"><strong>Twitter URL</strong></label>
                            <input type="url" id="fb" name="fb" placeholder="http://" value="<%if (artist.getTwitterURL() != null) {
                                    out.print(artist.getTwitterURL());
                                }%>">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="ig"><strong>Instagram URL</strong></label>
                            <input type="url" id="ig" name="ig" placeholder="http://" value="<%if (artist.getInstagramURL() != null) {
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
