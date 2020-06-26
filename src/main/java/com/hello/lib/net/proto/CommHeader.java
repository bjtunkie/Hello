package com.hello.lib.net.proto;

public interface CommHeader {

    byte zero = 0;
    /**
     * message only for the server, these messages will me timestamp and other details.
     * 1: can be used to update the IP address of an unique ID
     * 2. Update the IP address of a session
     */
    byte server_inbound = 1;

    /**
     * the server is only there to transfer or forward the message to the destination
     * 1: Video calls
     */
    byte gateway_transfer = 2;

    /**
     * The message is for server as well as for the destination too.
     */
    byte server_involved = 3;

    /**
     * response from server to the caller
     */
    byte server_response = 4;


}
