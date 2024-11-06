package cn.cat.rpc.demo.network.codec;

import java.io.Serializable;

public class MsgHeader implements Serializable {
    private short magic;
    private int serializationLen;
    private byte[] serialization;
    private byte msgType;

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public int getSerializationLen() {
        return serializationLen;
    }

    public void setSerializationLen(int serializationLen) {
        this.serializationLen = serializationLen;
    }

    public byte[] getSerialization() {
        return serialization;
    }

    public void setSerialization(byte[] serialization) {
        this.serialization = serialization;
    }
}
