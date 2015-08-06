<!DOCTYPE html>
<%//set a session to redirect example below:
    //session.setAttribute("redirectPage","http://localhost:8080/DanielMusic-war/#!/checkout");
%>
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