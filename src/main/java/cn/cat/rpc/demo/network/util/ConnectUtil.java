package cn.cat.rpc.demo.network.util;

import cn.cat.rpc.demo.domain.RpcProviderConfig;
import cn.cat.rpc.demo.network.client.ClientSocket;
import cn.cat.rpc.demo.type.Constants;
import io.netty.channel.ChannelFuture;

public class ConnectUtil {
    public static ChannelFuture connect(String providerInfo, RpcProviderConfig rpcProviderConfig) throws Exception {
        ChannelFuture channelFuture = null;
        ClientSocket clientSocket = new ClientSocket(rpcProviderConfig.getHost(),
                rpcProviderConfig.getPort());

        new Thread(clientSocket).start();
        // 等待连接成功
        for (int i = 0; i < 100; i++) {
            if (null != channelFuture) break;
            Thread.sleep(500);
            channelFuture = clientSocket.getFuture();
            // 将通信管道存入map
            Constants.PROVIDER_CHANNEL_FUTURE_MAP.put(providerInfo, channelFuture);
        }
        return channelFuture;
    }
}
