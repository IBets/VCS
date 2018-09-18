package com.company.application.server.provider;

import com.company.compressor.Compressor;
import com.company.config.ArchiveSettings;
import com.company.config.ConfigurationManager;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


public class FolderProvider implements IDataProvider {

    public FolderProvider(String path) {
        m_path = path;
    }

    @Override
    public void createRepository(String user, String repositoryName, Version version) throws IOException {
        var path = new File(m_path + File.separator + repositoryName);
        var time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if(!path .exists()){
            if(path.mkdir()) {
                var rep = new ConfigRepository(repositoryName, version);
                var ver = new ConfigVersion(new String[0], version, time, user);
                ConfigRepository.save(rep, path + File.separator + ".~config.json");
                ConfigVersion.save(ver, path +File.separator + version + File.separator + ".~version.json");
            } else throw new IOException("Error create repository");
        } else throw new IOException("Error repository exist");
    }

    @Override
    public void addRepositoryNewVersion(String user, String repositoryName, byte[] data, String[] actualFiles) throws IOException {
        var configRepository = ConfigRepository.load(m_path + File.separator + repositoryName + File.separator + ".~config.json");
        var pathNewVersion = m_path + File.separator + repositoryName + File.separator + Version.generateNextVersion(configRepository.CurrentVersion).toString();
        var time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));



        if (new File(pathNewVersion).mkdir()) {
            for (var file :  Compressor.decompressFile(data).entrySet())
                FileUtils.writeByteArrayToFile(new File(pathNewVersion + File.separator + file.getKey()), file.getValue());

            configRepository.CurrentVersion = Version.generateNextVersion(configRepository.CurrentVersion);
            configRepository.Versions.add(configRepository.CurrentVersion);
            var configVersion = new ConfigVersion(actualFiles, configRepository.CurrentVersion, time, user);
            ConfigRepository.save(configRepository, m_path + repositoryName + File.separator + ".~config.json");
            ConfigVersion.save(configVersion, pathNewVersion + File.separator + ".~version.json");
        } else throw new IOException("Error created new version");
    }



    @Override
    public File[] getRepositoryByActualVersion(String user, String repositoryName) throws IOException {

        var config = ConfigRepository.load(m_path + File.separator + repositoryName + File.separator + ".~config.json");
        var version = ConfigVersion.load(m_path + File.separator + repositoryName + File.separator + config.CurrentVersion + File.separator + ".~version.json");
        return this.getRepositoryByVersion(user, repositoryName, version.Version);

    }

    @Override
    public File[] getRepositoryByVersion(String user, String repositoryName, Version version) throws IOException {

        var config = ConfigVersion.load(m_path + File.separator + repositoryName + File.separator + version + File.separator + ".~version.json");
        var temp = new ArrayList<String>();
        var result = new ArrayList<File>();

        Consumer<Version> func = (ver)->{
            try {
                Files.walk(Paths.get(m_path + File.separator + repositoryName + File.separator + ver))
                        .filter(Files::isRegularFile)
                        .forEach((e)-> {
                            var fileName = Paths.get(m_path + File.separator + repositoryName + File.separator + ver.toString()).relativize(e).toString();
                            if(Arrays.asList(config.Files).contains(fileName) && !temp.contains(fileName)) {
                                if(!e.toFile().getName().startsWith(".~")) {
                                    temp.add(fileName);
                                    result.add(e.toFile());
                                }
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        while (!version.equals(new Version(0, 0, 0))){
            func.accept(version);
            version = Version.generatePrevVersion(version);
        }
        func.accept(new Version(0, 0, 0));

        return result.stream().toArray(File[]::new);
    }
    private String m_path;
}
