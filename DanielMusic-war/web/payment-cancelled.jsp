<!DOCTYPE html>
<%session.setAttribute("redirectPage","#!/checkout");%>
<%session.setAttribute("errMsg","Your checkout is not yet complete, click on the Pay Now button to do so.");%>
<html>
<head>
<title></title>
<script>
if (window != top) {
top.location.replace("/DanielMusic-war/redirect.jsp");
}
</script>
</head>
<body>
</body>
</html>