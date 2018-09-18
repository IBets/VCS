package com.company.application.client;

import com.company.compressor.Compressor;
import com.company.config.ArchiveSettings;
import com.company.config.ConfigurationManager;
import com.company.file_worker.MD5Executor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class CommandRevert implements ICommand {

    CommandRevert(String user, String repository, String version, String flags, byte[] data){
        m_user = user;
        m_repository = repository;
        m_version = version;
        m_flags = flags;
        m_data = data;
    }

    @Override
    public void execute() throws IOException {
        var path   = ((ArchiveSettings)ConfigurationManager.getSection("Archive")).Name;
        var conf = ConfigRepository.load(path + File.separator + ".~config.json");
        var md5  = new MD5Executor();
        if(m_flags.equals("hard")) {
            FileUtils.cleanDirectory(new File(path));
            for (var e : Compressor.decompressFile(m_data).entrySet()) {
                var file = new File(path + File.separator + e.getKey());
                FileUtils.writeByteArrayToFile(file, e.getValue());
                conf.Hashes.put(e.getKey(), md5.process(file));
                System.out.println(String.format("Revert: %s", e.getKey()));
            }
            ConfigRepository.save(conf, path + File.separator + ".~config.json");
            System.out.println(String.format("Success revert %s. User: '%s' Repository: '%s'", m_version, m_user, m_repository));
        } else {
            for (var e : Compressor.decompressFile(m_data).entrySet()) {
                if(!conf.Hashes.containsKey(e.getKey())) {
                    var file = new File(path + File.separator + e.getKey());
                    FileUtils.writeByteArrayToFile(file, e.getValue());
                    conf.Hashes.put(e.getKey(), md5.process(file));
                    System.out.println(String.format("Revert: %s", e.getKey()));
                }
            }
            ConfigRepository.save(conf, path + File.separator + ".~config.json");
            System.out.println(String.format("Success revert %s. User: '%s' Repository: '%s'", m_version, m_user, m_repository));
        }
    }

    private String m_user;
    private String m_repository;
    private String m_version;
    private String m_flags;
    private byte[] m_data;
}
