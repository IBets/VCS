package com.company.application.server;

import com.company.application.command_package.CommandPackage;
import com.company.application.command_package.CommandPackageLog;
import com.company.application.server.provider.ConfigRepository;
import com.company.application.server.provider.ConfigVersion;
import com.company.application.server.provider.Version;
import com.company.config.ArchiveSettings;
import com.company.config.ConfigurationManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class CommandLog implements ICommand {

    CommandLog(String user, String repository){
        m_user = user;
        m_repository = repository;
    }

    @Override
    public CommandPackage execute() throws IOException {

        var log = new StringBuilder();
        var path = ((ArchiveSettings)ConfigurationManager.getSection("Archive")).Name;
        var oldVersion = ConfigVersion.load(path + File.separator + m_repository + File.separator + new Version(0, 0, 0) + File.separator + ".~version.json");;
        for(var e :  ConfigRepository.load(path + File.separator + m_repository + File.separator + ".~config.json").Versions){
            var newVersion = ConfigVersion.load(path + File.separator + m_repository + File.separator + e + File.separator + ".~version.json");

            var deleteFiles = deltaFiles(oldVersion.Files, newVersion.Files);
            var createFiles = deltaFiles(newVersion.Files, oldVersion.Files);


            log.append(newVersion.toString()).append("\n");
            for(var item: deleteFiles) log.append(String.format("-- Delete: %s \n", item));
            for(var item: createFiles) log.append(String.format("-- Create: %s \n", item));
            oldVersion = newVersion;
        }

        return new CommandPackageLog(m_user, m_repository, log.toString());
    }

    String[] deltaFiles(String[] first, String[] second){

        var result = new ArrayList<String>();
        var firstList  = Arrays.asList(first);
        var secondList = Arrays.asList(second);

        firstList.forEach((e)->{
            if(!secondList.contains(e))
                result.add(e);
        });

        return result.stream().toArray(String[]::new);
    }

    private String m_user;
    private String m_repository;
}
