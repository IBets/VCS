package com.company.application.cli;

import com.beust.jcommander.Parameters;
import com.company.application.client.CommandFactory;
import com.company.application.client.ConfigRepository;
import com.company.application.command_package.CommandPackageCommit;
import com.company.application.network.NetWorkUtils;
import com.company.compressor.Compressor;
import com.company.config.ArchiveSettings;
import com.company.config.ClientSettings;
import com.company.config.ConfigurationManager;
import com.company.file_worker.FileWorker;
import com.company.file_worker.MD5Executor;
import javafx.util.Pair;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

@Parameters(commandNames = { "commit" }, commandDescription = "Commit command")
public class CommandCommit implements ICommand {
    @Override
    public void execute() {
        try(var sock = new Socket()) {
            var clientDesc  = (ClientSettings)ConfigurationManager.getSection("Client");
            var archiveDesc = (ArchiveSettings)ConfigurationManager.getSection("Archive");
            var repository  = this.loadActualFiles(archiveDesc.Name);
            var reposName   = ConfigRepository.load(archiveDesc.Name + File.separator + ".~config.json").Name;
            sock.connect(new InetSocketAddress(clientDesc.Host, clientDesc.Port));
            NetWorkUtils.send(new CommandPackageCommit(clientDesc.User, reposName, repository.getKey(), repository.getValue()), sock);
            CommandFactory.createInstance(NetWorkUtils.receive(sock)).execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "commit";
    }

    private Pair<String[], byte[]> loadActualFiles(String fileName) throws Exception {
        var fileWorker = new FileWorker(fileName, true);
        var hashesNew = fileWorker.execute(new MD5Executor());
        var hashesOld = ConfigRepository.load(fileName + File.separator + ".~config.json");
        var delta = new ArrayList<String>();

        hashesNew.forEach((k, v)->{
            if(hashesOld.Hashes.containsKey(k)) {
                if (!hashesOld.Hashes.get(k).equals(v))
                    delta.add(k);
            } else{
                delta.add(k);
            }
        });

        var conf = new ConfigRepository(hashesOld.Name, hashesNew);
        ConfigRepository.save(conf, fileName + File.separator + ".~config.json");

        var actual = new ArrayList<String>();
        hashesNew.forEach((k, v)->actual.add(k));

        for(var e: delta)
            System.out.println(String.format("Commit file: %s", e));
        return new Pair<>(actual.stream().toArray(String[]::new),
                Compressor.compressFiles(delta.stream().map((e)->new File(fileName + File.separator + e)).toArray(File[]::new)));
    }
}
