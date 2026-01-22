public class TestMyDate {
    public static void main(String[] args) {
        MyDate d1 = new MyDate();
        MyDate d2 = new MyDate(34355555133101L);

        System.out.println("MyDate() -> " +
                "year: " + d1.getYear() +
                ", month: " + d1.getMonth() +
                ", day: " + d1.getDay());

        System.out.println("MyDate(34355555133101L) -> " +
                "year: " + d2.getYear() +
                ", month: " + d2.getMonth() +
                ", day: " + d2.getDay());
    }
}
