package cn.cat.rpc.demo.test.service.impl;

import cn.cat.rpc.demo.test.service.HelloService;
import org.springframework.stereotype.Service;

@Service("helloService")
public class HelloServiceImpl implements HelloService {
    @Override
    public void echo() {
        System.out.println("hi itstack demo rpc");
    }
}
