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
                window.location.href = "http://localhost:8080/DanielMusic-war/<%=nextPage%>";
            })();
        </script>
        <%
        } else {
        %>
        <script>
            (function () {
                window.location.href = "http://localhost:8080/DanielMusic-war/#!/index";
            })();
        </script>
        <%
            }
        %>
    </div>
</section>