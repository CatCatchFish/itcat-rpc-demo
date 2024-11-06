package cn.cat.rpc.demo.type;

public class ProtocolConstants {
    public static final short MAGIC = 0x10;

    public enum MessageType {
        REQUEST,
        RESPONSE,
        HEARTBEAT;

        public static MessageType findByType(int type) {
            return MessageType.values()[type];
        }
    }
}
