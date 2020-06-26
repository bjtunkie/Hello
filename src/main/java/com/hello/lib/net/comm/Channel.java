package com.hello.lib.net.comm;

import com.hello.lib.net.proto.*;

public final class Channel {
    private static StagingArea area;

    private Channel() {
    }

    public static <T extends ZThread> void instantiate(boolean startServer, Class<T> threadClass, Object... params) {
        instantiate(startServer, threadClass, null, params);
    }

    public static <T extends ZThread> void instantiate(boolean startServer, Class<T> threadClass, ObjectPool<String, MessageResponse> requestPool, Object... params) {
        synchronized (Channel.class) {
            if (area == null) {
                area = new StagingArea(startServer, threadClass, requestPool, params);
            }
        }
    }


    public static <M extends BaseMessage> void send(M message, MessageResponse response) {
        area.send(message, response);
    }


    public static void makeConnection(String uniqueID, String host, int port) {
        area.getThreadFactory()
                .getThread()
                .makeConnection(uniqueID, host, port);
    }
}

