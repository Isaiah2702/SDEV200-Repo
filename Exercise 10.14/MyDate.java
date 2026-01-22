import java.util.GregorianCalendar;

public class MyDate {
    private int year;
    private int month; // 0-based: 0 = January
    private int day;

    /** No-arg constructor that creates a MyDate object for the current date. */
    public MyDate() {
        GregorianCalendar cal = new GregorianCalendar();
        this.year = cal.get(GregorianCalendar.YEAR);
        this.month = cal.get(GregorianCalendar.MONTH); // already 0-based
        this.day = cal.get(GregorianCalendar.DAY_OF_MONTH);
    }

    /** Constructs a MyDate object with a specified elapsed time since Jan 1, 1970 (ms). */
    public MyDate(long elapsedTime) {
        setDate(elapsedTime);
    }

    /** Constructs a MyDate object with the specified year, month, and day. */
    public MyDate(int year, int month, int day) {
        this.year = year;
        this.month = month; // assume caller uses 0-based month
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    /** Sets a new date for the object using the elapsed time (ms since Jan 1, 1970). */
    public void setDate(long elapsedTime) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(elapsedTime);

        this.year = cal.get(GregorianCalendar.YEAR);
        this.month = cal.get(GregorianCalendar.MONTH);
        this.day = cal.get(GregorianCalendar.DAY_OF_MONTH);
    }
}
