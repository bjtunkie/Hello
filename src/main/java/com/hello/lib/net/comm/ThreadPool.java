package com.hello.lib.net.comm;

import com.hello.lib.net.proto.MessageResponse;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Deque;
import java.util.LinkedList;

/***
 * It's a pool
 */
public class ThreadPool<T extends ZThread> {


    private final Deque<T> stack = new LinkedList<>();
    private final Class<T> threadClass;

    private final ObjectPool<String, MessageResponse> requestPool;
    private final ObjectPool<String, TCPConnection> connectionPool;
    private final Constructor<T> construct;
    private final Object[] params;

    ThreadPool(Class<T> t, ObjectPool<String, TCPConnection> c, ObjectPool<String, MessageResponse> r, Object... p) {
        threadClass = t;
        connectionPool = c;
        requestPool = r;
        Constructor<T> cr = null;

        if (t.getDeclaredConstructors().length > 1) {
            throw new RuntimeException("Custom thread should have only one constructor...");
        } else {
            cr = (Constructor<T>) threadClass.getDeclaredConstructors()[0];
            cr.setAccessible(true);
        }
        construct = cr;
        params = p;
    }

    T createThread() {

        try {
            synchronized (this) {
                T thread = construct.newInstance(this, connectionPool, requestPool, params);
                thread.start();
                return thread;
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final synchronized T getThread() {
        if (stack.isEmpty()) {
            T t;
            while ((t = createThread()) != null) {
                return t;
            }
        } else {
            return stack.pop();

        }

        return null;
    }

    public final void putBack(final ZThread thread) {
        synchronized (stack) {
            stack.push((T) thread);
        }
    }
}
