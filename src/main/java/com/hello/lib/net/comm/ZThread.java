package com.hello.lib.net.comm;


import com.hello.lib.net.proto.BaseMessage;
import com.hello.lib.net.proto.MessageResponse;
import com.hello.lib.net.proto.Util;
import com.hello.lib.praksh.Chef;
import com.hello.lib.praksh.EasyChef;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public abstract class ZThread extends Thread implements Util {
    private static final byte TASK_RECEIVE = 1;
    private static final byte TASK_SEND = 2;
    private static final byte TASK_CONNECT = 3;

    protected final int hashCode;
    protected final ThreadPool<? extends ZThread> factory;
    protected final Chef serializer;
    private final Object lock = new Object();
    private final List<Object> params;

    protected final ObjectPool<String, TCPConnection> connectionPool;
    protected final ObjectPool<String, MessageResponse> requestPool;
    private byte taskID = Byte.MIN_VALUE;

    public <T extends ZThread> ZThread(ThreadPool<T> factory, ObjectPool<String, TCPConnection> p, ObjectPool<String, MessageResponse> r) {
        this(factory, p, r, new EasyChef());
    }

    public <T extends ZThread> ZThread(ThreadPool<T> factory, ObjectPool<String, TCPConnection> p, ObjectPool<String, MessageResponse> r, Chef serializer) {
        this.hashCode = Objects.hash(this, lock);
        this.factory = factory;
        this.serializer = serializer;
        this.requestPool = r;
        this.connectionPool = p;
        this.params = new ArrayList<>(5);
    }


    public final <T extends BaseMessage> void assignOutMessage(T baseMessage, MessageResponse response) {
        requestPool.insert(baseMessage.requestID(), response);
        synchronized (lock) {
            taskID = TASK_SEND;
            params.clear();
            params.add(baseMessage);
            lock.notifyAll();
        }

    }

    public final void readInputFromConnection(byte[] input, TCPConnection conn) {
        synchronized (lock) {
            taskID = TASK_RECEIVE;
            params.clear();
            params.add(input);
            params.add(conn);
            lock.notifyAll();
        }
    }


    public final void makeConnection(String uniqueID, String host, int port) {
        synchronized (lock) {
            taskID = TASK_CONNECT;
            params.clear();
            params.add(uniqueID);
            params.add(host);
            params.add(port);
            lock.notifyAll();
        }
    }

    @Override
    public final void run() {

        while (true) {
            synchronized (lock) {
                if (taskID == Byte.MIN_VALUE) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Iterator<Object> in = params.iterator();
            switch (taskID) {
                case TASK_CONNECT: {
                    String uniqueID = (String) in.next();
                    String host = (String) in.next();
                    int port = (int) in.next();

                    if (!connectionPool.hasObject(uniqueID)) {
                        try {
                            Socket socket = new Socket(host, port);
                            TCPConnection connection = new TCPConnection(socket, factory);
                            connectionPool.insert(uniqueID, connection);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                }
                case TASK_RECEIVE: {
                    byte[] data = (byte[]) in.next();
                    TCPConnection conn = (TCPConnection) in.next();
                    onReceive(data, conn);
                    break;
                }
                case TASK_SEND:
                    sendMessage((BaseMessage) in.next());
                    break;
            }

            release();

        }

    }

    protected <T extends BaseMessage> void sendMessage(T outMessage) {
        byte[] out = serializer.serialize(outMessage);
        intToByteArray(outMessage.typeCode(), out, 2);
        Collection<TCPConnection> connections = connectionPool.fetch(outMessage.uniqueID());
        connections.forEach(conn -> {
            conn.send(out);
        });
    }

    protected <T extends BaseMessage> void sendMessage(T outMessage, TCPConnection connection) {
        byte[] out = serializer.serialize(outMessage);
        intToByteArray(outMessage.typeCode(), out, 2);
        connection.send(out);
    }

    protected abstract void onReceive(byte[] input, TCPConnection conn);

    public final void release() {
        synchronized (lock) {
            params.clear();
            taskID = Byte.MIN_VALUE;
        }
        factory.putBack(this);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZThread that = (ZThread) o;
        return hashCode == that.hashCode;
    }

    @Override
    public final int hashCode() {
        return hashCode;
    }
}
