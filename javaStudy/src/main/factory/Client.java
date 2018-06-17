public class Client {
    public static void main(String[] args) {
        Car car1 = CarFactory.createCar("奥迪");
        Car car2 = CarFactory.createCar("比亚迪");
        car1.run();
        car2.run();
    }
}
