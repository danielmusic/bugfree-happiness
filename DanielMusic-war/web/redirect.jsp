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
                window.location.href = "/DanielMusic-war/<%=nextPage%>";
            })();
        </script>
        <%
        } else {
        %>
        <script>
            (function () {
                window.location.href = "/DanielMusic-war/#!/index";
            })();
        </script>
        <%
            }
        %>
    </div>
</section>