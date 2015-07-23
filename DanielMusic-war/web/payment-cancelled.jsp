<!DOCTYPE html>
<%session.setAttribute("redirectPage","http://localhost:8080/DanielMusic-war/#!/checkout");%>
<%session.setAttribute("errMsg","Your checkout is not yet complete, click on the Pay Now button to do so.");%>
<html>
<head>
<title></title>
<script>
if (window != top) {
top.location.replace("http://localhost:8080/DanielMusic-war/redirect.jsp");
}
</script>
</head>
<body>
</body>
</html>