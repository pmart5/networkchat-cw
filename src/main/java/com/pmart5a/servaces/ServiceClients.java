package com.pmart5a.servaces;

import com.pmart5a.handler.ServerConnectionHandler;
import com.pmart5a.loggers.Logger;
import com.pmart5a.loggers.SingleLogger;
import com.pmart5a.settings.Settings;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static com.pmart5a.csapplication.Server.connections;
import static com.pmart5a.enums.ChatMsg.*;
import static com.pmart5a.enums.ErrorMsg.*;
import static com.pmart5a.enums.ParametersInt.*;
import static com.pmart5a.enums.ParametersStrings.*;
import static com.pmart5a.enums.SystemMsg.*;
import static com.pmart5a.servaces.MessageDesigner.*;

public class ServiceClients {

    public static void getMessageLogger() {
        Settings settings = Settings.getSettings();
        Logger messageLogger = new Logger(settings.getNameServerMessageLog());
        SingleLogger logger = SingleLogger.getLogger(settings.getNameServerLog());
        try (Socket clientSocket = new Socket(settings.getHost(), settings.getPort());
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),
                     StandardCharsets.UTF_8))) {
            String serverMessage = in.readLine();
            if (serverMessage != null) {
                out.println(NAME_SERVICE_TREAD.getValue());
            }
            while ((serverMessage = in.readLine()) != null) {
                messageLogger.logFile(serverMessage);
            }
        } catch (IOException e) {
            logger.logFileOut(getSystemDesign(ERROR_INPUT_OUTPUT.getMsg() + e));
        } finally {
            messageLogger.close();
        }
    }

    public static void checkConnections() {
        Settings settings = Settings.getSettings();
        SingleLogger logger = SingleLogger.getLogger(settings.getNameServerLog());
        try (Socket clientSocket = new Socket(settings.getHost(), settings.getPort());
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8)) {
            out.println(NAME_SERVICE_TREAD.getValue());
            deleteBrokenConnections(out, logger);
        } catch (IOException e) {
            logger.logFileOut(getSystemDesign(ERROR_INPUT_OUTPUT.getMsg() + e));
        }
    }

    private static void deleteBrokenConnections(PrintWriter out, SingleLogger logger) {
        while (true) {
            if (!connections.isEmpty()) {
                for (ServerConnectionHandler connection : connections) {
                    if (!connection.isAlive()) {
                        connections.remove(connection);
                        out.println(String.format(DISCONNECTED_CHAT.getMsg(), connection.getClientNickname(),
                                connections.size() - NUMBER_SERVICE_THREADS.getValue()));
                        logger.logFileOut(getSystemDesign(String.format(LOST_CONNECTION_CLIENT.getMsg(),
                                connection.getClientSocket().getRemoteSocketAddress())));
                    }
                }
            }
        }
    }
}