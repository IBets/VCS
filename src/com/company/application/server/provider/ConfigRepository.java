package com.company.application.server.provider;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;


public class ConfigRepository {
    public ConfigRepository(String name, Version version){
        Name = name;
        CurrentVersion = version;
        Versions = new LinkedList<>();
        Versions.add(new Version(version));
    }

    public static void save(ConfigRepository config, String fileName) throws IOException {
        FileUtils.write(new File(fileName), new GsonBuilder().create().toJson(config), "utf-8");
    }
    public static ConfigRepository load(String fileName) throws IOException {
        return new GsonBuilder().create().fromJson(FileUtils.readFileToString(new File(fileName), "utf-8"), ConfigRepository.class);
    }

    public String              Name;
    public Version             CurrentVersion;
    public LinkedList<Version> Versions ;


}
