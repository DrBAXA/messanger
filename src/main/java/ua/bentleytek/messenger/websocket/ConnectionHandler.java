package ua.bentleytek.messenger.websocket;


import org.springframework.stereotype.Component;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

public class ConnectionHandler extends  Endpoint{

    @Override
    public void onOpen(Session session, EndpointConfig config) {

        System.out.println("websocket connection opened: " + session.getId());

        CoordinateHandler handler = new CoordinateHandler(session);
        handler.setEncoder(busLocationEncoder);
        handler.setGatheringService(gatheringService);
        session.addMessageHandler(handler);
    }
}

