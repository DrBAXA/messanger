<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <script src="<c:url value="/resources"/>/jQuery/jquery.min.js"></script>
        <script src="<c:url value="/resources"/>/jQuery/jquery.nicescroll.min.js"></script>
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
                <p class="navbar-right navbar-text">Signed in as <a href="#" class="navbar-link">Mark Otto</a></p>
            </nav>
            <div class="col-lg-4 col-md-4 column friends-column" id="friends">
                <ul class="media-list">
                    <c:forEach var="friend" items="${friends}">
                        <li class=" friend-element" id="${friend.id}">
                            <div class="media-left">
                                <img class="friend-photo media-object" src="<c:url value="/resources"/>/img/${friend.photo}">
                            </div>
                            <div class="media-body">
                                <h4 class="media-heading">${friend.name}</h4>
                                <c:if test="${friend.online == true}">
                                    online
                                </c:if>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <div class="col-lg-8 col-md-8 column messages-column">
                <div class="messages" id="messages">
                    <div class="message friend-message">
                        <div class="triangle left-triangle"></div>
                        <div class="message-text">
                            Привдівлао діваомп ізвшармп ідвамлоівда іщвшаг ідвало іжєвадлп ідвалом ідварм ідвам дівлао
                        </div>
                        <div class="message-date">
                            21.05.2015 18:25
                        </div>
                    </div>
                    <div class="message my-message">
                        <div class="triangle right-triangle"></div>
                        <div class="message-text">
                            Привдівлао діваомп ізвшармп ідвамлоівда іщвшаг ідвало іжєвадлп ідвалом ідварм ідвам дівлао
                        </div>
                        <div class="message-date">
                            21.05.2015 18:25
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
