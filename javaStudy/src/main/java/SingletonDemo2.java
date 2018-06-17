/**
 * 懒汉式单例模式
 */

public class SingletonDemo2 {

    private static SingletonDemo2 s;

    public SingletonDemo2() {

    }
    public static synchronized SingletonDemo2 getInstance() {
        if (s == null){
            s = new SingletonDemo2();
        }
        return s;
    }
}
