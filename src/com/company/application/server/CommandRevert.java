package com.company.application.server;

import com.company.application.command_package.CommandPackage;
import com.company.application.command_package.CommandPackageError;
import com.company.application.command_package.CommandPackageRevert;
import com.company.application.server.provider.FolderProvider;
import com.company.application.server.provider.Version;
import com.company.compressor.Compressor;
import com.company.config.ArchiveSettings;
import com.company.config.ConfigurationManager;

public class CommandRevert  implements ICommand {

    CommandRevert(String user, String repository, String version, String flags){
        m_user = user;
        m_repository = repository;
        m_version = version;
        m_flags = flags;
    }

    @Override
    public CommandPackage execute() {
        try {
            var provider = new FolderProvider(((ArchiveSettings)ConfigurationManager.getSection("Archive")).Name);
            if(!m_version.equals("")) {
                var data = Compressor.compressFilesEx(provider.getRepositoryByVersion(m_user, m_repository, Version.fromString(m_version)));
                return new CommandPackageRevert(m_user, m_repository, m_version, m_flags, data);
            } else {
                var data = Compressor.compressFilesEx(provider.getRepositoryByActualVersion(m_user, m_repository));
                return new CommandPackageRevert(m_user, m_repository, m_version, "hard", data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommandPackageError(e.getMessage());
        }
    }

    private String m_user;
    private String m_repository;
    private String m_version;
    private String m_flags;
}
