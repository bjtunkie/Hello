package com.hello.lib.net.comm;

import com.hello.lib.net.proto.BaseMessage;
import com.hello.lib.net.proto.MessageResponse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

final class StagingArea {
    private final ThreadPool<? extends ZThread> threadFactory;
    private final ObjectPool<String, MessageResponse> requestPool;
    private final ObjectPool<String, TCPConnection> connectionPool;
    private final Thread TCPServer = new Thread(this::acceptConnections);
    private final boolean startServer;

    <T extends ZThread> StagingArea(boolean enableServer, Class<T> threadClass, ObjectPool<String, MessageResponse> r, Object... threadParams) {
        connectionPool = new TCPConnectionPool();
        requestPool = r == null ? new TimedRequestPool() : r;
        threadFactory = new ThreadPool<>(threadClass, connectionPool, requestPool, threadParams);
        startServer = enableServer;
        TCPServer.start();
    }

    public <M extends BaseMessage> void send(M message, MessageResponse response) {
        ZThread t = threadFactory.getThread();
        t.assignOutMessage(message, response);

    }


    public ThreadPool<? extends ZThread> getThreadFactory() {
        return threadFactory;
    }

    private void acceptConnections() {

        final String host = "0.0.0.0";
        final int port = 3004;
        while (startServer) {
            try {
                ServerSocket serverSocket = new ServerSocket();
                serverSocket.bind(new InetSocketAddress(host, port));

                while (true) {
                    Socket socket = serverSocket.accept();
                    TCPConnection conn = new TCPConnection(socket, threadFactory);
                    connectionPool.insert(ObjectPool.DEFAULT_KEY, conn);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
