package com.company.application.cli;

import com.beust.jcommander.Parameters;
import com.company.application.client.CommandFactory;
import com.company.application.client.ConfigRepository;
import com.company.application.command_package.CommandPackageAdd;
import com.company.application.command_package.CommandPackageUpdate;
import com.company.application.network.NetWorkUtils;
import com.company.config.ArchiveSettings;
import com.company.config.ClientSettings;
import com.company.config.ConfigurationManager;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;

@Parameters(commandNames = { "update" }, commandDescription = "Update command")
public class CommandUpdate implements ICommand {
    @Override
    public void execute() {
        try(var sock = new Socket()) {
            var clientDesc  = (ClientSettings)ConfigurationManager.getSection("Client");
            var archiveDesc = (ArchiveSettings)ConfigurationManager.getSection("Archive");
            var reposName   = ConfigRepository.load(archiveDesc.Name + File.separator + ".~config.json").Name;
            sock.connect(new InetSocketAddress(clientDesc.Host, clientDesc.Port));
            NetWorkUtils.send(new CommandPackageUpdate(clientDesc.User, reposName), sock);
            CommandFactory.createInstance(NetWorkUtils.receive(sock)).execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "update";
    }
}
