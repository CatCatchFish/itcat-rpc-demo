package cn.cat.rpc.demo.network.client;

import cn.cat.rpc.demo.network.codec.RpcDecoder;
import cn.cat.rpc.demo.network.codec.RpcEncoder;
import cn.cat.rpc.demo.network.msg.Request;
import cn.cat.rpc.demo.network.msg.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientSocket implements Runnable {

    private ChannelFuture future;

    private final String inetHost;
    private final int inetPort;

    public ClientSocket(String inetHost, int inetPort) {
        this.inetHost = inetHost;
        this.inetPort = inetPort;
    }

    @Override
    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.AUTO_READ, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new RpcDecoder(),
                            new RpcEncoder(),
                            new MyClientHandler());
                }
            });
            ChannelFuture f = b.connect(inetHost, inetPort).sync();
            this.future = f;
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void setFuture(ChannelFuture future) {
        this.future = future;
    }

    public ChannelFuture getFuture() {
        return this.future;
    }
}