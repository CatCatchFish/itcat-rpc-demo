package cn.cat.rpc.demo.reflect;

import cn.cat.rpc.demo.network.msg.Request;
import cn.cat.rpc.demo.reflect.util.ClassLoaderUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class JDKProxy {
    public static <T> T getProxy(Class<T> interfaceClass, Request request) throws Exception {
        InvocationHandler handler = new JDKInvocationHandler(request);
        ClassLoader loader = ClassLoaderUtil.getCurrentClassLoader();
        T result = (T) Proxy.newProxyInstance(loader, new Class[]{interfaceClass}, handler);
        return result;
    }
}
