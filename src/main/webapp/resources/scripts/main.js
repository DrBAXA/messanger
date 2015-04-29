var selectedFriend;

$(document).ready(
    function() {
        $(".column").niceScroll({cursorcolor:'#00F'}).hide();

        $('.friend-element').on('click', function(event){
            selectedFriend = $(event.currentTarget).attr('id');
            getMessages(selectedFriend, 0);
        })
    });

function getMessages(friendId, first){
    $.ajax({
        url: getHomeUrl()+'messages?friendId=' + friendId + '&first =' + first,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        statusCode: {
            200: addMessages
        }
    })
}

function addMessages(messages){
    messages.forEach(function(message){
        var messageElementText ='<div class="message">' +
                                    '<div class="triangle"></div>' +
                                    '<div class="message-text">' + message.text + '</div>'+
                                    '<div class="message-date">' + message.date + '</div>'+
                                '</div>';
        var messageElement = $.parseHTML(messageElementText);

        if(message.to.id == selectedFriend){
            $(messageElement).addClass('friend-message');
            $(messageElement).find('div.triangle').addClass('left-triangle')
        }else{
            $(messageElement).addClass('my-message');
            $(messageElement).find('div.triangle').addClass('right-triangle')
        }

        $('#messages').append(messageElement)
    })
}