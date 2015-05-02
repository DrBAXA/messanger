<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:url var="homeUrl" value="/"/>
<!DOCTYPE html>
<html>
<head>
    <script src="<c:url value="/resources"/>/jQuery/jquery.min.js"></script>
    <link rel="stylesheet" href="<c:url value="/resources"/>/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<c:url value="/resources"/>/bootstrap/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="<c:url value="/resources"/>/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<c:url value="/resources"/>/styles/style.css">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
</head>
<body>
<div class="container ">
    <div class=" col-lg-4 col-lg-offset-4 col-md-6 col-md-offset-3 reg-form">
        <form:form commandName="user" role="form" method="POST" action="${homeUrl}signup">
            <div class="form-group">
                <form:input cssClass="form-control" path="name" placeholder="Логін"
                            data-content="" data-placement="right"
                            data-toggle="popover" data-original-title=""/>
                <form:errors path="name" cssClass="text-danger"/>
            </div>
            <div class="form-group">
                <form:input cssClass="form-control" path="email" placeholder="someuser@somehost.com"
                            data-content="" data-placement="right"
                            data-toggle="popover" data-original-title=""/>
                <form:errors path="email" cssClass="text-danger"/>
            </div>
            <div class="form-group">
                <form:input type="password" cssClass="form-control" path="password"
                            placeholder="Пароль"/>
                <form:errors path="password" cssClass="text-danger"/>
            </div>
            <div class="form-group">
                <input type="password" class="form-control" id="confirmPassword"
                       placeholder="Ще раз пароль">
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-md btn-success btn-block">Зареєструватись</button>
            </div>
        </form:form>
        <div class="form-group">
            <a class="btn btn-lg btn-primary btn-block" href="login">Увійти</a>
        </div>
    </div>
</div>
</body>
</html>