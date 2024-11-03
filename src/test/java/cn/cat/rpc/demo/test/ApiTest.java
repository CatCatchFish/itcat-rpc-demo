package cn.cat.rpc.demo.test;

import cn.cat.rpc.demo.test.domain.User;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ApiTest {
    public static void main(String[] args) {
        String[] configs = {"itstack-rpc-consumer.xml", "itstack-rpc-provider.xml"};
        new ClassPathXmlApplicationContext(configs);
    }

    @Test
    public void isPropertyTest() {
        Class<?> beanClass = User.class;
        Method[] methods = beanClass.getMethods();
        for (Method method : methods) {
            if (!isProperty(method, beanClass)) {
                String name = method.getName();
                String methodName = name.substring(3, 4).toLowerCase() + name.substring(4);
                System.out.println(methodName);
            }
        }
    }

    private boolean isProperty(Method method, Class<?> beanClass) {
        String methodName = method.getName();
        boolean flag = methodName.length() > 3 && methodName.startsWith("set") && Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 1;
        Method getter = null;
        if (!flag) return false;

        Class<?> type = method.getParameterTypes()[0];
        // 尝试获取对应的 getter 方法
        try {
            getter = beanClass.getMethod("get" + methodName.substring(3));
        } catch (NoSuchMethodException ignore) {

        }

        // 尝试获取对应的 is 方法
        if (null == getter) {
            try {
                getter = beanClass.getMethod("is" + methodName.substring(3));
            } catch (NoSuchMethodException ignore) {

            }
        }
        flag = getter != null && Modifier.isPublic(getter.getModifiers()) && type.equals(getter.getReturnType());
        return flag;
    }
}
