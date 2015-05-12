//Id and unreadMessages  of current selected friend
var selectedFriend = {
    id: 0,              //id uf current selected friend
    unreadMessages: {
        all: 0,
        loaded: 0
    },  //ids of new messages from this friend
    getAllUnread: function () {
        return this.unreadMessages.all;
    },
    setAllUnread: function (count) {
        this.unreadMessages.all = count;
    },
    incLoaded: function () {
        this.unreadMessages.loaded++
    },
    getToLoad: function () {
        return this.unreadMessages.all - this.unreadMessages.loaded;
    },
    clear: function () {
        this.unreadMessages.all = 0;
        this.unreadMessages.loaded = 0;
    }
};
var messagesCount = 0;//Count of loaded messages to list(used for loading older messages)
var messagesHeight = 0;//Height of messages list(used for auto scroll to bottom after receiving or sending message
var currentMessagesPackHeight = 0;//Height of just loaded messages (used fo keep scroll in position after adding messages to top
var friends = [];//Friends id

$(document).ready(function () {
    registerEvents();
    loadFriends();
    checkNewMessages()
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
        clearUnread(selectedFriend.id);
        if (id != selectedFriend.id) {
            selectFriend(id)
        }
    });

    //Auto load older messages if scroll is in top of list
    $('#messages-column').scroll($.debounce(500, function () {
        if ($(this).scrollTop() == 0) {
            getMessages(selectedFriend.id, messagesCount);
        }
    }));

    $(document).on("click", function () {
        selectedFriend.clear();
        clearUnread(selectedFriend.id);
    });

    checkChangesCycle();
}

/*
 Init updating data each 10 seconds;
 */
function checkChangesCycle() {
    setInterval(checkFriendsStatus, getTimeout());
}

//-----------FRIENDS-------------------------------------

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
    $('#friends').empty();
    friends.forEach(addFriendElement);
    selectFriend(friends[0].id);
}

/*
 Add each friend to document
 */
function addFriendElement(friend) {
    var friendElement = createFriendElement(friend);
    $('#friends').append(friendElement);
    friends[friend.id] = 0;
}

/*
 Create friends element from html
 */
function createFriendElement(friend) {
    var online = friend.online ? 'online' : '';
    var friendElementHtml = '<li class="friend-element" id="' + friend.id + '">' +
        '<div class="media-left">' +
        '<img class="friend-photo media-object" src="' + getHomeUrl() + 'resources/img/' + friend.photo + '">' +
        '</div>' +
        '<div class="media-body">' +
        '<h4 class="media-heading">' + friend.name + '</h4>' +
        '<span>' + online + '</span>' +
        '</div>' +
        '</li>';
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
    friends.forEach(function (friendId) {
        $('#' + friendId).find('div.media-body span').text('online')
    });
}

/*
 Set status of all friends to offline
 */
function clearFriendsStatus() {
    for (var friendId in friends) {
        $('#' + friendId).find('div.media-body span').text('')
    }
}

function findFriend(){
    var nameOrEmail = $('#find');
    $.ajax({
        url: getHomeUrl() + 'friends/find?nameOrEmail=' +nameOrEmail.val(),
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        statusCode: {
            200: function(data){
                nameOrEmail.val('');
                showFoundFriends(data);
            }
        }
    });
    return false
}

function showFoundFriends(user){
    var userElement = createFriendElement(user);
    var modalContainer = $('#found');
    modalContainer.empty();
    modalContainer.append(userElement);
    if(user.id in friends){
        $('#invite').attr('disabled', true);
    }else{
        $('#invite').attr('onclick', 'sandInvitation(' + user.id + ')');
    }
    $('#find-friend-modal').modal('toggle');
}

//--------------FRIEND_INVITATIONS-------------------------

function sandInvitation(userId){
    $.ajax({
        url: getHomeUrl() + 'friends/invitations/' +userId,
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        statusCode: {
            200: function(){
                $('#find-friend-modal').modal('toggle');
            }
        }
    });
}

function getInvitations(){
    $.ajax({
        url: getHomeUrl() + 'friends/invitations',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        statusCode: {
            200: showInvitations
        }
    })
}

function showInvitations(invitations){
    var invitationContainer = $('#invitations-container');
    invitationContainer.empty();
    invitations.forEach(function(invitation){
        var element = createInvElement(invitation);
        invitationContainer.append(element);
    });
    $('#invitations').modal('toggle');
}

