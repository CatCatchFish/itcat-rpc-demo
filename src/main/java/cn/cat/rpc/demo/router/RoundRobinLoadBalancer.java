package cn.cat.rpc.demo.router;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalancer implements LoadBalancer {
    private static final AtomicInteger roundRobinId = new AtomicInteger(0);

    @Override
    public String select(List<String> serviceInstance) {
        int index = roundRobinId.getAndIncrement();
        // 防止溢出
        if (index == Integer.MAX_VALUE) {
            roundRobinId.set(0);
        }
        return serviceInstance.get(index % serviceInstance.size());
    }
}
