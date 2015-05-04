var INVALID_NAME = "Логін повиненн складатись не менш ніж з 4 латинських літер";
var INVALID_EMAIL = "Така адреса електронної пошти не існує";
var INVALID_PASSWORD = "Пароль повинен складатись не менше ніж з 6 символів";
var DIFFERENT_PASSWORD = "Паролі не співпадають";

var fileInputConfig = {
    showRemove: false,
    showCaption: false,
    showUpload: false,
    allowedFileTypes: ['image'],
    maxFileSize: 3*1024,
    browseLabel: 'Виберіть фото',
    msgInvalidFileType: 'Тип файлу "{name}" невірний. Тільки файли зображень підтримуються. Виберіть інший файл, будьласка!',
    msgSizeTooLarge: 'Файл "{name}" (<b>{size} KB</b>) більший за максимально дозволений розмір <b>{maxSize} KB</b>. Виберіть інший файл, будьласка!'
};

$(document).ready(function(){
    var imageElement = $('#image');

    imageElement.fileinput(fileInputConfig);
    imageElement.on('fileerror', function(event, data) {
        imageElement.val('');
    });
});


function validate(){
    var result = true;

    var login = $('#name').val();
    var email = $('#email').val();
    var password = $('#password').val();
    var confirmPassword = $('#confirmPassword').val();
    var image = $('#image').val();

    if(! validateLogin(login)){
        showPopover("name", INVALID_NAME);
        result = false;
    }
    if(! validateEmail(email)){
        showPopover("email", INVALID_EMAIL);
        result = false;
    }
    if(! validatePasswordStrange(password)){
        showPopover("password", INVALID_PASSWORD);
        result = false;
    }
    if(! validateConfirmPassword(confirmPassword, password)){
        showPopover("confirmPassword", DIFFERENT_PASSWORD);
        result = false;
    }
    if(! validateImage(image)){
        result = false;
    }

    return result;
}

function send(){
    if(validate()){
        $('form').submit();
    }
}

function showPopover(elementName, message) {
    var element = $('#' + element);
    element.attr("data-content", message);
    element.popover("show");
}