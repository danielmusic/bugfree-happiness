<section id="page" data-title="Change Artist/Band Name">
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <h1 class="heading-l">Change Artist/Band Name</h1>
        <span class="overlay dots"></span>
    </section>
    <section class="content section">
        <div class="container">
            <%@page import="EntityManager.Account"%>
            <%
                Account account = (Account) session.getAttribute("account");
                if (account != null) {
            %>


            <article>
                <form class="form" name="resetPasswordForm" action="ClientAccountManagementController">
                    <jsp:include page="jspIncludePages/displayMessage.jsp" />
                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="name"><strong>New Name</strong>*</label>
                            <input name="name" id="name" value="<%=account.getName()%>" required>
                        </div>
                        <div class="col-1-2 last">
                            <label for="email"><strong>Notice</strong></label>
                            <p>Changing your artist/band name requires your account to be re-approved again by our staffs. While your account is pending approval, your existing profile page and albums listing will be hidden on our website. Your already submitted or published albums, singles and tracks will not be updated. </p>
                        </div>
                    </div>
                    <input type="hidden" value="ChangeName" name="target">
                    <button type="submit" class="medium invert">Change Name</button>
                    <button type="button" class="medium invert" onclick="window.location.href = '#!/artist/profile'">Back to Home</button>
                    <div class="clear"></div>
                </form>
            </article>
            <%} else {%>
            <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
            <%}%>
        </div>
    </section>
</section>

