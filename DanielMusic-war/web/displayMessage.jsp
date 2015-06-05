<%
    String errMsg = (String) session.getAttribute("errMsg");
    String goodMsg = (String) session.getAttribute("goodMsg");

    if ((errMsg != null) && (goodMsg == null)) {
        if (!errMsg.equals("")) {
            out.println("<p class='error'>" + errMsg + "</p>");
        }
    } else if ((errMsg == null && goodMsg != null)) {
        if (!goodMsg.equals("")) {
            out.println("<p class='success'>" + goodMsg + "</p>");
        }
    }
    session.removeAttribute("errMsg");
    session.removeAttribute("goodMsg");
%>