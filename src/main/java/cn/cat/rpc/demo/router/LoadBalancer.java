package cn.cat.rpc.demo.router;

import java.util.List;

public interface LoadBalancer {
    String select(List<String> serviceInstance);
}
