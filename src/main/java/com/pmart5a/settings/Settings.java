package com.pmart5a.settings;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Properties;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static com.pmart5a.enums.ErrorMsg.*;
import static com.pmart5a.enums.ParametersInt.*;
import static com.pmart5a.enums.ParametersStrings.*;
import static com.pmart5a.enums.SystemMsg.*;
import static com.pmart5a.servaces.MessageDesigner.getSystemDesign;

public class Settings {

    private final int port;
    private final String host;
    private final String nameServerLog;
    private final String nameServerMessageLog;
    private final String nameClientLog;
    private final String nameClientMessageLog;
    private static PrintWriter outConsole;
    private static Settings settings = null;

    private Settings(int port, String host, String nameServerLog, String nameServerMessageLog, String nameClientLog,
                     String nameClientMessageLog) {
        this.port = port;
        this.host = host;
        this.nameServerLog = nameServerLog;
        this.nameServerMessageLog = nameServerMessageLog;
        this.nameClientLog = nameClientLog;
        this.nameClientMessageLog = nameClientMessageLog;
        outConsole = new PrintWriter(System.out, true, StandardCharsets.UTF_8);
    }

    public static Settings getSettings() {
        if (settings == null) {
            ArrayList<String> settingsList = getListSettings();
            settings = new Settings(Integer.parseInt(settingsList.get(0)), settingsList.get(1), settingsList.get(2),
                    settingsList.get(3), settingsList.get(4), settingsList.get(5));
        }
        return settings;
    }

    private static ArrayList<String> getListSettings() {
        Properties presetsDefault = new Properties();
        fillPresetsDefault(presetsDefault);
        String fullNameFileSettings = SETTINGS_DIRECTORY.getValue() + SETTINGS_FILE.getValue();
        Path pathFileSettings = Paths.get(fullNameFileSettings);
        if (Files.notExists(pathFileSettings, NOFOLLOW_LINKS)) {
            return getListSettingsDefault(presetsDefault, fullNameFileSettings);
        } else {
            return getListSettingsFile(presetsDefault, pathFileSettings);
        }
    }

    private static void fillPresetsDefault(Properties presetsDefault) {
        presetsDefault.setProperty(KEY_PORT.getValue(), DEFAULT_PORT.getValue());
        presetsDefault.setProperty(KEY_HOST.getValue(), DEFAULT_HOST.getValue());
        presetsDefault.setProperty(KEY_NAME_SERVER_LOG.getValue(), DEFAULT_NAME_SERVER_LOG.getValue());
        presetsDefault.setProperty(KEY_NAME_SERVER_MESSAGE_LOG.getValue(), DEFAULT_NAME_SERVER_MESSAGE_LOG.getValue());
        presetsDefault.setProperty(KEY_NAME_CLIENT_LOG.getValue(), DEFAULT_NAME_CLIENT_LOG.getValue());
        presetsDefault.setProperty(KEY_NAME_CLIENT_MESSAGE_LOG.getValue(), DEFAULT_NAME_CLIENT_MESSAGE_LOG.getValue());
    }

    private static ArrayList<String> getListSettingsDefault(Properties presetsDefault, String fullNameFileSettings) {
        System.out.println(getSystemDesign(SETTINGS_FILE_NOT_FOUND.getMsg()));
        createSettingsFile(presetsDefault, fullNameFileSettings);
        ArrayList<String> listSettings = new ArrayList<>();
        listSettings.add(DEFAULT_PORT.getValue());
        listSettings.add(DEFAULT_HOST.getValue());
        listSettings.add(DEFAULT_NAME_SERVER_LOG.getValue());
        listSettings.add(DEFAULT_NAME_SERVER_MESSAGE_LOG.getValue());
        listSettings.add(DEFAULT_NAME_CLIENT_LOG.getValue());
        listSettings.add(DEFAULT_NAME_CLIENT_MESSAGE_LOG.getValue());
        return listSettings;
    }

    private static ArrayList<String> getListSettingsFile(Properties presetsDefault, Path pathFileSettings) {
        ArrayList<String> listSettings = new ArrayList<>();
        try (InputStream fileIn = Files.newInputStream(pathFileSettings)) {
            Properties presets = new Properties(presetsDefault);
            presets.load(fileIn);
            String port = presets.getProperty(KEY_PORT.getValue());
            if (!isPortValue(port)) {
                System.out.println(getSystemDesign(INVALID_PORT_VALUE.getMsg()));
                port = DEFAULT_PORT.getValue();
            }
            listSettings.add(port);
            listSettings.add(presets.getProperty(KEY_HOST.getValue()));
            listSettings.add(presets.getProperty(KEY_NAME_SERVER_LOG.getValue()));
            listSettings.add(presets.getProperty(KEY_NAME_SERVER_MESSAGE_LOG.getValue()));
            listSettings.add(presets.getProperty(KEY_NAME_CLIENT_LOG.getValue()));
            listSettings.add(presets.getProperty(KEY_NAME_CLIENT_MESSAGE_LOG.getValue()));
        } catch (NumberFormatException e) {
            outConsole.println(getSystemDesign(ERROR_TYPE_CONVERSION.getMsg() + e));
        } catch (IOException e) {
            outConsole.println(getSystemDesign(ERROR_INPUT_OUTPUT.getMsg() + e));
        }
        return listSettings;
    }

    private static void createSettingsFile(Properties presetsDefault, String fullNameFileSettings) {
        try (FileWriter fileWriter = new FileWriter(fullNameFileSettings, StandardCharsets.UTF_8)) {
            presetsDefault.store(fileWriter, COMMENT_SETTINGS_FILE.getValue());
        } catch (IOException e) {
            outConsole.println(getSystemDesign(ERROR_INPUT_OUTPUT.getMsg() + e));
        }
    }

    private static boolean isPortValue(String port) {
        int symbol;
        for (int i = 0; i < port.length(); i++) {
            symbol = port.charAt(i);
            if (symbol < MIN_CHARACTER_CODE.getValue() || symbol > MAX_CHARACTER_CODE.getValue()) {
                return false;
            }
        }
        return true;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getNameServerLog() {
        return nameServerLog;
    }

    public String getNameServerMessageLog() {
        return nameServerMessageLog;
    }

    public String getNameClientLog() {
        return nameClientLog;
    }

    public String getNameClientMessageLog() {
        return nameClientMessageLog;
    }
}