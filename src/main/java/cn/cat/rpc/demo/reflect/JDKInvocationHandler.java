package cn.cat.rpc.demo.reflect;

import cn.cat.rpc.demo.network.future.impl.SyncWrite;
import cn.cat.rpc.demo.network.msg.Request;
import cn.cat.rpc.demo.network.msg.Response;
import cn.cat.rpc.demo.type.Constants;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JDKInvocationHandler implements InvocationHandler {
    private final Request request;

    public JDKInvocationHandler(Request request) {
        this.request = request;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取代理的方法名
        String methodName = method.getName();
        Class<?>[] paramTypes = method.getParameterTypes();

        // 处理Object的方法
        if (Constants.ObjectMethod.TO_STRING.getName().equals(methodName)
                && paramTypes.length == 0) {
            return request.toString();
        } else if (Constants.ObjectMethod.HASH_CODE.getName().equals(methodName)
                && paramTypes.length == 0) {
            return request.hashCode();
        } else if (Constants.ObjectMethod.EQUALS.getName().equals(methodName)
                && paramTypes.length == 1) {
            return request.equals(args[0]);
        }

        // 封装本地方法的调用请求
        request.setMethodName(methodName);
        request.setParamTypes(paramTypes);
        request.setArgs(args);
        request.setRef(request.getRef());
        // 异步调用远程服务方提供的服务
        Response response = new SyncWrite().writeAndSync(request.getChannel(), request, 5000);
        return response.getResult();
    }
}
