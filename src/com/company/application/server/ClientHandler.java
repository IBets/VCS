package com.company.application.server;

import com.company.application.network.NetWorkUtils;
import com.company.threading.ThreadTask;
import java.net.Socket;

class ClientHandler extends ThreadTask {
    ClientHandler(Socket client){
        m_sock = client;
    }

    @Override
    public void execute() {
        try {
            var pack = CommandFactory.createInstance(NetWorkUtils.receive(m_sock)).execute();
            NetWorkUtils.send(pack, m_sock);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return String.format("ThreadClient %s", m_sock.getRemoteSocketAddress().toString());
    }

    private Socket m_sock;
}
