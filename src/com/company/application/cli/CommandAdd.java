package com.company.application.cli;

import com.beust.jcommander.*;
import com.company.application.client.CommandFactory;
import com.company.application.command_package.CommandPackageAdd;
import com.company.application.network.NetWorkUtils;
import com.company.config.ClientSettings;
import com.company.config.ConfigurationManager;


import java.net.InetSocketAddress;
import java.net.Socket;


@Parameters(commandNames = {"add"}, commandDescription = "Add command")
public class CommandAdd implements ICommand {
    @Override
    public void execute(){
        try(var sock = new Socket()) {
            var clientDesc = (ClientSettings)ConfigurationManager.getSection("Client");
            sock.connect(new InetSocketAddress(clientDesc.Host, clientDesc.Port));
            NetWorkUtils.send(new CommandPackageAdd(clientDesc.User, m_repositoryName), sock);
            CommandFactory.createInstance(NetWorkUtils.receive(sock)).execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public String getName() {
        return "add";
    }

    @Parameter(names = {"-r", "--r"},  description = "Create repository", required = true)
    private String m_repositoryName = "";
}
