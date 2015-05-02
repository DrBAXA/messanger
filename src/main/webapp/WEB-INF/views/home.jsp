<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <script src="<c:url value="/resources"/>/jQuery/jquery.min.js"></script>
        <script src="<c:url value="/resources"/>/jQuery/jquery.nicescroll.min.js"></script>
        <script src="<c:url value="/resources"/>/jQuery/JQuery.debounce.js"></script>
        <link rel="stylesheet" href="<c:url value="/resources"/>/styles/style.css">
        <link rel="stylesheet" href="<c:url value="/resources"/>/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="<c:url value="/resources"/>/bootstrap/css/bootstrap-theme.min.css">
        <script src="<c:url value="/resources"/>/bootstrap/js/bootstrap.min.js"></script>
        <script src="<c:url value="/resources"/>/scripts/main.js"></script>
        <script>
            function getHomeUrl(){
                return '<c:url value="/"/>';
            }
        </script>
        <title>
            BentleyTecMessenger
        </title>
    </head>
    <body>
        <div class="container col-lg-6 col-lg-offset-3 col-md-6 col-md-offset-3 col-sm-12 ">
            <nav class="navbar navbar-default">
                <p class="navbar-right navbar-text">Signed in as <a href="/logout" class="navbar-link">${user}</a></p>
            </nav>
            <div class="col-lg-4 col-md-4 column friends-column">
                <ul class="media-list" id="friends">

                </ul>
            </div>
            <div class="col-lg-8 col-md-8 column messages-column" id="messages-column">
                <div class="messages" id="messages">

                </div>
            </div>
            <div class="col-lg-8 col-md-8 new-message-box">
                    <input class="message-text" id="message" placeholder="напишіть повідомлення ..." onkeyup="sendMessage(event)">
            </div>
        </div>
    </body>
</html>
