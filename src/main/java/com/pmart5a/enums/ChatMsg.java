package com.pmart5a.enums;

public enum ChatMsg {
    INPUT_NICKNAME("Введите Ваше имя: "),
    LOG_IN_TO_CHAT("%s, Вы вошли в чат. Всего участников: %d. (Для выхода: /exit)"),
    EXITING_CHAT("'%s' покинул чат. Всего участников: %d."),
    JOINING_CHAT("'%s' присоеденился к чату. Всего участников: %d."),
    NICKNAME_MESSAGE("%s: %s"),
    DISCONNECTED_CHAT("'%s' потерялся...:( Всего участников: %d.");

    private String  msg;

    ChatMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg(){
        return msg;
    }
}