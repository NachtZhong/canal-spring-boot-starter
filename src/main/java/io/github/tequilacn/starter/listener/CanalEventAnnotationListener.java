package io.github.tequilacn.starter.listener;

/**
 * @author Nacht
 * Created on 2021/4/12
 */
public class CanalEventAnnotationListener {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        System.out.println(Integer.TYPE == Integer.class);
        System.out.println(Integer.TYPE);
        System.out.println(Integer.class);
        System.out.println(CanalEventAnnotationListener.class.getClassLoader());;
    }
}
