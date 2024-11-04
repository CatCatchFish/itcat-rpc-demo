package cn.cat.rpc.demo.config.spring.bean;

import cn.cat.rpc.demo.config.ServerConfig;
import cn.cat.rpc.demo.domain.LocalServerInfo;
import cn.cat.rpc.demo.network.server.ServerSocket;
import cn.cat.rpc.demo.registry.RedisRegistryCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServerBean extends ServerConfig implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(ServerBean.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("启动注册中心......");
        RedisRegistryCenter.init(host, port);
        logger.info("注册中心启动成功！");

        logger.info("初始化生产端服务......");
        ServerSocket serverSocket = new ServerSocket(applicationContext);
        Thread thread = new Thread(serverSocket);
        thread.start();
        while (!serverSocket.isActiveSocketServer()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
        logger.info("初始化生产端服务完成 {} {}", LocalServerInfo.LOCAL_HOST, LocalServerInfo.LOCAL_PORT);
    }
}
