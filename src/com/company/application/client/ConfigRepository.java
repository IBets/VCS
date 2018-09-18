package com.company.application.client;

import com.company.application.server.provider.ConfigVersion;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ConfigRepository {
    public String Name;
    public Map<String, String> Hashes;
    public ConfigRepository(String name, Map<String, String> hashes){
        Name = name;
        Hashes = hashes;
    }
    public static void save(ConfigRepository  config, String fileName) throws IOException {
        FileUtils.write(new File(fileName), new GsonBuilder().create().toJson(config), "utf-8");
    }

    public static ConfigRepository load(String fileName) throws IOException {
        return new GsonBuilder().create().fromJson(FileUtils.readFileToString(new File(fileName), "utf-8"), ConfigRepository .class);
    }
}
