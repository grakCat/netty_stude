package com.greak.netty_stude.pb;

import com.greak.netty_stude.proto.ProtobufMessage;

/**
 * Created on 2018/12/4.
 *
 * @author grayCat
 * @since 1.0
 */
@ProtobufMessage
public class ClusterMessage {

    public String sessionId;
    public PFMessage msg;


    public ClusterMessage(PFMessage msg) {
        this.msg = msg;
    }

    public ClusterMessage(String sessionId, PFMessage msg) {
        this.sessionId = sessionId;
        this.msg = msg;
    }
}
