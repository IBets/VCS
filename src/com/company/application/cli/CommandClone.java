package com.company.application.cli;

import com.beust.jcommander.*;
import com.company.application.client.CommandFactory;
import com.company.application.command_package.CommandPackageClone;
import com.company.application.network.NetWorkUtils;
import com.company.config.ClientSettings;
import com.company.config.ConfigurationManager;
import java.net.*;


@Parameters(commandNames = { "clone" }, commandDescription = "Clone command")
public class CommandClone implements ICommand {
    @Override
    public void execute() {
        try(var sock = new Socket()) {
            var clientDesc = (ClientSettings)ConfigurationManager.getSection("Client");
            sock.connect(new InetSocketAddress(clientDesc.Host, clientDesc.Port));
            NetWorkUtils.send(new CommandPackageClone(clientDesc.User, m_repositoryName, m_path, m_flags), sock);
            CommandFactory.createInstance(NetWorkUtils.receive(sock)).execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public String getName() {
        return "clone";
    }

    @Parameter(names = {"-f", "--flags"},  description = "Path")
    private String m_flags = "";

    @Parameter(names = {"-p", "--path"},  description = "Path", required = true)
    private String m_path = "";

    @Parameter(names = {"-r", "--repository"},  description = "Repository name", required = true)
    private String m_repositoryName = "";
}
