package cn.cat.rpc.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("rpc.server")
public class ServerProperties {
    private String host;
    private int port;

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
