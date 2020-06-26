package com.hello.lib.message.login;


import com.hello.lib.net.proto.BaseMessage;

public class SignInRequest extends BaseMessage {
    public static final short CODE = 94;
    private String userID, password;

    public SignInRequest(String userID, String password, Config config) {
        super(config.setJCode(CODE));
        this.userID = userID;
        this.password = password;
    }
}
