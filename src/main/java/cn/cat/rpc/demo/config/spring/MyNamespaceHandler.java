package cn.cat.rpc.demo.config.spring;

import cn.cat.rpc.demo.config.spring.bean.ConsumerBean;
import cn.cat.rpc.demo.config.spring.bean.ProviderBean;
import cn.cat.rpc.demo.config.spring.bean.ServerBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("server", new MyBeanDefinitionParser(ServerBean.class));
        registerBeanDefinitionParser("provider", new MyBeanDefinitionParser(ProviderBean.class));
        registerBeanDefinitionParser("consumer", new MyBeanDefinitionParser(ConsumerBean.class));
    }
}
