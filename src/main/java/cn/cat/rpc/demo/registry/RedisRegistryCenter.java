package cn.cat.rpc.demo.registry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisRegistryCenter {
    private static Jedis jedis;

    // 初始化redis
    public static void init(String host, int port) {
        // 连接池配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);
        config.setTestOnBorrow(false);
        JedisPool jedisPool = new JedisPool(config, host, port);
        jedis = jedisPool.getResource();
    }

    /**
     * 注册生产者
     *
     * @param nozzle 接口
     * @param alias  别名
     * @param info   信息
     * @return 注册结果
     */
    public static Long registryProvider(String nozzle, String alias, String info) {
        // info 是一段json字符串
        return jedis.sadd(nozzle + "_" + alias, info);
    }

    /**
     * 获取生产者
     * 模拟权重，随机获取
     *
     * @param nozzle 接口名称
     */
    public static String obtainProvider(String nozzle, String alias) {
        return jedis.srandmember(nozzle + "_" + alias);
    }

    public static Jedis jedis() {
        return jedis;
    }
}
