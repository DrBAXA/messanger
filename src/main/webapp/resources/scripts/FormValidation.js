/**
 * Methods for validating credentials by regular expressions, that was entered by user.
 */

function validateEmail(email){
    var regExp = /^[A-Za-z]([A-Za-z0-9])+([\.\-_]?[A-Za-z0-9]+)*@([a-z0-9-])+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
    return regExp.test(email);
}

function validatePasswordStrange(password){
    var regExp = /.{6,20}/;
    return regExp.test(password);
}

function validateConfirmPassword(password, confirm){
    return password == confirm;
}

function validateLogin(login){
    var regExp = /^(\w){2,}[\s]?(\w){2,}$/;
    return regExp.test(login);
}

function validateImage(image){
    return image != '';
}