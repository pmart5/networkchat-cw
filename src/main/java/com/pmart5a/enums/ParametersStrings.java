package com.pmart5a.enums;

public enum ParametersStrings {
    DEFAULT_CLIENT_NICKNAME("noName"),
    NAME_SERVICE_TREAD("ServiceThread"),
    EXIT_COMMAND("/exit"),
    NAME_DIRECTORY_LOG("/logs/"),
    PARENT_DIRECTORY_LOG(System.getProperty("user.dir")),
    SETTINGS_DIRECTORY(System.getProperty("user.dir")),
    SETTINGS_FILE("/settings.properties"),
    KEY_PORT("port"),
    KEY_HOST("host"),
    KEY_NAME_SERVER_LOG("nameServerLog"),
    KEY_NAME_SERVER_MESSAGE_LOG("nameServerMessageLog"),
    KEY_NAME_CLIENT_LOG("nameClientLog"),
    KEY_NAME_CLIENT_MESSAGE_LOG("nameClientMessageLog"),
    DEFAULT_PORT("24321"),
    DEFAULT_HOST("localhost"),
    DEFAULT_NAME_SERVER_LOG("server.log"),
    DEFAULT_NAME_SERVER_MESSAGE_LOG("servermsg.log"),
    DEFAULT_NAME_CLIENT_LOG("client.log"),
    DEFAULT_NAME_CLIENT_MESSAGE_LOG("clientmsg.log"),
    COMMENT_SETTINGS_FILE("""
            The parameter values after the '=' sign can be replaced with their own.
            For example:
              server settings:
                port=8788 - port of the chat server
                nameServerLog=slog.log - server system message log
                nameServerMessageLog=smessage.log - full chat log on the server
              settings for the client:
                port=8788 - port of the chat server
                host=10.101.40.88 - IP-address of the chat server
                nameClientLog=clog.log - client system message log
                nameClientMessageLog=cmessage.log - client's chat log
             If there is no string with the parameter, the default values are used.
            """);

    private String value;

    ParametersStrings(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}