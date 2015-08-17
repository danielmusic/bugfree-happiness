<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Sound.sg">
    <%@page import="java.util.List"%>
    <%@page import="EntityManager.Album"%>
    <%@page import="EntityManager.Music"%>
    <%@page import="EntityManager.Artist"%>
    <%@page import="EntityManager.Account"%>
    <%@page import="java.text.NumberFormat"%>
    <%
        Music music = (Music) session.getAttribute("musicLyrics");
        if (music != null) {
    %>
    <section class="intro-title section border-bottom" style="background-image: url(placeholders/artist-single-bg.jpg)">
        <h2 class="heading-l"><%=music.getName()%>'s <span class="color">Lyrics</span></h2>
        <span class="overlay grids"></span>
    </section>
    <section class="content section">
        <div class="container">
            <script>
                function loadArtist(id) {
                    url = "./MusicController?target=GetArtistByID";
                    $.ajax({
                        type: "GET",
                        async: false,
                        url: url,
                        data: {'id': id},
                        dataType: "text",
                        success: function (val) {
                            var json = JSON.parse(val);
                            if (json.result) {
                                window.location.href = "#!/artists";
                            }
                        },
                        error: function (xhr, status, error) {
                            document.getElementById("errMsg").style.display = "block";
                            document.getElementById('errMsg').innerHTML = error;
                            hideLoader();
                            ajaxResultsError(xhr, status, error);
                        }
                    });
                }
            </script>
            <article>
                <%
                    if (music.getLyrics() != null) {
                        String repl = music.getLyrics().replaceAll("\\r", "<br>");
                        out.print(repl);
                    }
                %>
                <br/><br/><br/>
                
                <b>Artist:</b> <a style="cursor: pointer;" onclick="javascript:loadArtist(<%=music.getAlbum().getArtist().getId()%>)"><%=music.getAlbum().getArtist().getName()%></a>
            </article>
        </div>
    </section>
    <%} else {%>
    <p class='warning'>No music was specified or the lyrics for the specified music is no longer available.</p>
    <%}%>
</section>
