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
                <div class="navbar-right">
                    <c:if test="${not empty(invitations)}">
                        <p onclick="getInvitations()" class="navbar-link navbar-text invitations"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>(${invitations})</p>
                    </c:if>
                    <p class="navbar-text">Ви увійшли як ${user} (<a href="/logout" class="navbar-link">вийти</a>)</p>
                </div>
            </nav>
            <div class="col-lg-4 col-md-4 column friends-column">
                <ul class="media-list" id="friends">

                </ul>
                <div class="find-friend">
                    <form onsubmit="return findFriend()">
                        <input type="text" id="find" class="find-friend-input" placeholder="Знайти більше по e-mail чи ніку ... ">
                    </form>
                </div>
            </div>
            <div class="col-lg-8 col-md-8 column messages-column" id="messages-column">
                <div class="messages" id="messages">

                </div>
            </div>
            <div class="col-lg-8 col-lg-offset-4 col-md-8 col-md-offset-4 new-message-box">
                    <input class="message-text" id="message" placeholder="напишіть повідомлення ..." onkeyup="sendMessage(event)">
            </div>
        </div>
        <!-- Modal find friends-->
        <div class="modal fade" id="find-friend-modal" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body" id="found">

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Відмінити</button>
                        <button type="button" id="invite" class="btn btn-primary">Надіслати запрошення</button>
                    </div>
                </div>
            </div>
        </div>
        <%--Modal invitations--%>
        <div class="modal fade" id="invitations" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body" id="invitations-container">

                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
