package com.company.application.client;

import com.company.compressor.Compressor;
import com.company.config.ArchiveSettings;
import com.company.config.ConfigurationManager;
import com.company.file_worker.MD5Executor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.HashMap;


public class CommandClone implements ICommand {

    CommandClone(String user, String repository, String path, String flags, byte[] data){
        m_user = user;
        m_repository = repository;
        m_path = path;
        m_flags = flags;
        m_data = data;
    }

    @Override
    public void execute() throws Exception {
        if(m_flags.equals("current")){
            ConfigurationManager.setSection(new ArchiveSettings(m_path));
            FileUtils.cleanDirectory(new File(((ArchiveSettings)ConfigurationManager.getSection("Archive")).Name));
        } else {
            ConfigurationManager.setSection(new ArchiveSettings(m_path + File.separator +  m_repository));
        }

        var path = ((ArchiveSettings)ConfigurationManager.getSection("Archive")).Name;
        var hashes = new HashMap<String, String>();

        for (var e : Compressor.decompressFile(m_data).entrySet()) {
            var file = new File(path + File.separator + e.getKey());
            var md5  = new MD5Executor();
            FileUtils.writeByteArrayToFile(file, e.getValue());
            hashes.put(e.getKey(), md5.process(file));
            System.out.println(String.format("Clone: %s", e.getKey()));
        }
        var conf = new ConfigRepository(m_repository, hashes);
        ConfigRepository.save(conf, path + File.separator + ".~config.json");
        System.out.println(String.format("Success clone. User: '%s' Repository: '%s'", m_user, m_repository));
    }

    private String m_user;
    private String m_repository;
    private String m_path;
    private String m_flags;
    private byte[] m_data;

}
