<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:url var="homeUrl" value="/"/>
<!DOCTYPE html>
<html>
<head>
    <script src="<c:url value="/resources"/>/jQuery/jquery.min.js"></script>
    <script src="<c:url value="/resources"/>/bootstrap/js/bootstrap.min.js"></script>
    <script src="<c:url value="/resources"/>/bootstrap/js/fileinput.min.js"></script>
    <link rel="stylesheet" href="<c:url value="/resources"/>/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<c:url value="/resources"/>/bootstrap/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="<c:url value="/resources"/>/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<c:url value="/resources"/>/styles/style.css">
    <link rel="stylesheet" href="<c:url value="/resources"/>/bootstrap/css/fileinput.min.css">
    <script src="<c:url value="/resources"/>/scripts/signup.js"></script>
    <script src="<c:url value="/resources"/>/scripts/FormValidation.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
</head>
<body>
<div class="container ">
    <div class=" col-lg-4 col-lg-offset-4 col-md-6 col-md-offset-3 reg-form">
        <form:form commandName="user" role="form" method="POST" action="${homeUrl}signup" enctype="multipart/form-data">
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
                <input id="image" name="image" type="file" multiple="true" accept="image/*">
                <script type="application/javascript">

                </script>
            </div>
        </form:form>
        <div class="form-group">
            <button type="button" class="btn btn-md btn-success btn-block" onclick="send()">Зареєструватись</button>
        </div>
        <div class="form-group">
            <a class="btn btn-lg btn-primary btn-block" href="login">Увійти</a>
        </div>
    </div>
</div>
<!-- Small modal window-->
<div class="container">
    <div class="col-md-9">
        <div class="alert" role="alert" style="display: none; z-index: 9999;">
            <a class="close" onclick="$('.alert').fadeOut('slow');">&times;</a>
        </div>
    </div>
</div>
</body>
</html>