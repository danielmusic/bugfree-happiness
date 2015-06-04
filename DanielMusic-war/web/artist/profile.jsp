<%@page import="EntityManager.Artist"%>

<section id="page" data-title="Artist Profile">
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/about-bg.jpg)">
        <h1 class="heading-l">Artist Profile Page</h1>
        <span class="overlay dots"></span>
    </section>
    <section class="content section">
        <div class="container">
            <article>
                <%
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    System.out.print(">>>>>>>>>>>>" + artist);
                    if (artist != null) {
                %>
                <h1>Hello world</h1>
                <%} else {%>
                <h1>Bye world</h1>
                <%}%>
            </article>
        </div>
    </section>
</section>


