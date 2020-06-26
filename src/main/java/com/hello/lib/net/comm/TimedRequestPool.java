package com.hello.lib.net.comm;

import com.hello.lib.net.proto.MessageResponse;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TimedRequestPool implements ObjectPool<String, MessageResponse> {


    private final Map<String, Object[]> connections;

    public TimedRequestPool() {
        this.connections = new HashMap<>();
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(TIME_TO_LIVE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            remove();
        });
        thread.start();
    }

    @Override
    public void insert(String key, MessageResponse... value) {
        connections.put(key, new Object[]{System.currentTimeMillis(), Arrays.asList(value)});
    }

    @Override
    public void remove() {

        final long now = System.currentTimeMillis();
        connections.entrySet().removeIf(entry -> {
            long duration = now - (long) entry.getValue()[0];
            return duration > TIME_TO_LIVE;
        });

    }

    @Override
    public void removeAll() {
        connections.clear();
    }

    @Override
    public Collection<MessageResponse> remove(String key) {
        Object[] x = connections.remove(key);
        return x == null ? null : (Collection<MessageResponse>) x[1];
    }

    @Override
    public Collection<MessageResponse> fetch(String key) {
        Object[] x = connections.get(key);
        return x == null ? null : (Collection<MessageResponse>) x[1];
    }

    @Override
    public boolean hasObject(String key) {
        return connections.containsKey(key);
    }
}
