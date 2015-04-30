var selectedFriend;
var messagesCount = 0;
var messagesHeight = 0;
var currentMessagesPackHeight = 0;

$(document).ready(
    function() {
        $(".column").niceScroll({cursorcolor:'#00F'}).hide();

        $('.friend-element').on('click', function(event){
            $('.selected').removeClass('selected');
            $(event.currentTarget).addClass("selected");
            selectFriend($(event.currentTarget).attr('id'));
        });

        $('#messages-column').scroll($.debounce( 500, function(){
            if($(this).scrollTop() == 0){
                getMessages(selectedFriend, messagesCount);
            }
        }));

    });

function increaseValues(elementHeight){
    messagesCount++;
    messagesHeight += elementHeight + 31;
}

function selectFriend(id){
    if(id != selectedFriend){
        messagesCount = 0;
        messagesHeight = 0;
        selectedFriend = id;
        $('#messages').empty();
        getMessages(selectedFriend, messagesCount);
    }
}

function getMessages(friendId, first){
    $.ajax({
        url: getHomeUrl()+'messages?friendId=' + friendId + '&first=' + first,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        statusCode: {
            200: addMessagesToTop
        }
    })
}

function getMessageElement(message){
    var messageElementText ='<div class="message">' +
        '<div class="triangle"></div>' +
        '<div class="message-text">' + message.text + '</div>'+
        '<div class="message-date">' + new Date(message.date).toLocaleString() + '</div>'+
        '</div>';
    var messageElement = $.parseHTML(messageElementText);

    if(message.to.id == selectedFriend){
        $(messageElement).addClass('my-message');
        $(messageElement).find('div.triangle').addClass('right-triangle')
    }else{
        $(messageElement).addClass('friend-message');
        $(messageElement).find('div.triangle').addClass('left-triangle')
    }

    return messageElement
}

function addMessagesToTop(messages){
    currentMessagesPackHeight = 0;
    messages.forEach(function(message){
        var messageElement = getMessageElement(message);
        $('#messages').prepend(messageElement);
        currentMessagesPackHeight += $(messageElement).height() + 31;
        increaseValues($(messageElement).height());
        $('#messages-column').scrollTop(currentMessagesPackHeight);
    });

}

function addMessageToBottom(message){
    var messageElement = getMessageElement(message);
    increaseValues($(messageElement).height());
    $('#messages').append(messageElement);
    $('#messages-column').animate({ scrollTop: messagesHeight}, 1000);
}

function sendMessage(){
    var message = {
        text: $("#message").val(),
        date: Date.now(),
        to: {id : selectedFriend}
    };

    $.ajax({
        url: getHomeUrl()+'messages',
        type: 'POST',
        data: JSON.stringify(message),
        contentType: 'application/json; charset=utf-8',
        statusCode: {
            200: addMessageToBottom(message)
        }
    })
}