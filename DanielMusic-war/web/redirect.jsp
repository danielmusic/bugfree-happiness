<!-- ############################# Ajax Page Container ############################# -->
<section id="page">
    <div class="container">
        <%
            String nextPage = (String) session.getAttribute("redirectPage");
            session.removeAttribute("redirectPage");
            if (nextPage != null) {
        %>
        <script>
            (function () {
                window.location.href = "/<%=nextPage%>";
            })();
        </script>
        <%} else {%>
        <script>
            (function () {
                window.location.href = "/#!/index";
            })();
        </script>
        <%}%>
    </div>
</section>