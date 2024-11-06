package cn.cat.rpc.demo.network.codec;

import cn.cat.rpc.demo.network.msg.RpcMsg;
import cn.cat.rpc.demo.network.serialization.ProtobufSerialization;
import cn.cat.rpc.demo.network.serialization.RpcSerialization;
import cn.cat.rpc.demo.network.serialization.factory.SerializationFactory;
import cn.cat.rpc.demo.type.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder<RpcMsg<Object>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMsg<Object> msg, ByteBuf byteBuf) throws Exception {
        MsgHeader header = msg.getHeader();
        // 1、写入魔数
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getMsgType());
        // 2、写入序列化算法类型
        byteBuf.writeInt(header.getSerializationLen());
        final byte[] ser = header.getSerialization();
        final String serialization = new String(ser);
        byteBuf.writeBytes(ser);
        RpcSerialization rpcSerialization = SerializationFactory.get(Constants.RpcSerializationType.get(serialization));
        byte[] data = rpcSerialization.serialize(msg.getBody());
        // 写入数据长度(接收方根据数据长度读取数据内容)
        byteBuf.writeInt(data.length);
        // 写入数据
        byteBuf.writeBytes(data);
    }
}
