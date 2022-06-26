package com.pmart5a.loggers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static com.pmart5a.enums.ErrorMsg.*;
import static com.pmart5a.enums.ParametersStrings.*;
import static com.pmart5a.servaces.MessageDesigner.getSystemDesign;
public class Logger {

    private BufferedWriter logFileWriter;
    private PrintWriter outConsole;
    private String pathDirectoryLog;
    private String pathLogFile;

    public Logger(String fileName) {
        try {
            pathDirectoryLog = PARENT_DIRECTORY_LOG.getValue() + NAME_DIRECTORY_LOG.getValue();
            Files.createDirectories(Paths.get(pathDirectoryLog));
            pathLogFile = pathDirectoryLog + fileName;
            logFileWriter = Files.newBufferedWriter(Paths.get(pathLogFile), StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            outConsole = new PrintWriter(System.out, true, StandardCharsets.UTF_8);
        } catch (IOException e) {
            outputErrorMessage(e);
        }
    }

    public String getPathDirectoryLog() {
        return pathDirectoryLog;
    }

    public String getPathLogFile() {
        return pathLogFile;
    }

    public void logFile(String message) {
        try {
            logFileWriter.write(message);
            logFileWriter.newLine();
            logFileWriter.flush();
        } catch (IOException e) {
            outputErrorMessage(e);
        }
    }

    public void logOut(String message) {
        outConsole.println(message);
    }

    public void logFileOut(String message) {
        logFile(message);
        logOut(message);
    }

    public void close() {
        try {
            logFileWriter.close();
            outConsole.close();
        } catch (IOException e) {
            outputErrorMessage(e);
        }
    }

    private void outputErrorMessage(IOException e) {
        outConsole.println(getSystemDesign(ERROR_INPUT_OUTPUT.getMsg() + e));
    }
}