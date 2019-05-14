package com.greak.netty_stude.proto;

/**
 * Created on 2018/12/6.
 *
 * @author grayCat
 * @since 1.0
 */
public class UseMM {

    /**
     * 吃牌数据接受
     */
    @ProtobufMessage(messageType = 4300, cmd = 12118)
    public static class ReqChiGame {
        public String userChose;
    }

    /**
     * 碰数据接受
     */
    @ProtobufMessage(messageType = 4300, cmd = 12119)
    public static class ReqPengGame {
        public boolean isFei;//飞，false代表不飞
        public int coreNumber;
    }

    /**
     * 换三张返回
     */
    @ProtobufMessage(resp = true, messageType = 4300, cmd = 12120)
    public static class RespSanGame {
        public String returnStatu;//游戏返回状态
        public long playerId;
    }
}
