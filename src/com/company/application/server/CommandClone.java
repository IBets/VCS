package com.company.application.server;


import com.company.application.command_package.CommandPackageClone;
import com.company.application.command_package.CommandPackage;
import com.company.application.command_package.CommandPackageError;
import com.company.application.server.provider.FolderProvider;
import com.company.compressor.Compressor;
import com.company.config.ArchiveSettings;
import com.company.config.ConfigurationManager;


public class CommandClone implements ICommand {
    CommandClone(String user, String repository, String path, String flags){
        m_user = user;
        m_repository = repository;
        m_path = path;
        m_flags = flags;
    }

    @Override
    public CommandPackage execute() {
        try {
            var provider = new FolderProvider(((ArchiveSettings)ConfigurationManager.getSection("Archive")).Name);
            return new CommandPackageClone(m_user, m_repository, m_path, m_flags, Compressor.compressFilesEx(provider.getRepositoryByActualVersion(m_user, m_repository)));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommandPackageError(e.getMessage());
        }
    }

    private String m_user;
    private String m_repository;
    private String m_path;
    private String m_flags;

}
