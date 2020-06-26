package com.hello.lib.net.proto;

@FunctionalInterface
public interface MessageResponse {

    void onResponse(Object response);
}
