package com.hello.lib.message;

import com.hello.lib.message.chat.SimpleMessage;
import com.hello.lib.net.proto.BaseMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Messages {

    public static Map<Integer, Class<? extends BaseMessage>> getClasses() {
        Map<Integer, Class<? extends BaseMessage>> MAP = new HashMap<>();
        MAP.put(SimpleMessage.CODE, SimpleMessage.class);
        return Collections.unmodifiableMap(MAP);
    }
}
