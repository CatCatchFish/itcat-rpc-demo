package cn.cat.rpc.demo.config.spring.bean;

import cn.cat.rpc.demo.config.ConsumerConfig;
import cn.cat.rpc.demo.domain.RpcProviderConfig;
import cn.cat.rpc.demo.network.client.ClientSocket;
import cn.cat.rpc.demo.network.msg.Request;
import cn.cat.rpc.demo.reflect.JDKProxy;
import cn.cat.rpc.demo.reflect.util.ClassLoaderUtil;
import cn.cat.rpc.demo.registry.RedisRegistryCenter;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

public class ConsumerBean extends ConsumerConfig implements FactoryBean {
    private ChannelFuture channelFuture;
    private RpcProviderConfig rpcProviderConfig;

    @Override
    public Object getObject() throws Exception {
        // 获取redis连接
        if (null == rpcProviderConfig) {
            String infoStr = RedisRegistryCenter.obtainProvider(nozzle, alias);
            rpcProviderConfig = JSON.parseObject(infoStr, RpcProviderConfig.class);
        }
        Assert.notNull(rpcProviderConfig, "rpcProviderConfig is null");

        // 获取通信管道
        if (null == channelFuture) {
            ClientSocket clientSocket = new ClientSocket(rpcProviderConfig.getHost(),
                    rpcProviderConfig.getPort());
            new Thread(clientSocket).start();
            // 等待连接成功
            for (int i = 0; i < 100; i++) {
                if (null != channelFuture) break;
                Thread.sleep(500);
                channelFuture = clientSocket.getFuture();
            }
        }
        Assert.notNull(channelFuture, "channelFuture is null");
        Request request = new Request();
        request.setChannel(channelFuture.channel());
        request.setNozzle(nozzle);
        request.setRef(rpcProviderConfig.getRef());
        request.setAlias(alias);
        // 将请求封装成JDK代理
        return JDKProxy.getProxy(ClassLoaderUtil.forName(nozzle), request);
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
