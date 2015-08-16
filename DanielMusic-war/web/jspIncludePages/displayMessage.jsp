<%
    String errMsg = (String) session.getAttribute("errMsg");
    String goodMsg = (String) session.getAttribute("goodMsg");

    System.out.print(">>>>> " + goodMsg);

    if ((errMsg != null) && (goodMsg == null)) {
        if (!errMsg.equals("")) {
            out.println("<p class='error' id='errMsg'>" + errMsg + "</p>");
        }
    } else if ((errMsg == null && goodMsg != null)) {
        if (!goodMsg.equals("")) {
            out.println("<p class='success' id='goodMsg'>" + goodMsg + "</p>");
        }
    }

    session.removeAttribute("errMsg");
    session.removeAttribute("goodMsg");
%>