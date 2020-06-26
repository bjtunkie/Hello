package com.hello.lib.net.comm;

import java.util.Collection;

public interface ObjectPool<K, V> {
    int TIME_TO_LIVE = 5000;
    String DEFAULT_KEY = "";


    public void insert(K key, V... value);

    public void remove();

    public void removeAll();

    public Collection<V> remove(K key);

    public Collection<V> fetch(K key);

    boolean hasObject(K key);

}
