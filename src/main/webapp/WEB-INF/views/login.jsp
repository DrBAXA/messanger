<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <script src="<c:url value="/resources"/>/jQuery/jquery.min.js"></script>
    <link rel="stylesheet" href="<c:url value="/resources"/>/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<c:url value="/resources"/>/bootstrap/css/bootstrap-theme.min.css">
    <script src="<c:url value="/resources"/>/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <div class="col-lg-6 col-lg-offset-2">
        <form class="form-signin" role="form" action="<c:url value="/"/>login" method="POST">
            <h2 class="form-signin-heading">Введіть реєстраційні дані</h2>
            <c:if test="${not empty error}">
                <div class="text-danger">
                    <span><strong>Не вдається увійти.</strong></span>
                    <p>Будь ласка, перевірте, чи правильно введені логін та пароль.</p>
                </div>
            </c:if>
            <div class="form-group">
                <input type="text" class="form-control" name="name" placeholder="Login" required autofocus>
            </div>
            <div class="form-group">
                <input type="password" class="form-control" name="password" placeholder="Password" required>
            </div>
            <div class="form-group">
                <button class="btn btn-lg btn-primary btn-block" type="submit">Log in</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>