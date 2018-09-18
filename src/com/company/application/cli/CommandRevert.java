package com.company.application.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.company.application.client.CommandFactory;
import com.company.application.client.ConfigRepository;
import com.company.application.command_package.CommandPackageRevert;
import com.company.application.network.NetWorkUtils;
import com.company.config.ArchiveSettings;
import com.company.config.ClientSettings;
import com.company.config.ConfigurationManager;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;

@Parameters(commandNames = { "revert" }, commandDescription = "Revert command")
public class CommandRevert implements ICommand {
    @Override
    public void execute() {
        try(var sock = new Socket()) {
            var clientDesc  = (ClientSettings)ConfigurationManager.getSection("Client");
            var archiveDesc = (ArchiveSettings)ConfigurationManager.getSection("Archive");
            var reposName   = ConfigRepository.load(archiveDesc.Name + File.separator + ".~config.json").Name;
            sock.connect(new InetSocketAddress(clientDesc.Host, clientDesc.Port));
            NetWorkUtils.send(new CommandPackageRevert(clientDesc.User, reposName, m_version, m_flags), sock);
            CommandFactory.createInstance(NetWorkUtils.receive(sock)).execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "revert";
    }

    @Parameter(names = {"-v", "--version"},  description = "Version")
    private String m_version = "";

    @Parameter(names = {"-f", "--flags"},  description = "Flags")
    private String m_flags = "hard";
}
