package com.pmart5a.enums;

public enum ErrorMsg {
    ERROR_INPUT_OUTPUT("Ошибка ввода-вывода "),
    ERROR_TYPE_CONVERSION("Ошибка преобразвания типов для поля 'порт' ");

    private String msg;

    ErrorMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg(){
        return msg;
    }
}