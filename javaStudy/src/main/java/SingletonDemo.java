/**
 * 饿汉式单例模式
 */

public class SingletonDemo {
    //类初始化时，立即加载这个对象
    private static SingletonDemo s= new SingletonDemo();

    public SingletonDemo() {
    }

    public static SingletonDemo getInstance(){
        return s;
    }
}
