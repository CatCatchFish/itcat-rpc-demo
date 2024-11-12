package cn.cat.rpc.demo.reflect;

import cn.cat.rpc.demo.network.future.impl.SyncWrite;
import cn.cat.rpc.demo.network.msg.Request;
import cn.cat.rpc.demo.network.msg.Response;
import cn.cat.rpc.demo.reflect.tolerant.FailoverFaultTolerantStrategy;
import cn.cat.rpc.demo.reflect.tolerant.FaultContext;
import cn.cat.rpc.demo.type.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JDKInvocationHandler implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(JDKInvocationHandler.class);
    private final Request request;
    private final String currentProvider;

    public JDKInvocationHandler(Request request, String currentProvider) {
        this.request = request;
        this.currentProvider = currentProvider;
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
        Response response = new SyncWrite().writeAndSync(request.getChannel(), request, 2000);

        if (null != response.getException()) {
            logger.error("invoke rpc provider method error:{}, try to handle exception", response.getException().getMessage());
            return handleException(request);
        }
        return response.getResult();
    }

    private Object handleException(Request request) {
        final FaultContext faultContext = new FaultContext();
        List<String> downTimeProviders = new ArrayList<>();
        downTimeProviders.add(currentProvider);
        faultContext.setDowntimeProviders(downTimeProviders);
        faultContext.setRequest(request);
        try {
            return FailoverFaultTolerantStrategy.handle(faultContext);
        } catch (Exception e) {
            logger.error("handle exception error:{}", e.getMessage());
            throw new RuntimeException("服务rpc调用失败，请检查服务是否正常");
        }
    }
}
