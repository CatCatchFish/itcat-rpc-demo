package cn.cat.rpc.demo.config;

public class ServerConfig {
    // 注册中心地址
    protected String host;
    // 注册中心端口
    protected int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
