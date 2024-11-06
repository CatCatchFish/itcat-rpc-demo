package cn.cat.rpc.demo.network.util;

import cn.cat.rpc.demo.network.codec.MsgHeader;
import cn.cat.rpc.demo.network.msg.Request;
import cn.cat.rpc.demo.network.msg.Response;
import cn.cat.rpc.demo.network.msg.RpcMsg;
import cn.cat.rpc.demo.type.Constants;
import cn.cat.rpc.demo.type.ProtocolConstants;

public class MsgBuildUtil {
    public static RpcMsg<Request> buildRequestMsg(Constants.RpcSerializationType serializationType, Request request) {
        MsgHeader header = new MsgHeader();
        header.setMagic(ProtocolConstants.MAGIC);
        header.setMsgType((byte) ProtocolConstants.MessageType.REQUEST.ordinal());
        byte[] bytes = serializationType.name.getBytes();
        header.setSerialization(bytes);
        header.setSerializationLen(bytes.length);

        RpcMsg<Request> rpcMsg = new RpcMsg<>();
        rpcMsg.setHeader(header);
        rpcMsg.setBody(request);
        return rpcMsg;
    }

    public static RpcMsg<Response> buildResponseMsg(Constants.RpcSerializationType serializationType, Response response) {
        MsgHeader header = new MsgHeader();
        header.setMagic(ProtocolConstants.MAGIC);
        header.setMsgType((byte) ProtocolConstants.MessageType.RESPONSE.ordinal());
        byte[] bytes = serializationType.name.getBytes();
        header.setSerialization(bytes);
        header.setSerializationLen(bytes.length);

        RpcMsg<Response> rpcMsg = new RpcMsg<>();
        rpcMsg.setHeader(header);
        rpcMsg.setBody(response);
        return rpcMsg;
    }
}
