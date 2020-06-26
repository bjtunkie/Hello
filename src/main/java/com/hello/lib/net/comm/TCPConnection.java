package com.hello.lib.net.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TCPConnection {
    static final int TIME_TO_LIVE = 1000 * 60 * 2; // 2 minutes to live.
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final Socket socket;
    private final ThreadPool<? extends ZThread> tFactory;
    private final Thread thread;
    private final byte[] buffer = new byte[Constant.BUFFER_SIZE];

    private String associatedUniqueID;


    public <T extends ZThread> TCPConnection(Socket socket, ThreadPool<T> threadFactory) {
        this("", socket, threadFactory);
    }

    public <T extends ZThread> TCPConnection(String uniqueID, Socket socket, ThreadPool<T> threadFactory) {
        this.socket = socket;
        InputStream is;
        OutputStream os;
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            is = null;
            os = null;
            close();
        }

        inputStream = is;
        outputStream = os;

        tFactory = threadFactory;
        thread = new Thread(this::receive);
        thread.start();

    }

    public InetAddress getAddress() {
        return socket.getInetAddress();
    }


    public void send(byte[] data) {
        try {
            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receive() {

        try {
            while (true) {
                int count = inputStream.read(buffer);
                if (count > 0) {

                    byte[] data = new byte[count];
                    System.arraycopy(buffer, 0, data, 0, count);
                    tFactory.getThread().readInputFromConnection(data, this);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            /**
             * Socket disconnected.
             */
            close();
        }

    }


    public boolean isOpen() {
        return socket.isConnected();
    }

    public String getAssociatedUniqueID() {
        return associatedUniqueID;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TCPConnection that = (TCPConnection) o;
        return hashCode() == that.hashCode();
    }

}
