package cn.cat.rpc.demo.test.service.impl;

import cn.cat.rpc.demo.test.service.IHelloService;

public class HelloService implements IHelloService {
    @Override
    public void echo() {
        System.out.println("hi itstack demo rpc");
    }
}