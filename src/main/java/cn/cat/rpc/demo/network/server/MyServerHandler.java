package cn.cat.rpc.demo.network.server;

import cn.cat.rpc.demo.network.codec.MsgHeader;
import cn.cat.rpc.demo.network.msg.Request;
import cn.cat.rpc.demo.network.msg.Response;
import cn.cat.rpc.demo.network.msg.RpcMsg;
import cn.cat.rpc.demo.network.util.MsgBuildUtil;
import cn.cat.rpc.demo.reflect.util.ClassLoaderUtil;
import cn.cat.rpc.demo.type.Constants;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    private final ApplicationContext applicationContext;

    MyServerHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) {
        try {
            RpcMsg<?> rpcMsg = (RpcMsg<?>) obj;
            MsgHeader header = rpcMsg.getHeader();
            Request msg = (Request) rpcMsg.getBody();
            //反馈
            Response request = new Response();
            request.setRequestId(msg.getRequestId());
            try {
                // 映射处理
                Class<?> classType = ClassLoaderUtil.forName(msg.getNozzle());
                Method method = classType.getMethod(msg.getMethodName(), msg.getParamTypes());
                Object objectBean = applicationContext.getBean(msg.getRef());
                Object result = method.invoke(objectBean, msg.getArgs());
                // 反馈结果
                request.setResult(result);
            } catch (Exception e) {
                // 反馈异常
                request.setException(e);
            }

            String serializationType = new String(header.getSerialization(), StandardCharsets.UTF_8);
            RpcMsg<Response> responseRpcMsg = MsgBuildUtil.buildResponseMsg(
                    Objects.requireNonNull(Constants.RpcSerializationType.get(serializationType)),
                    request
            );

            ctx.writeAndFlush(responseRpcMsg);
            //释放
            ReferenceCountUtil.release(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
