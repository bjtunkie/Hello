package com.hello.lib.net.proto;

public abstract class BaseMessage implements CommHeader {
    
    private long timestamp;

    private final int typeCode;
    private final byte b1, b2;
    private final String dstUniqueID;
    private final String srcUniqueID;
    private final String requestID;
    private final String sessionID;
    private final String responseID; // basically requestID if you are responding to any

    public BaseMessage(Config config) {
        this(config.b1, zero, config.code, config.dstUniqueID, config.srcUniqueID, config.requestID, config.sessionID, config.responseID);
    }

    public BaseMessage(byte h1, byte h2, int classCode, String dstUniqueID, String srcUniqueID, String requestID, String sessionID, String responseID) {
        this.b1 = h1;
        this.b2 = h2;
        this.typeCode = classCode;
        this.dstUniqueID = dstUniqueID;
        this.srcUniqueID = srcUniqueID;
        this.requestID = requestID;
        this.sessionID = sessionID;
        this.responseID = responseID;

    }

    public String getSessionID() {
        return sessionID;
    }

    public String getResponseID() {
        return responseID;
    }

    public String getSrcUniqueID() {
        return srcUniqueID;
    }

    public String getDstUniqueID() {
        return dstUniqueID;
    }

    public final long resetTimestamp() {
        return timestamp = System.currentTimeMillis();
    }

    public final String uniqueID() {
        return dstUniqueID;
    }

    public final String requestID() {
        return requestID;
    }

    public int typeCode() {
        return typeCode;
    }

    public final long timestamp() {
        return timestamp;
    }

    public final Config getConfigForResponse() {
        Config config = new Config()
                .setResponseID(this.requestID)
                .setSessionID(this.sessionID)
                .setSrcUniqueID(this.dstUniqueID)
                .setDstUniqueID(this.srcUniqueID);

        return config;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseMessage that = (BaseMessage) o;
        return requestID.equals(that.requestID);
    }

    @Override
    public final int hashCode() {
        return requestID.hashCode();
    }

    public static final class Config {
        private byte b1, b2;
        private int code;
        private String dstUniqueID;
        private String srcUniqueID;
        private String requestID;
        private String sessionID;
        private String responseID;

        public Config() {
            dstUniqueID = srcUniqueID = requestID = sessionID = responseID = "";
        }

        public Config setHeader(byte a) {
            b1 = a;
            b2 = a;
            return this;
        }

        public Config setJCode(short code) {
            this.code = code;
            return this;
        }

        public Config setDstUniqueID(String dstUniqueID) {
            this.dstUniqueID = dstUniqueID;
            return this;
        }

        public Config setSrcUniqueID(String srcUniqueID) {
            this.srcUniqueID = srcUniqueID;
            return this;
        }

        public Config setRequestID(String requestID) {
            this.requestID = requestID;
            return this;
        }

        public Config setSessionID(String sessionID) {
            this.sessionID = sessionID;
            return this;
        }

        public Config setResponseID(String responseID) {
            this.responseID = responseID;
            return this;
        }
    }


}
