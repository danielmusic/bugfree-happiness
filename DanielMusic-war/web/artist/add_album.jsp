<!-- ############################# Ajax Page Container ############################# -->
<section id="page" data-title="Albums">

    <section class="content section">
        <div class="container">
            <article>
                <script>
                    function loadAjax() {
                        var name = $('#name').val();
                        var yearReleased = $('#yearReleased').val();
                        var picture = $('#picture').val();
                        var description = $('#description').val();

                        url = "./MusicManagementController?target=AddAlbum";

                        $.ajax({
                            type: "POST",
                            async: false,
                            url: url,
                            enctype: 'multipart/form-data',
                            data: {'name': name, 'yearReleased': yearReleased, 'picture': picture, 'description': description},
                            dataType: "text",
                            xhr: function () {  // Custom XMLHttpRequest
                                var myXhr = $.ajaxSettings.xhr();
                                if (myXhr.upload) { // Check if upload property exists
                                    myXhr.upload.addEventListener('progress', progressHandlingFunction, false); // For handling the progress of the upload
                                }
                                return myXhr;
                            },
                            success: function (val) {
                                alert("4");
                                window.event.returnValue = true;
                                var json = JSON.parse(val);
                                if (json.result) {
                                    window.event.returnValue = false;
                                    window.location.href = "#!/artist/albums";
                                    document.add_albumForm.getElementById("goodMsg").style.display = "block";
                                    document.add_albumForm.getElementById('goodMsg').innerHTML = json.message;
                                } else {
                                    window.event.returnValue = false;
                                    window.location.href = "#!/artist/add_album";
                                    document.getElementById("errMsg").style.display = "block";
                                    document.getElementById('errMsg').innerHTML = json.message;
                                }
                            },
                            error: function (xhr, status, error) {
                                alert("5");
                                document.getElementById("errMsg").style.display = "block";
                                document.getElementById('errMsg').innerHTML = error;
                                hideLoader();
                                ajaxResultsError(xhr, status, error);
                            }
                        });
                    }

                    function progressHandlingFunction(e) {
                        if (e.lengthComputable) {
                            $('progress').attr({value: e.loaded, max: e.total});
                        }
                    }
                </script>

                <%@page import="EntityManager.Artist"%>
                <%
                    Artist artist = (Artist) (session.getAttribute("artist"));
                    if (artist != null) {
                %>

                <form name="add_albumForm" class="form">
                    <p class="error" id="errMsg" style="display:none;"></p>
                    <p class="success" id="goodMsg"  style="display:none;"></p>

                    <h2>Album details</h2>

                    <div class="row clearfix">
                        <div class="col-1-2">
                            <label for="name"><strong>Album Name</strong> </label>
                            <input type="text" id="name" name="name" required>
                        </div>
                        <div class="col-1-2 last">
                            <label for="yearReleased"><strong>Year Released</strong> </label>
                            <input type="number" id="yearReleased" name="yearReleased" min="1900" max="2050" required>
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="picture"><strong>Album Art</strong> </label>
                            <input type="file" id="picture" name="picture">
                        </div>
                    </div>

                    <div class="row clearfix">
                        <div class="col-1-1">
                            <label for="description"><strong>Album Description</strong> </label>
                            <textarea id="description" name="description" cols="88" rows="6"></textarea>
                        </div>
                    </div>

                    <button class="large invert" onclick="loadAjax()">Add</button>
                    <div class="clear"></div>
                </form>
                <%} else {%>
                <p class="warning" id="errMsg">Ops. Session timeout. <a href="#!/login">Click here to login again.</a></p>
                <%}%>
            </article>
        </div>
    </section>
</section>
