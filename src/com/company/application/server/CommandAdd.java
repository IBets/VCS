package com.company.application.server;

import com.company.application.command_package.CommandPackageAdd;
import com.company.application.command_package.CommandPackage;
import com.company.application.command_package.CommandPackageError;
import com.company.application.server.provider.FolderProvider;
import com.company.application.server.provider.Version;
import com.company.config.ArchiveSettings;
import com.company.config.ConfigurationManager;

public class CommandAdd implements ICommand {

    CommandAdd(String user, String repository){
        m_user = user;
        m_repository = repository;
    }

    @Override
    public CommandPackage execute() {

        try {
            var provider = new FolderProvider(((ArchiveSettings)ConfigurationManager.getSection("Archive")).Name);
            provider.createRepository(m_user, m_repository, new Version(0, 0, 0));
            return new CommandPackageAdd(m_user, m_repository);
        } catch (Exception e){
            return new CommandPackageError(e.getMessage());
        }

    }

    private String m_user;
    private String m_repository;
}
