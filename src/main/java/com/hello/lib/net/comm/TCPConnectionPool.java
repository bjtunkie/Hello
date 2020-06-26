package com.hello.lib.net.comm;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class TCPConnectionPool implements ObjectPool<String, TCPConnection> {

    public static final String DEFAULT_KEY = "";
    private final Predicate<TCPConnection> removeIf;
    private final Map<String, Set<TCPConnection>> connections;

    public TCPConnectionPool() {
        this(c -> !c.isOpen());
    }
    public TCPConnectionPool(Predicate<TCPConnection> removeIf) {
        this.removeIf = removeIf;
        this.connections = new HashMap<>();
    }


    final Function<? super String, ? extends Set<TCPConnection>> creator = k -> new HashSet<>();

    @Override
    public void insert(String key, TCPConnection... value) {
        synchronized (connections) {
            Set<TCPConnection> set = connections.computeIfAbsent(key, creator);
            if (value != null) Collections.addAll(set, value);
        }
    }

    @Override
    public void remove() {
        connections
                .values()
                .parallelStream()
                .forEach(set -> {
                    set.removeIf(removeIf);
                });
    }

    @Override
    public void removeAll() {
        connections
                .values()
                .parallelStream()
                .forEach(Set::clear);
    }


    @Override
    public Collection<TCPConnection> remove(String key) {
        Collection<TCPConnection> collection = fetch(key);
        Collection<TCPConnection> copy = new LinkedList<>();
        copy.addAll(collection);
        collection.clear();
        return copy;
    }

    @Override
    public Collection<TCPConnection> fetch(String key) {
        return connections.computeIfAbsent(key, creator);
    }

    @Override
    public boolean hasObject(String key) {
        return !fetch(key).isEmpty();
    }
}
