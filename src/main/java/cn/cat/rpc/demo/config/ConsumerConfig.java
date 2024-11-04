package cn.cat.rpc.demo.config;

public class ConsumerConfig {
    // 接口
    protected String nozzle;
    // 别名
    protected String alias;

    public String getNozzle() {
        return nozzle;
    }

    public void setNozzle(String nozzle) {
        this.nozzle = nozzle;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
