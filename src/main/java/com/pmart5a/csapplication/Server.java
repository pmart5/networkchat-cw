package com.pmart5a.csapplication;

import com.pmart5a.handler.ServerConnectionHandler;
import com.pmart5a.loggers.SingleLogger;
import com.pmart5a.servaces.ServiceClients;
import com.pmart5a.settings.Settings;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;

import static com.pmart5a.servaces.MessageDesigner.getSystemDesign;
import static com.pmart5a.enums.ErrorMsg.*;
import static com.pmart5a.enums.SystemMsg.*;

public class Server {

    public static final ConcurrentSkipListSet<ServerConnectionHandler> connections =
            new ConcurrentSkipListSet<>(Comparator.comparing(ServerConnectionHandler::getConnectionNumber));

    public static void main(String[] args) {
        Settings settings = Settings.getSettings();
        SingleLogger logger = SingleLogger.getLogger(settings.getNameServerLog());
        try (ServerSocket serverSocket = new ServerSocket(settings.getPort())) {
            logger.logFileOut(getSystemDesign(SERVER_START.getMsg()));
            boolean isRunOnce = true;
            while (true) {
                try {
                    logger.logFileOut(getSystemDesign(WAITING_CONNECTION.getMsg()));
                    Socket clientSocket = serverSocket.accept();
                    if (isRunOnce) {
                        new Thread(null, ServiceClients::getMessageLogger).start();
                        new Thread(null, ServiceClients::checkConnections).start();
                        isRunOnce = false;
                    }
                    logger.logFileOut(getSystemDesign(String.format(CLIENT_CONNECTED.getMsg(),
                            clientSocket.getRemoteSocketAddress())));
                    connections.add(new ServerConnectionHandler(clientSocket));
                } catch (IOException e) {
                    logger.logFileOut(getSystemDesign(ERROR_INPUT_OUTPUT.getMsg() + e));
                }
            }
        } catch (IOException e) {
            logger.logFileOut(getSystemDesign(ERROR_INPUT_OUTPUT.getMsg() + e));
        } finally {
            logger.logFileOut(getSystemDesign(SERVER_STOP.getMsg()));
            logger.close();
        }
    }
}