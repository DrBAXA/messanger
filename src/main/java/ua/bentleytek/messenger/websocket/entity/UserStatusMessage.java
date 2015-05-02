package ua.bentleytek.messenger.websocket.entity;

public class UserStatusMessage {
    private int userId;
    private int friendId;
    private Action action;
}

enum Action{
    TYPING,
    DELETE
}
