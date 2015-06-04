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
                            <input type="text" id="name" name="name" disabled>
                        </div>

                        <div class="col-1-3">
                            <label for="email"><strong>Email address</strong> </label>
                            <input type="email" id="email" name="email">
                        </div>

                        <div class="col-1-3 last">
                            <label for="paypal" class="tip"><strong>PayPay email address <a class="tip">(?)</a></strong></label>
                            <input type="text" value="" id="paypal" disabled>
                            <div class="tip-content hidden">
                                <span>Animated ToolTip</span>
                                Phasellus ligula sem, laoreet luctus luctus sed, pharetra in mi. Aenean accumsan gravida convallis.
                            </div>
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
                            <textarea name="bio" id="bio" cols="88" rows="6" required></textarea>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="fb"><strong>Facebook URL</strong></label>
                            <input type="url" id="fb" name="fb" placeholder="http://">
                        </div>

                        <div class="col-1-2 last">
                            <label for="fb"><strong>Twitter URL</strong></label>
                            <input type="url" id="fb" name="fb" placeholder="http://">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="ig"><strong>Instagram URL</strong></label>
                            <input type="url" id="ig" name="ig" placeholder="http://">
                        </div>

                        <div class="col-1-2 last">
                            <label for="ws"><strong>Website</strong></label>
                            <input type="url" id="ws" name="ws" placeholder="http://">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="pic"><strong>Profile Picture</strong></label>
                            <input type="file" id="pic">
                        </div>
                    </div>


                    <input type="submit" value="Save" class="large invert">
                    <input type="hidden" value="ArtistSignup" name="target">
                    <div class="clear"></div>
                </form>

                <%} else {%>
                <h1>Bye world</h1>
                <%}%>
            </article>
        </div>
    </section>
</section>
