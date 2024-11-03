package cn.cat.rpc.demo.spring.bean;

import cn.cat.rpc.demo.ConsumerConfig;
import org.springframework.beans.factory.FactoryBean;

public class ConsumerBean<T> extends ConsumerConfig<T> implements FactoryBean {
    @Override
    public Object getObject() throws Exception {
        return refer();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
