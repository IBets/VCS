package com.company.application.client;

import com.company.compressor.Compressor;
import com.company.config.ArchiveSettings;
import com.company.config.ConfigurationManager;
import com.company.file_worker.MD5Executor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CommandUpdate implements ICommand {

    CommandUpdate(String user, String repository, byte[] data){
        m_user = user;
        m_repository = repository;
        m_data = data;
    }


    @Override
    public void execute() throws IOException {
        var path   = ((ArchiveSettings)ConfigurationManager.getSection("Archive")).Name;
        var conf = ConfigRepository.load(path + File.separator + ".~config.json");
        var md5  = new MD5Executor();
        for (var e : Compressor.decompressFile(m_data).entrySet()) {
            var file = new File(path + File.separator + e.getKey());
            FileUtils.writeByteArrayToFile(file, e.getValue());
            conf.Hashes.put(e.getKey(), md5.process(file));
            System.out.println(String.format("Update: %s", e.getKey()));
        }
        ConfigRepository.save(conf, path + File.separator + ".~config.json");
        System.out.println(String.format("Success update. User: '%s' Repository: '%s'", m_user, m_repository));
    }

    private String m_user;
    private String m_repository;
    private byte[] m_data;
}
