package com.pmart5a.enums;

public enum SystemMsg {
    SERVER_START("Сервер старт"),
    WAITING_CONNECTION("Ожидание подключения..."),
    SERVER_STOP("Сервер стоп"),
    CLIENT_CONNECTED("Клиент подключился: %s"),
    CLIENT_DISCONNECTED("Клиент отключился: %s"),
    LOST_CONNECTION_CLIENT("Потеряно соединение с клиентом: %s"),
    CONNECTING_SERVER("Клиент успешно подключился к серверу"),
    DISCONNECTING_SERVER("Клиент завершил работу"),
    SETTINGS_FILE_NOT_FOUND("Не найден файл настроек! В текущей директории будет создан файл настроек" +
            " settings.properties с параметрами по умолчанию."),
    INVALID_PORT_VALUE("Значение параметра порт в файле настроек некорректно. Будет использовано значение" +
            " по умолчанию: 24321.");

    private String msg;

    SystemMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}