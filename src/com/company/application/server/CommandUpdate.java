package com.company.application.server;

import com.company.application.command_package.CommandPackage;
import com.company.application.command_package.CommandPackageError;
import com.company.application.command_package.CommandPackageUpdate;
import com.company.application.server.provider.FolderProvider;
import com.company.compressor.Compressor;
import com.company.config.ArchiveSettings;
import com.company.config.ConfigurationManager;

public class CommandUpdate implements ICommand {

    CommandUpdate(String user, String repository){
        m_user = user;
        m_repository = repository;
    }

    @Override
    public CommandPackage execute() {
        try {
            var provider = new FolderProvider(((ArchiveSettings)ConfigurationManager.getSection("Archive")).Name);
            return new CommandPackageUpdate(m_user, m_repository, Compressor.compressFilesEx(provider.getRepositoryByActualVersion(m_user, m_repository)));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommandPackageError(e.getMessage());
        }
    }

    private String m_user;
    private String m_repository;
}
