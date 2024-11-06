package cn.cat.rpc.demo.network.msg;

import cn.cat.rpc.demo.network.codec.MsgHeader;

public class RpcMsg<T> {
    private MsgHeader header;
    private T body;

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
