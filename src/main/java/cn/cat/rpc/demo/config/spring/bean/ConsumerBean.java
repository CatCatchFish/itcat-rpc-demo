package cn.cat.rpc.demo.config.spring.bean;

import cn.cat.rpc.demo.config.ConsumerConfig;
import cn.cat.rpc.demo.domain.RpcProviderConfig;
import cn.cat.rpc.demo.network.msg.Request;
import cn.cat.rpc.demo.network.util.ConnectUtil;
import cn.cat.rpc.demo.reflect.JDKProxy;
import cn.cat.rpc.demo.reflect.util.ClassLoaderUtil;
import cn.cat.rpc.demo.registry.RedisRegistryCenter;
import cn.cat.rpc.demo.router.RoundRobinLoadBalancer;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

import java.util.List;

public class ConsumerBean extends ConsumerConfig implements FactoryBean {
    private RpcProviderConfig rpcProviderConfig;

    @Override
    public Object getObject() throws Exception {
        // 获取redis连接
        String providerInfo = null;
        if (null == rpcProviderConfig) {
            List<String> providerList = RedisRegistryCenter.obtainProvider(nozzle, alias);
            // 负载均衡
            final RoundRobinLoadBalancer loadBalancer = new RoundRobinLoadBalancer();
            providerInfo = loadBalancer.select(providerList);
            rpcProviderConfig = JSON.parseObject(providerInfo, RpcProviderConfig.class);
        }
        Assert.notNull(rpcProviderConfig, "rpcProviderConfig is null");

        // 获取通信管道
        ChannelFuture channelFuture = ConnectUtil.connect(providerInfo, rpcProviderConfig);
        Assert.notNull(channelFuture, "channelFuture is null");
        Request request = new Request();
        request.setChannel(channelFuture.channel());
        request.setNozzle(nozzle);
        request.setRef(rpcProviderConfig.getRef());
        request.setAlias(alias);
        // 将请求封装成JDK代理
        return JDKProxy.getProxy(ClassLoaderUtil.forName(nozzle), request, providerInfo);
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return ClassLoaderUtil.forName(nozzle);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
