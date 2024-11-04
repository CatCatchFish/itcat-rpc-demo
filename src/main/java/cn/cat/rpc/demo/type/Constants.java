package cn.cat.rpc.demo.type;

public class Constants {
    public enum ObjectMethod {
        TO_STRING("toString"),
        HASH_CODE("hashCode"),
        EQUALS("equals");

        ObjectMethod(String name) {
            this.name = name;
        }

        private final String name;

        public String getName() {
            return name;
        }
    }
}
