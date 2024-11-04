package cn.cat.rpc.demo.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApiTest {
    public static void main(String[] args) {
        String[] configs = {"itstack-rpc-center.xml", "itstack-rpc-provider.xml", "itstack-rpc-consumer.xml"};
        new ClassPathXmlApplicationContext(configs);
    }
}
