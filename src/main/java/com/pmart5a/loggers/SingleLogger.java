package com.pmart5a.loggers;

public class SingleLogger {

    private static SingleLogger singleLogger = null;
    private final Logger logger;

    private SingleLogger(Logger logger) {
        this.logger = logger;
    }

    public static SingleLogger getLogger(String fileName) {
        if (singleLogger == null) {
            singleLogger = new SingleLogger(new Logger(fileName));
        }
        return singleLogger;
    }

    public void logFile(String message) {
        logger.logFile(message);
    }

    public void logFileOut(String message) {
        logger.logFileOut(message);
    }

    public void close() {
        logger.close();
    }
}