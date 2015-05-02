package ua.bentleytek.messenger.config;


import ua.bentleytek.messenger.websocket.ConnectionHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.DeploymentException;
import javax.websocket.Extension;
import javax.websocket.server.ServerContainer;
import java.util.Set;


@WebListener
public class SocketContextListener implements ServletContextListener {

    private final static String SERVER_CONTAINER_ATTRIBUTE = "javax.websocket.server.ServerContainer";

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext container = sce.getServletContext();

        final ServerContainer serverContainer = (ServerContainer) container.getAttribute(SERVER_CONTAINER_ATTRIBUTE);
        try {
            serverContainer.addEndpoint(new SocketEndpointConfig(ConnectionHandler.class, "/websocket"));
        } catch (DeploymentException e) {
            e.printStackTrace();
        }
        Set<Extension> installedExtensions = serverContainer.getInstalledExtensions();
        System.out.println("Installed extensions: " + installedExtensions.size());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
