<!DOCTYPE html>
<html>
    <head>
        <!-- Basic -->
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">	

        <title>Daniel Music</title>	
        <!-- Favicon -->
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon" />
        <link rel="apple-touch-icon" href="img/apple-touch-icon.png">

        <!-- Mobile Metas -->
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Web Fonts  -->
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700,800%7CShadows+Into+Light" rel="stylesheet" type="text/css">

        <!-- Vendor CSS -->
        <link rel="stylesheet" href="../assets/vendor/bootstrap/bootstrap.css">
        <link rel="stylesheet" href="../assets/vendor/fontawesome/css/font-awesome.css">
        <link rel="stylesheet" href="../assets/vendor/owlcarousel/owl.carousel.min.css" media="screen">
        <link rel="stylesheet" href="../assets/vendor/owlcarousel/owl.theme.default.min.css" media="screen">
        <link rel="stylesheet" href="../assets/vendor/magnific-popup/magnific-popup.css" media="screen">

        <!-- Theme CSS -->
        <link rel="stylesheet" href="../assets/css/theme.css">
        <link rel="stylesheet" href="../assets/css/theme-elements.css">
        <link rel="stylesheet" href="../assets/css/theme-blog.css">
        <link rel="stylesheet" href="../assets/css/theme-shop.css">
        <link rel="stylesheet" href="../assets/css/theme-animate.css">

        <!-- Skin CSS -->
        <link rel="stylesheet" href="../assets/css/skins/default.css">

        <!-- Theme Custom CSS -->
        <link rel="stylesheet" href="../assets/css/custom.css">

        <!-- Head Libs -->
        <script src="../assets/vendor/modernizr/modernizr.js"></script>

        <!--[if IE]>
                <link rel="stylesheet" href="css/ie.css">
        <![endif]-->

        <!--[if lte IE 8]>
                <script src="vendor/respond/respond.js"></script>
                <script src="vendor/excanvas/excanvas.js"></script>
        <![endif]-->

    </head>
    <body>
        <div role="main" class="main">
            <div class="row">
                <div class="col-md-4 col-md-offset-4">  
                    <div class="featured-box featured-boxes.login" style="min-height: 100%;">
                        <div class="panel-heading"> 
                            <i class="fa fa-unlock-alt fa-4x"  style="margin-top: 10px;"></i><h6 class="panel-title">Login</h6>
                        </div>

                        <div class="panel-body">
                            <jsp:include page="displayMessageLong.jsp" />
                            <form action="../AdminAccountManagementController">
                                <div class="row">
                                    <div class="form-group">
                                        <div class="col-md-12">
                                            <label>Email</label>
                                            <input type="email" name="email" id="email" class="form-control input-lg" required="true">
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="form-group">
                                        <div class="col-md-12">
                                            <label>Password</label>
                                            <input type="password" name="password" id="password" class="form-control input-lg" required="true">
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <input type="submit" value="Login" class="btn btn-lg btn-primary btn-block">
                                    </div>
                                </div>
                                <input type="hidden" name="target" value="Login">
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Vendor -->
        <!--[if lt IE 9]>
        <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
        <![endif]-->
        <!--[if gte IE 9]><!-->
        <script src="../assets/vendor/jquery/jquery.js"></script>
        <!--<![endif]-->
        <script src="../assets/vendor/jquery.appear/jquery.appear.js"></script>
        <script src="../assets/vendor/jquery.easing/jquery.easing.js"></script>
        <script src="../assets/vendor/jquery-cookie/jquery-cookie.js"></script>
        <script src="../assets/vendor/bootstrap/bootstrap.js"></script>
        <script src="../assets/vendor/common/common.js"></script>
        <script src="../assets/vendor/jquery.validation/jquery.validation.js"></script>
        <script src="../assets/vendor/jquery.stellar/jquery.stellar.js"></script>
        <script src="../assets/vendor/jquery.easy-pie-chart/jquery.easy-pie-chart.js"></script>
        <script src="../assets/vendor/jquery.gmap/jquery.gmap.js"></script>
        <script src="../assets/vendor/isotope/jquery.isotope.js"></script>
        <script src="../assets/vendor/owlcarousel/owl.carousel.js"></script>
        <script src="../assets/vendor/jflickrfeed/jflickrfeed.js"></script>
        <script src="../assets/vendor/magnific-popup/jquery.magnific-popup.js"></script>
        <script src="../assets/vendor/vide/vide.js"></script>

        <!-- Theme Base, Components and Settings -->
        <script src="../assets/js/theme.js"></script>

        <!-- Theme Custom -->
        <script src="../assets/js/custom.js"></script>

        <!-- Theme Initialization Files -->
        <script src="../assets/js/theme.init.js"></script>

    </body>
</html>
