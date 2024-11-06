package cn.cat.rpc.demo.network.future.impl;

import cn.cat.rpc.demo.network.future.SyncWriteFuture;
import cn.cat.rpc.demo.network.future.SyncWriteMap;
import cn.cat.rpc.demo.network.future.WriteFuture;
import cn.cat.rpc.demo.network.msg.Request;
import cn.cat.rpc.demo.network.msg.Response;
import cn.cat.rpc.demo.network.msg.RpcMsg;
import cn.cat.rpc.demo.network.util.MsgBuildUtil;
import cn.cat.rpc.demo.type.Constants;
import io.netty.channel.Channel;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SyncWrite {
    public Response writeAndSync(final Channel channel, final Request request, final long timeout) throws Exception {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        if (request == null) {
            throw new NullPointerException("request");
        }
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout <= 0");
        }

        String requestId = UUID.randomUUID().toString();
        request.setRequestId(requestId);

        WriteFuture<Response> future = new SyncWriteFuture(request.getRequestId());
        SyncWriteMap.syncKey.put(requestId, future);


        RpcMsg<Request> rpcMsg = MsgBuildUtil.buildRequestMsg(
                Constants.RpcSerializationType.PROTOBUF,
                request
        );

        Response response = doWriteAndSync(channel, rpcMsg, timeout, future);
        SyncWriteMap.syncKey.remove(requestId);
        return response;
    }

    private Response doWriteAndSync(final Channel channel, final RpcMsg<Request> request, final long timeout, final WriteFuture<Response> writeFuture) throws Exception {
        channel.writeAndFlush(request).addListener(channelFuture -> {
            writeFuture.setWriteResult(channelFuture.isSuccess());
            writeFuture.setCause(channelFuture.cause());
            if (!writeFuture.isWriteSuccess()) {
                SyncWriteMap.syncKey.remove(writeFuture.requestId());
            }
        });

        Response response = writeFuture.get(timeout, TimeUnit.MILLISECONDS);
        if (response == null) {
            if (writeFuture.isTimeout()) {
                throw new TimeoutException();
            } else {
                // write exception
                throw new Exception(writeFuture.cause());
            }
        }
        return response;
    }
}
