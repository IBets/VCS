package com.company.application;

import com.company.config.*;
import com.company.application.server.Server;

import java.io.IOException;

public class ApplicationServer extends Application {
    public ApplicationServer () throws IOException {
        var setting = (ServerSettings)ConfigurationManager.getSection("Server");
        m_server = new Server(setting.Host, setting.Port);        
    }

    @Override
    public void run() {
        m_server.run();
    }

    @Override
    public void shutdown() {
        m_server.shutdown();
    }

    private Server m_server;

}
