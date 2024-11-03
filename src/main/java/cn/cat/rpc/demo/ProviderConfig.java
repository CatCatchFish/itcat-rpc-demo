package cn.cat.rpc.demo;

public class ProviderConfig {
    // 接口
    private String nozzle;
    // 映射
    private String ref;
    // 别名
    private String alias;

    // 发布
    protected void doExport() {
        System.out.format("生产者信息 => 【接口：%s】 【映射：%s】 【别名：%s】\r\n", nozzle, ref, alias);
    }

    public String getNozzle() {
        return nozzle;
    }

    public void setNozzle(String nozzle) {
        this.nozzle = nozzle;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
