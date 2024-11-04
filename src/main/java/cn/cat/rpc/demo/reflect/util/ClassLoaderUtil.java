package cn.cat.rpc.demo.reflect.util;

public class ClassLoaderUtil {
    /**
     * 获取当前线程的ClassLoader
     *
     * @return ClassLoader
     */
    public static ClassLoader getCurrentClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ClassLoaderUtil.class.getClassLoader();
        }
        return cl == null ? ClassLoader.getSystemClassLoader() : cl;
    }

    /**
     * 根据类名加载Class
     *
     * @param className 类名
     * @return Class
     * @throws ClassNotFoundException 找不到类
     */
    public static Class<?> forName(String className)
            throws ClassNotFoundException {
        return forName(className, true);
    }

    /**
     * 根据类名加载Class
     *
     * @param className  类名
     * @param initialize 是否初始化
     * @return Class
     * @throws ClassNotFoundException 找不到类
     */
    public static Class<?> forName(String className, boolean initialize)
            throws ClassNotFoundException {
        return Class.forName(className, initialize, getCurrentClassLoader());
    }
}
