package cn.cat.rpc.demo.network.codec;

import cn.cat.rpc.demo.network.msg.Request;
import cn.cat.rpc.demo.network.msg.Response;
import cn.cat.rpc.demo.network.msg.RpcMsg;
import cn.cat.rpc.demo.network.serialization.RpcSerialization;
import cn.cat.rpc.demo.network.serialization.factory.SerializationFactory;
import cn.cat.rpc.demo.network.util.MsgBuildUtil;
import cn.cat.rpc.demo.type.Constants;
import cn.cat.rpc.demo.type.ProtocolConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.Objects;

public class RpcDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }

        // 读取魔数字段
        short magic = in.readShort();
        if (magic != ProtocolConstants.MAGIC) {
            throw new IllegalArgumentException("magic number is illegal, " + magic);
        }

        // 读取消息类型
        byte msgType = in.readByte();
        if (ProtocolConstants.MessageType.findByType(msgType) == null) {
            return;
        }

        // 读取序列化类型字段
        final int len = in.readInt();
        if (in.readableBytes() < len) {
            in.resetReaderIndex();
            return;
        }
        final byte[] serializationBytes = new byte[len];
        in.readBytes(serializationBytes);
        final String serialization = new String(serializationBytes);

        in.markReaderIndex();
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        RpcSerialization rpcSerialization = SerializationFactory.get(Constants.RpcSerializationType.get(serialization));

        // 反序列化Request对象
        switch (ProtocolConstants.MessageType.findByType(msgType)) {
            case REQUEST -> {
                RpcMsg<Request> requestRpcMsg = MsgBuildUtil.buildRequestMsg(
                        Objects.requireNonNull(Constants.RpcSerializationType.get(serialization)),
                        rpcSerialization.deserialize(data, Request.class)
                );
                out.add(requestRpcMsg);
            }
            case RESPONSE -> {
                out.add(rpcSerialization.deserialize(data, Response.class));
            }
            default -> {
                throw new IllegalArgumentException("msgType is illegal, " + msgType);
            }
        }
    }
}
