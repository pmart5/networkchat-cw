package com.pmart5a.csapplication;

import com.pmart5a.loggers.Logger;
import com.pmart5a.settings.Settings;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static com.pmart5a.enums.ErrorMsg.*;
import static com.pmart5a.enums.ChatMsg.*;
import static com.pmart5a.enums.ParametersStrings.*;
import static com.pmart5a.enums.SystemMsg.*;
import static com.pmart5a.servaces.MessageDesigner.*;

public class Client {

    private static void readMessage(BufferedReader in, Logger messageLogger) throws IOException {
        String serverMessage;
        while ((serverMessage = in.readLine()) != null) {
            if (serverMessage.equals(EXIT_COMMAND.getValue())) {
                break;
            } else if (serverMessage.equals(getSpiritDesign(INPUT_NICKNAME.getMsg()))) {
                messageLogger.logOut(serverMessage);
            } else {
                messageLogger.logFileOut(serverMessage);
            }
        }
    }

    private static void writeMessage(PrintWriter out, Logger logger, Logger messageLogger) {
        try (BufferedReader inConsole = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            String nickname = inConsole.readLine().trim();
            out.println(nickname);
            String userMessage;
            do {
                userMessage = inConsole.readLine().trim();
                if (userMessage.equals("")) {
                    continue;
                }
                out.println(userMessage);
                if (!userMessage.equals(EXIT_COMMAND.getValue())) {
                    messageLogger.logFile(getUserDesign(String.format(NICKNAME_MESSAGE.getMsg(), nickname,
                            userMessage)));
                }
            } while (!userMessage.equals(EXIT_COMMAND.getValue()));
        } catch (IOException e) {
            logger.logFileOut(getSystemDesign(ERROR_INPUT_OUTPUT.getMsg() + e));
        }
    }

    private static void runWriterMessage(PrintWriter out, Logger logger, Logger messageLogger) {
        Thread writerMessage = new Thread(null, () -> writeMessage(out, logger, messageLogger));
        writerMessage.setDaemon(true);
        writerMessage.start();
    }

    public static void main(String[] args) {
        Settings settings = Settings.getSettings();
        Logger logger = new Logger(settings.getNameClientLog());
        Logger messageLogger = new Logger(settings.getNameClientMessageLog());
        try (Socket clientSocket = new Socket(settings.getHost(), settings.getPort());
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),
                     StandardCharsets.UTF_8))) {
            logger.logFile(getSystemDesign(CONNECTING_SERVER.getMsg()));
            runWriterMessage(out, logger, messageLogger);
            readMessage(in, messageLogger);
        } catch (IOException e) {
            logger.logFileOut(getSystemDesign(ERROR_INPUT_OUTPUT.getMsg() + e));
        } finally {
            logger.logFile(getSystemDesign(DISCONNECTING_SERVER.getMsg()));
            logger.close();
            messageLogger.close();
        }
    }
}