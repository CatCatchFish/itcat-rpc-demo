package cn.cat.rpc.demo.reflect.tolerant;

import cn.cat.rpc.demo.network.msg.Request;

import java.util.List;

public class FaultContext {
    private List<String> downtimeProviders;
    private Request request;

    public FaultContext() {
    }

    public List<String> getDowntimeProviders() {
        return downtimeProviders;
    }

    public void setDowntimeProviders(List<String> downtimeProviders) {
        this.downtimeProviders = downtimeProviders;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
