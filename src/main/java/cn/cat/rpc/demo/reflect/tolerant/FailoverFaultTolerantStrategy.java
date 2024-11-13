package cn.cat.rpc.demo.reflect.tolerant;

import cn.cat.rpc.demo.domain.RpcProviderConfig;
import cn.cat.rpc.demo.network.future.impl.SyncWrite;
import cn.cat.rpc.demo.network.msg.Request;
import cn.cat.rpc.demo.network.msg.Response;
import cn.cat.rpc.demo.network.util.ConnectUtil;
import cn.cat.rpc.demo.registry.RedisRegistryCenter;
import cn.cat.rpc.demo.type.Constants;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFuture;

import java.util.List;

public class FailoverFaultTolerantStrategy {
    public static Object handle(FaultContext faultContext) throws Exception {
        final List<String> downtimeProviders = faultContext.getDowntimeProviders();
        final Request request = faultContext.getRequest();
        // 从Redis中获取服务提供者列表
        List<String> providers = RedisRegistryCenter.obtainProvider(request.getNozzle(), request.getAlias())
                .stream()
                .filter(provider -> !downtimeProviders.contains(provider))
                .toList();
        if (providers.isEmpty()) {
            throw new NullPointerException("No available provider found.");
        }
        // 另选一个服务提供者
        String attemptProvider = providers.get(0);

        ChannelFuture channelFuture = Constants.PROVIDER_CHANNEL_FUTURE_MAP.get(attemptProvider);
        if (null == channelFuture) {
            RpcProviderConfig providerConfig = JSON.parseObject(attemptProvider, RpcProviderConfig.class);
            channelFuture = ConnectUtil.connect(attemptProvider, providerConfig);
        }
        // 另选服务提供者的通信管道
        Response response = new SyncWrite().writeAndSync(channelFuture.channel(), request, 3000);
        if (null != response.getException()) {
            // 若通信失败，则将故障提供者加入到不可用列表中
            downtimeProviders.add(attemptProvider);
            faultContext.setDowntimeProviders(downtimeProviders);
            handle(faultContext);
        }
        return response.getResult();
    }
}
