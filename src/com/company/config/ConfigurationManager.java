package com.company.config;

import com.company.application.Application;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {
    private static Map<String, Object> m_data  = new HashMap<>();
    public synchronized static <T> T getSection(String section)  {

        var value = (T) m_data.get(section);
        if(value == null) {
            try { load(System.getProperty("config"), section);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (T)m_data.get(section);
    }

    public synchronized static void setSection(ArchiveSettings settings) throws IOException {
        save(System.getProperty("config"), settings);
    }

    private static void load(String fileName, String section) throws IOException {

        var content = FileUtils.readFileToString(new File(fileName), "utf-8");
        var json = new JSONObject(content);

        switch (section){
            case "Application":{
                var applicationSettings = new ApplicationSettings(json.getJSONObject("Application").getEnum(ApplicationSettings.AppType.class, "Type"));
                m_data.put("Application", applicationSettings);
                break;
            }
            case "Server":{
                var serverSettings = new ServerSettings();
                serverSettings.Host = json.getJSONObject("Server").getString("Host");
                serverSettings.Port = json.getJSONObject("Server").getInt("Port");
                m_data.put("Server", serverSettings);
                break;
            }
            case "Client":{
                var clientSettings = new ClientSettings();
                clientSettings.User = json.getJSONObject("Client").getString("User");
                clientSettings.Host = json.getJSONObject("Client").getString("Host");
                clientSettings.Port = json.getJSONObject("Client").getInt("Port");

                m_data.put("Client", clientSettings);
                break;
            }
            case "Thread":{
                var threadSettings = new ThreadSettings();
                threadSettings.Name = json.getJSONObject("Thread").getString("Name");
                m_data.put("Thread", threadSettings);
                break;
            }
            case "Archive":{
                var archiveSettings = new ArchiveSettings(json.getJSONObject("Archive").getString("Name"));
                m_data.put("Archive", archiveSettings);
                break;
            }
        }
    }

    private  static void save(String fileName, ArchiveSettings section) throws IOException {
        m_data.put("Archive", section);
        var json = new JSONObject( FileUtils.readFileToString(new File(fileName), "utf-8"));
        json.put("Archive", new JSONObject().put("Name", section.Name));
        FileUtils.write(new File(fileName), json.toString(), "utf-8");
    }
}