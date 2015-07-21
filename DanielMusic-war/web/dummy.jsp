<!-- ############################# Ajax Page Container ############################# -->
<section id="page">
    <%
        String nextPage = (String) session.getAttribute("redirectPage");
        if (nextPage != null) {
    %>
    <script>
        window.location.href = <%=nextPage%>;
    </script>
    <%
    } else {
    %>
    <script>
        window.location.href = "#!/index";
    </script>
    <%
        }
    %>
</section>