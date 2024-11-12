package cn.cat.rpc.demo.type;

import io.netty.channel.ChannelFuture;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static Map<String, ChannelFuture> PROVIDER_CHANNEL_FUTURE_MAP = new HashMap<>();

    public enum ObjectMethod {
        TO_STRING("toString"),
        HASH_CODE("hashCode"),
        EQUALS("equals");

        ObjectMethod(String name) {
            this.name = name;
        }

        private final String name;

        public String getName() {
            return name;
        }
    }

    public enum RpcSerializationType {
        JSON("json"),
        PROTOBUF("protobuf");
        public final String name;

        RpcSerializationType(String type) {
            this.name = type;
        }

        public static RpcSerializationType get(String type) {
            for (RpcSerializationType value : values()) {
                if (value.name.equals(type)) {
                    return value;
                }
            }
            return null;
        }

    }
}
