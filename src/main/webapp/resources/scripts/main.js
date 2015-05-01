var selectedFriend;
var messagesCount = 0;
var messagesHeight = 0;
var currentMessagesPackHeight = 0;
var friends = [];

$(document).ready(
    function() {
        registerEvents();
        checkNewMessagesCycle();
    });

function registerEvents(){
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
}

function loadFriends(){
    $.ajax({
        url: getHomeUrl()+'friends=',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        statusCode: {
            200: addMessagesToTop
        }
    })
}

function showFriends(friends){
    friends.forEach(addFriendElement)
}

function addFriendElement(friend){
    var friendElement = getFriendElement(friend);
    $('#friends').append(friendElement);
    friends.push(friend.id)
}

function getFriendElement(friend){
    var friendElementHtml ='<li class=" friend-element" id="' + friend.id + '">' +
                                '<div class="media-left">' +
                                    '<img class="friend-photo media-object" src="' + getHomeUrl() + 'resources"/>/img/' + friend.photo + '">' +
                                '</div>' +
                                '<div class="media-body">' +
                                    '<h4 class="media-heading">' + friend.name + '</h4>' +
                                     friend.online ? 'online' : '' +
                                '</div></li>';
    return $.parseHTML(friendElementHtml);
}

function checkNewMessagesCycle(){
        setInterval(checkNewMessages, 10000);
}

function checkNewMessages(){
    $.ajax({
        url: getHomeUrl()+'messages/unread',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        statusCode: {
            200: processChecked
        }
    })
}

function processChecked(unreadMap){
    clearUnreadMessage();
    for(var friendId in unreadMap){
        addUnreadMessages(friendId, unreadMap[friendId]);
    }
}

function clearUnreadMessage(){
    friends.forEach(function(id){
        var friendElement = $('#' + id).find('.media-body h4');
        var name = $(friendElement).text();
        var pattern = /\(\d\)$/;
        name = name.replace(pattern, '')
    })
}

function addUnreadMessages(friendId, count){
    var friendElement = $('#' + friendId).find('.media-body h4');
    var name = $(friendElement).text();
    $(friendElement).text(name+ ' (' + count + ')');
}

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
    var messageElementHtml ='<div class="message">' +
        '<div class="triangle"></div>' +
        '<div class="message-text">' + message.text + '</div>'+
        '<div class="message-date">' + new Date(message.date).toLocaleString() + '</div>'+
        '</div>';
    var messageElement = $.parseHTML(messageElementHtml);

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
    checkNewMessages();
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