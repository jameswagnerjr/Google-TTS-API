package net.wagnerjr.hassioapi.configuration;

import okio.BufferedSource;
import okio.Okio;
import okio.Source;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigHandler {
    private static Configuration configuration;

    public static Configuration getConfig(File configFile) {
        Source source = null;
        try {
            source = Okio.source(configFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedSource bufferedSource = Okio.buffer(source);

        try {
            configuration = Configuration.fromJsonBufferedSource(bufferedSource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configuration;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }
}
