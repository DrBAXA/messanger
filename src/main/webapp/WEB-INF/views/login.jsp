<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <script src="<c:url value="/resources"/>/jQuery/jquery.min.js"></script>
    <link rel="stylesheet" href="<c:url value="/resources"/>/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<c:url value="/resources"/>/bootstrap/css/bootstrap-theme.min.css">
    <script src="<c:url value="/resources"/>/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="<c:url value="/resources"/>/styles/style.css">
</head>
<body>
<div class="container">
    <div class="col-lg-4 col-lg-offset-4 col-md-6 col-md-offset-3">
        <form class="login-form" role="form" action="<c:url value="/"/>login" method="POST">
            <c:if test="${not empty error}">
                <div class="text-danger">
                    <span><strong>Не вдається увійти.</strong></span>
                    <p>Будь ласка, перевірте чи правильно введені логін та пароль.</p>
                </div>
            </c:if>
            <div class="form-group">
                <input type="text" class="form-control" name="name" placeholder="Login" required autofocus>
            </div>
            <div class="form-group">
                <input type="password" class="form-control" name="password" placeholder="Password" required>
            </div>
            <div class="form-group">
                <button class="btn btn-lg btn-primary btn-block" type="submit">Увійти</button>
            </div>
        </form>
        <div class="form-group">
            <a class="btn btn-lg btn-success btn-block" href="signup">Зареєструватись</a>
        </div>
    </div>
</div>
</body>
</html>