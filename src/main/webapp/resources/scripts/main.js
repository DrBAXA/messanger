var selectedFriend;
var messagesCount = 0;
var messagesHeight = 0;
var currentMessagesPackHeight = 0;
var friends = [];
var newMessages = {};

$(document).ready(
    function () {
        registerEvents();
        loadFriends();
    });

function registerEvents() {
    $(".column").niceScroll({cursorcolor: '#F9F9F9'}).hide();

    $('#friends').on('click', '.friend-element', function (event) {
        selectFriend($(event.currentTarget).attr('id'));
    });

    $('#messages-column').scroll($.debounce(500, function () {
        if ($(this).scrollTop() == 0) {
            getMessages(selectedFriend, messagesCount);
        }
    }));

    checkChangesCycle();
}

function loadFriends() {
    $.ajax({
        url: getHomeUrl() + 'friends',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        statusCode: {
            200: showFriends
        }
    })
}

function showFriends(friends) {
    friends.forEach(addFriendElement);
    selectFriend(friends[0].id);
    checkNewMessages()
}

function addFriendElement(friend) {
    var friendElement = getFriendElement(friend);
    $('#friends').append(friendElement);
    friends.push(friend.id)
}

function getFriendElement(friend) {
    var online = friend.online ? 'online' : '';
    var friendElementHtml = '<li class="friend-element" id="' + friend.id + '">' +
        '<div class="media-left">' +
        '<img class="friend-photo media-object" src="' + getHomeUrl() + 'resources/img/' + friend.photo + '">' +
        '</div>' +
        '<div class="media-body">' +
        '<h4 class="media-heading">' + friend.name + '</h4>' +
        '<span>' + online + '</span>' +
        '</div></li>';
    return $.parseHTML(friendElementHtml);
}

function checkFriendsStatus() {
    $.ajax({
        url: getHomeUrl() + 'friends/online',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        statusCode: {
            200: function (friendsOnline) {
                clearFriendsStatus();
                updateFriendsStatus(friendsOnline);
            }
        }
    })
}

function updateFriendsStatus(friends) {
    for (var friendId in friends) {
        $('#' + friendId).find('div.media-body span').text('online')
    }
}

function clearFriendsStatus() {
    friends.forEach(function (friendId) {
        $('#' + friendId).find('div.media-body span').text('')
    })
}

function checkChangesCycle() {
    setInterval(checkNewMessages, 10000);
    setInterval(checkFriendsStatus, 10000);
}

function checkNewMessages() {
    $.ajax({
        url: getHomeUrl() + 'messages/unread',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        statusCode: {
            200: processChecked
        }
    })
}

function processChecked(unreadMap) {
    clearUnreadMessage();
    for (var friendId in unreadMap) {
        addUnreadMessages(friendId, unreadMap[friendId]);
        if (friendId == selectedFriend) {
            countUnread(friendId, unreadMap[friendId])
        }
    }
}

function countUnread(friendId, countAll) {
    if (friendId in newMessages) {
        newMessages[friendId].all = countAll;
        if(countAll == 0){
            newMessages[friendId].loaded = 0;
        }
        var toLoad = countAll - newMessages[friendId].loaded;
        if (toLoad > 0) {
            loadNewMessages(friendId, toLoad)
        }
    } else {
        var newMessagesByFriend = {
            all: countAll,
            loaded: 0
        };
        newMessages[friendId] = newMessagesByFriend;
        loadNewMessages(friendId, countAll)
    }
}

function clearUnreadMessage() {
    friends.forEach(function (id) {
        var friendElement = $('#' + id).find('.media-body h4');
        var name = $(friendElement).text();
        var pattern = /\(\d\)$/;
        name = name.replace(pattern, '');
        $(friendElement).text(name)
    })
}

function addUnreadMessages(friendId, count) {
    var friendElement = $('#' + friendId).find('.media-body h4');
    var name = $(friendElement).text();
    $(friendElement).text(name + ' (' + count + ')');
}

function increaseValues(elementHeight) {
    messagesCount++;
    messagesHeight += elementHeight + 31;
}

function selectFriend(id) {
    $('.selected').removeClass('selected');
    $('#' + id).addClass("selected");
    if (id != selectedFriend) {
        messagesCount = 0;
        messagesHeight = 0;
        selectedFriend = id;
        $('#messages').empty();
        getMessages(selectedFriend, messagesCount);
    }
}

function loadNewMessages(friendId, count) {
    $.ajax({
        url: getHomeUrl() + 'messages?friendId=' + friendId + '&first=' + 0 + '&count=' + count + '&setRead=false',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        statusCode: {
            200: function (messages) {
                newMessages[friendId].loaded += messages.length;
                messages.reverse().forEach(addMessageToBottom)
            }
        }
    })
}

function getMessages(friendId, first) {
    $.ajax({
        url: getHomeUrl() + 'messages?friendId=' + friendId + '&first=' + first,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        statusCode: {
            200: addMessagesToTop
        }
    })
}

function getMessageElement(message) {
    var messageElementHtml = '<div class="message">' +
        '<div class="triangle"></div>' +
        '<div class="message-text">' + message.text + '</div>' +
        '<div class="message-date">' + new Date(message.date).toLocaleString() + '</div>' +
        '</div>';
    var messageElement = $.parseHTML(messageElementHtml);

    if (message.to.id == selectedFriend) {
        $(messageElement).addClass('my-message');
        $(messageElement).find('div.triangle').addClass('right-triangle')
    } else {
        $(messageElement).addClass('friend-message');
        $(messageElement).find('div.triangle').addClass('left-triangle')
    }

    return messageElement
}

function addMessagesToTop(messages) {
    currentMessagesPackHeight = 0;
    messages.forEach(function (message) {
        var messageElement = getMessageElement(message);
        $('#messages').prepend(messageElement);
        currentMessagesPackHeight += $(messageElement).height() + 31;
        increaseValues($(messageElement).height());
        $('#messages-column').scrollTop(currentMessagesPackHeight);
    });
    checkNewMessages();
}

function addMessageToBottom(message) {
    var messageElement = getMessageElement(message);
    $('#messages').append(messageElement);
    increaseValues($(messageElement).height());
    $('#messages-column').animate({scrollTop: messagesHeight}, 1000);
}

function sendMessage(event) {
    event.preventDefault();
    if (event.keyCode == 13 ) {
        var pattern = /^\s*$/;
        var text = $("#message").val();
        if(! pattern.test(text)){
            var message = {
                text: text,
                date: Date.now(),
                to: {id: selectedFriend}
            };

            $.ajax({
                url: getHomeUrl() + 'messages',
                type: 'POST',
                data: JSON.stringify(message),
                contentType: 'application/json; charset=utf-8',
                statusCode: {
                    200: addMessageToBottom(message)
                }
            });
        }
        $("#message").val('')
    }
}