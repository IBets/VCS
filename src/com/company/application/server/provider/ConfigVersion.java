package com.company.application.server.provider;


import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;

public class ConfigVersion {

    ConfigVersion(String[] files, Version version, String time, String user){
        Files = files;
        Version = version;
        Time = time;
        User = user;
    }

    public static void save(ConfigVersion config, String fileName) throws IOException {
        FileUtils.write(new File(fileName), new GsonBuilder().create().toJson(config), "utf-8");
    }

    public static ConfigVersion load(String fileName) throws IOException {
        return new GsonBuilder().create().fromJson(FileUtils.readFileToString(new File(fileName), "utf-8"), ConfigVersion.class);
    }

    @Override
    public String toString() {
        return String.format("User: %s Time: %s Version: %s", User, Time, Version);
    }

    public String   User;
    public String   Time;
    public String[] Files;
    public Version  Version;
}
