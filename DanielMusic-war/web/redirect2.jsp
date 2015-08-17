<!DOCTYPE html>
<%//set a session to redirect example below:
    //session.setAttribute("redirectPage","http://sounds.sg/#!/checkout");
%>
<html>
    <head>
        <title></title>
        <script>
            if (window != top) {
                top.location.replace("/redirect.jsp");
            }
        </script>
    </head>
    <body>
    </body>
</html>