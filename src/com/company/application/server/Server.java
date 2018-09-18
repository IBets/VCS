package com.company.application.server;
import com.company.threading.ThreadDispatcher;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;



public class Server {

    public Server(String host, int port) throws IOException {
        m_socket = new ServerSocket(port, 0, InetAddress.getByName(host));
        m_isRun  = new AtomicBoolean(false);
    }

    public void run() {
        m_logger.info(String.format("run server: %s:%s ", m_socket.getInetAddress().toString(), m_socket.getLocalPort()));
        m_isRun.set(true);
        try {
            while (m_isRun.get()){
                try {
                    var client = m_socket.accept();
                    ThreadDispatcher.getInstance().add(new ClientHandler(client));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                if (m_socket != null) {
                    m_socket.close();
                    ThreadDispatcher.getInstance();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void shutdown() {
        m_logger.info("shutdown");
        m_isRun.set(false);
    }

    private AtomicBoolean m_isRun;
    private ServerSocket m_socket;
    private static Logger m_logger = Logger.getLogger(Server.class.getName());

}