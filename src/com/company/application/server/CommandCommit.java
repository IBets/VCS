package com.company.application.server;

import com.company.application.command_package.CommandPackage;
import com.company.application.command_package.CommandPackageCommit;
import com.company.application.server.provider.FolderProvider;
import com.company.config.ArchiveSettings;
import com.company.config.ConfigurationManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommandCommit implements ICommand {
    public CommandCommit(String user, String repository, String[] actualFiles, byte[] data){
        m_user = user;
        m_repository = repository;
        m_actualFiles = actualFiles;
        m_data = data;
    }
    @Override
    public CommandPackage execute() throws IOException {
        var provider = new FolderProvider(((ArchiveSettings)ConfigurationManager.getSection("Archive")).Name);
        provider.addRepositoryNewVersion(m_user, m_repository, m_data, m_actualFiles);
        return new CommandPackageCommit(m_user, m_repository);
    }
    private String   m_user;
    private String   m_repository;
    private String[] m_actualFiles;
    private byte[]   m_data;
}
