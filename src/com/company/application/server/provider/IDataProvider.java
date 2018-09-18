package com.company.application.server.provider;

import java.io.File;
import java.io.IOException;


public interface IDataProvider {
    void createRepository(String user, String repositoryName, Version version) throws IOException;

    void addRepositoryNewVersion(String user, String repName, byte[] files, String[] actualFiles) throws IOException;

    File[] getRepositoryByActualVersion(String user, String repName) throws IOException;

    File[] getRepositoryByVersion(String user, String repName, Version versionName) throws IOException;

}
