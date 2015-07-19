<script>
    function closeWindow() {
        var parent_window = window.opener;
         parent_window.location.href("#!/index");
         window.close();
    }
</script>
<body onload="closeWindow()"></body>