function createInvElement(invitor) {
    var online = invitor.online ? 'online' : '';
    var friendElementHtml = '<li class="invitor-element media" id="' + invitor.id + '">' +
                                '<div class="media-left">' +
                                    '<img class="friend-photo media-object" src="' + getHomeUrl() + 'resources/img/' + invitor.photo + '">' +
                                '</div>' +
                                '<div class="media-body">' +
                                    '<h4 class="media-heading">' + invitor.name + '</h4>' +
                                    '<span>' + online + '</span>' +
                                '</div>' +
                                '<div class="media-right">' +
                                    '<button class="btn btn-danger" onclick="rejectInvitation(' + invitor.id + ')"><span class="glyphicon glyphicon-remove-sign"></span></button>' +
                                    '<button class="btn btn-success" onclick="acceptInvitation(' + invitor.id + ')"><span class="glyphicon glyphicon-ok-sign"></span></button>' +
                                '</div>' +
                            '</li>';
    return $.parseHTML(friendElementHtml);
}

function acceptInvitation(id){
    $.ajax({
        url: getHomeUrl() + 'friends/invitations/' + id + '/accept',
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        statusCode: {
            200: function(){
                $('#invitations').modal('toggle');
                loadFriends();
                updateInvStatus();
            }
        }
    })
}

function rejectInvitation(id){
    $.ajax({
        url: getHomeUrl() + 'friends/invitations/' + id,
        type: 'DELETE',
        contentType: 'application/json; charset=utf-8',
        statusCode: {
            200: updateInvStatus()
        }
    })
}

function updateInvStatus(){
    var invCount = $("#inv-count").text()-1;
    $("#inv-count").text(invCount);
    if(invCount = 0){
        $("#inv-count-container").visible(false);
    }
}

//-----------------------MESSAGES----------------------------------------------------
/*
 Request to get unread messages count by friends
 */
function checkNewMessages() {
    $.ajax({
        url: getHomeUrl() + 'messages/unread',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        success: function(data) {
            processNewMessages(data);
            checkNewMessages();
        },
        error:checkNewMessages
    })

}

/*
 Show count of unread messages for each friend
 */
function processNewMessages(unreadMap) {
    clearAllUnreadMessage();
    if(! unreadMap)
        return;
    for (var friendId in unreadMap) {
        friends[friendId] = unreadMap[friendId];
        addUnreadMessages(friendId, unreadMap[friendId]);
        if (friendId == selectedFriend.id) {
            selectedFriend.setAllUnread(unreadMap[friendId]);
            var unloaded = selectedFriend.getToLoad();
            if (unloaded > 0) {
                loadNewMessages(selectedFriend.id, unloaded);
            }
        }
    }
}

/*
 Clear count of unread messages for each friend
 */
function clearAllUnreadMessage() {
    for (var friendId in friends) {
        clearUnread(friendId);
    }
}

/**
 * Clear messages count from friend item
 * @param id
 */
function clearUnread(id) {
    markRead(id);
    var friendElement = $('#' + id).find('.media-body h4');
    var name = $(friendElement).text();
    var pattern = /\(\d+\)$/;
    name = name.replace(pattern, '');
    $(friendElement).text(name);
}

/**
 * Send request to server about reading messages from user with given id
 * @param id
 */
function markRead(id) {
    if (selectedFriend.getAllUnread() > 0) {
        selectedFriend.clear();
        friends[id] = 0;
        $.ajax({
            url: getHomeUrl() + 'messages/read/' + id,
            type: 'POST'
        })
    }
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
Update count of loaded messages and summary height if messages stack
 */
function increaseValues(elementHeight) {
    messagesCount++;
    messagesHeight += elementHeight + 31;
}

/**
 * Set given friend selected and load messages for conversation with him
 * @param id
 */
function selectFriend(id) {
    $('.selected').removeClass('selected');
    $('#' + id).addClass("selected");
    messagesCount = 0;
    messagesHeight = 0;
    selectedFriend.clear();
    selectedFriend.id = id;
    selectedFriend.setAllUnread(friends[id]);
    $('#messages').empty();
    getMessages(id, messagesCount);
    markRead(id);
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
                messages.reverse().forEach(function (message) {
                    selectedFriend.incLoaded();
                    addMessageToBottom(message);
                })
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

    if (message.to.id == selectedFriend.id) {
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
    if (event.keyCode == 13) {
        var pattern = /^\s*$/;
        var text = messageInput.val();
        if (!pattern.test(text)) {
            var message = {
                text: text,
                date: Date.now(),
                to: {id: selectedFriend.id}
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