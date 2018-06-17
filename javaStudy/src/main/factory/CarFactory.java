public  class CarFactory {
    public static Car createCar(String type){
        if (type.equals("奥迪")){
            return new Aodi();
        } else if (type.equals("比亚迪")){
            return new Byd();
        } else {
            return null;
        }
    }
}
