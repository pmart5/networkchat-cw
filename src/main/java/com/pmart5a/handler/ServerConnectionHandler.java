package com.pmart5a.handler;

import com.pmart5a.csapplication.Server;
import com.pmart5a.loggers.SingleLogger;
import com.pmart5a.servaces.GeneratorId;
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

public class ServerConnectionHandler extends Thread {

    private final Socket clientSocket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final int connectionNumber;
    private String clientNickname;

    public ServerConnectionHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
        out = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8);
        GeneratorId generator = GeneratorId.getGeneratorId();
        connectionNumber = generator.getId();
        clientNickname = DEFAULT_CLIENT_NICKNAME.getValue();
        start();
    }

    @Override
    public void run() {
        Settings settings = Settings.getSettings();
        SingleLogger logger = SingleLogger.getLogger(settings.getNameServerLog());
        try {
            getNickname();
            processNickname();
            processClientMessage();
            connections.remove(this);
            sendToEveryone();
            logger.logFileOut(getSystemDesign(String.format(CLIENT_DISCONNECTED.getMsg(),
                    clientSocket.getRemoteSocketAddress())));
        } catch (IOException e) {
            logger.logFileOut(getSystemDesign(ERROR_INPUT_OUTPUT.getMsg() + e));
        } finally {
            try {
                out.close();
                in.close();
                clientSocket.close();
            } catch (IOException e) {
                logger.logFileOut(getSystemDesign(ERROR_INPUT_OUTPUT.getMsg() + e));
            }
        }
    }

    private void getNickname() throws IOException {
        sendMessage(getSpiritDesign(INPUT_NICKNAME.getMsg()));
        String clientMessage = in.readLine();
        if (!clientMessage.equals("null") && !clientMessage.equals("")) {
            clientNickname = clientMessage;
        }
    }

    private void processNickname() {
        if (!clientNickname.equals(NAME_SERVICE_TREAD.getValue())) {
            for (ServerConnectionHandler connection : connections) {
                if (connection.equals(this)) {
                    sendMessageSpirit(connection, LOG_IN_TO_CHAT.getMsg());
                } else {
                    sendMessageSpirit(connection, JOINING_CHAT.getMsg());
                }
            }
        }
    }

    private void processClientMessage() throws IOException {
        String clientMessage;
        while ((clientMessage = in.readLine()) != null) {
            if (clientMessage.equals(EXIT_COMMAND.getValue())) {
                this.sendMessage(clientMessage);
                break;
            } else {
                sendToEveryoneExceptYourself(clientMessage);
            }
        }
    }

    private void sendToEveryone() {
        if (!clientNickname.equals(NAME_SERVICE_TREAD.getValue())) {
            for (ServerConnectionHandler connection : connections) {
                sendMessageSpirit(connection, EXITING_CHAT.getMsg());
            }
        }
    }

    private void sendMessageSpirit(ServerConnectionHandler connection, String messageSpirit) {
        connection.sendMessage(getSpiritDesign(String.format(messageSpirit, clientNickname,
                connections.size() - NUMBER_SERVICE_THREADS.getValue())));
    }

    private void sendToEveryoneExceptYourself(String clientMessage) {
        for (ServerConnectionHandler connection : Server.connections) {
            if (!connection.equals(this)) {
                if (clientNickname.equals(NAME_SERVICE_TREAD.getValue())) {
                    connection.sendMessage(getSpiritDesign(clientMessage));
                } else {
                    connection.sendMessage(getUserDesign(String.format(NICKNAME_MESSAGE.getMsg(), clientNickname,
                            clientMessage)));
                }

            }
        }
    }

    private void sendMessage(String message) {
        out.println(message);
    }

    public int getConnectionNumber() {
        return connectionNumber;
    }

    public String getClientNickname() {
        return clientNickname;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}