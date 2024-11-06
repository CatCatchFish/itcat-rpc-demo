package cn.cat.rpc.demo.network.serialization.factory;

import cn.cat.rpc.demo.network.serialization.JsonSerialization;
import cn.cat.rpc.demo.network.serialization.ProtobufSerialization;
import cn.cat.rpc.demo.network.serialization.RpcSerialization;
import cn.cat.rpc.demo.type.Constants;

import java.util.HashMap;
import java.util.Map;

public class SerializationFactory {
    private static final Map<Constants.RpcSerializationType, RpcSerialization> serializationMap =
            new HashMap<>();

    static {
        serializationMap.put(Constants.RpcSerializationType.JSON, new JsonSerialization());
        serializationMap.put(Constants.RpcSerializationType.PROTOBUF, new ProtobufSerialization());
    }

    public static RpcSerialization get(Constants.RpcSerializationType serializationType) {
        return serializationMap.get(serializationType);
    }
}
