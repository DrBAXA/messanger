var selectedFriend;//Id of current selected friend
var messagesCount = 0;//Count of loaded messages to list(used for loading older messages)
var messagesHeight = 0;//Height of messages list(used for auto scroll to bottom after receiving or sending message
var currentMessagesPackHeight = 0;//Height of just loaded messages (used fo keep scroll in position after adding messages to top
var friends = [];//Friends id
var newMessages = {};//Count all unread messages and already loaded unread messages for each friends

$(document).ready(
    function () {
        registerEvents();
        loadFriends();
    });

function registerEvents() {
    /*
    Make scroll more elegant
     */
    $(".column").niceScroll({cursorcolor: '#F9F9F9'}).hide();

    /*
    Set new selected friend and load messages for him
     */
    $('#friends').on('click', '.friend-element', function (event) {
        var id = $(event.currentTarget).attr('id');
        $('.selected').removeClass('selected');
        $('#' + id).addClass("selected");
        if (id != selectedFriend) {
            messagesCount = 0;
            messagesHeight = 0;
            selectedFriend = id;
            $('#messages').empty();
            getMessages(selectedFriend);
        }
    });

    //Auto load older messages if scroll is in top of list
    $('#messages-column').scroll($.debounce(500, function () {
        if ($(this).scrollTop() == 0) {
            getMessages(selectedFriend, messagesCount);
        }
    }));

    checkChangesCycle();
}
/*
Init updating data each 10 seconds;
 */
function checkChangesCycle() {
    setInterval(checkNewMessages, 10000);
    setInterval(checkFriendsStatus, 10000);
}

/*
Request to get list of friends
 */
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

/*
Show loaded friends list
 */
function showFriends(friends) {
    friends.forEach(addFriendElement);
    //selectFriend(friends[0].id);
    checkNewMessages()
}
/*
Add each friend to document
 */
function addFriendElement(friend) {
    var friendElement = getFriendElement(friend);
    $('#friends').append(friendElement);
    friends.push(friend.id)
}
/*
Create friends element from html
 */
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

/*
Request to get online status of friends
 */
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

/*
Set status of given friends to online
 */
function updateFriendsStatus(friends) {
    for (var friendId in friends) {
        $('#' + friendId).find('div.media-body span').text('online')
    }
}

/*
Set status of all friends to offline
 */
function clearFriendsStatus() {
    friends.forEach(function (friendId) {
        $('#' + friendId).find('div.media-body span').text('')
    })
}

/*
Request to get unread messages count by friends
 */
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

/*
Show count of unread messages for each friend
 */
function processChecked(unreadMap) {
    clearUnreadMessage();
    for (var friendId in unreadMap) {
        addUnreadMessages(friendId, unreadMap[friendId]);
        if (friendId == selectedFriend) {
            countUnread(friendId, unreadMap[friendId])
        }
    }
}

/*
Updating count of unread messages for each friend
and loading new messages for current selected friend
 */
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
        newMessages[friendId] = {  all: countAll, loaded: 0};
        loadNewMessages(friendId, countAll)
    }
}

/*
Clear count of unread messages for each friend
 */
function clearUnreadMessage() {
    friends.forEach(function (id) {
        var friendElement = $('#' + id).find('.media-body h4');
        var name = $(friendElement).text();
        var pattern = /\(\d\)$/;
        name = name.replace(pattern, '');
        $(friendElement).text(name)
    })
}

/*
Set count of unread messages for given friend
 */
function addUnreadMessages(friendId, count) {
    var friendElement = $('#' + friendId).find('.media-body h4');
    var name = $(friendElement).text();
    $(friendElement).text(name + ' (' + count + ')');
}

/*

 */
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

/*
Request to get unread messages
 */
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

/*
Request to get page of messages of FRIEND from FIRST number
 */
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

/*
Create message element from html
 */
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

/*
Add all loaded messages to top of list
 */
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

/*
Add given message to list bottom
 */
function addMessageToBottom(message) {
    var messageElement = getMessageElement(message);
    $('#messages').append(messageElement);
    increaseValues($(messageElement).height());
    $('#messages-column').animate({scrollTop: messagesHeight}, 1000);
}

/*
Send message to current selected friend
 */
function sendMessage(event) {
    var messageInput = $("#message");
    if (event.keyCode == 13 ) {
        var pattern = /^\s*$/;
        var text = messageInput.val();
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
        messageInput.val('')
    }
}