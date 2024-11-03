package cn.cat.rpc.demo.spring;

import cn.cat.rpc.demo.spring.bean.ConsumerBean;
import cn.cat.rpc.demo.spring.bean.ProviderBean;
import cn.cat.rpc.demo.spring.bean.ServerBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("consumer", new MyBeanDefinitionParser(ConsumerBean.class));
        registerBeanDefinitionParser("provider", new MyBeanDefinitionParser(ProviderBean.class));
        registerBeanDefinitionParser("server", new MyBeanDefinitionParser(ServerBean.class));
    }
}
