import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class ClockPane extends Pane {
    private int hour;
    private int minute;
    private int second;

    public ClockPane() {
        setCurrentTime();
    }

    public ClockPane(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        paintClock();
    }

    public int getHour() { return hour; }
    public int getMinute() { return minute; }
    public int getSecond() { return second; }

    public void setHour(int hour) {
        this.hour = hour;
        paintClock();
    }

    public void setMinute(int minute) {
        this.minute = minute;
        paintClock();
    }

    public void setSecond(int second) {
        this.second = second;
        paintClock();
    }

    public void setCurrentTime() {
        Calendar calendar = new GregorianCalendar();
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
        this.second = calendar.get(Calendar.SECOND);
        paintClock();
    }

    private void paintClock() {
        // Make the clock responsive to pane size
        double w = getWidth();
        double h = getHeight();
        double radius = Math.min(w, h) * 0.4;  // 80% diameter -> 0.4 radius
        double centerX = w / 2.0;
        double centerY = h / 2.0;

        // Clock face
        Circle circle = new Circle(centerX, centerY, radius);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);

        Text t12 = new Text(centerX - 5, centerY - radius + 12, "12");
        Text t3  = new Text(centerX + radius - 12, centerY + 5, "3");
        Text t6  = new Text(centerX - 3, centerY + radius - 5, "6");
        Text t9  = new Text(centerX - radius + 5, centerY + 5, "9");

        // Hands
        double secondX = centerX + radius * 0.8 * Math.sin(second * (2 * Math.PI / 60));
        double secondY = centerY - radius * 0.8 * Math.cos(second * (2 * Math.PI / 60));
        Line sLine = new Line(centerX, centerY, secondX, secondY);
        sLine.setStroke(Color.RED);

        double minuteX = centerX + radius * 0.65 * Math.sin(minute * (2 * Math.PI / 60));
        double minuteY = centerY - radius * 0.65 * Math.cos(minute * (2 * Math.PI / 60));
        Line mLine = new Line(centerX, centerY, minuteX, minuteY);
        mLine.setStroke(Color.BLUE);

        double hourIn12 = hour % 12 + minute / 60.0;
        double hourX = centerX + radius * 0.5 * Math.sin(hourIn12 * (2 * Math.PI / 12));
        double hourY = centerY - radius * 0.5 * Math.cos(hourIn12 * (2 * Math.PI / 12));
        Line hLine = new Line(centerX, centerY, hourX, hourY);
        hLine.setStroke(Color.GREEN);

        getChildren().setAll(circle, t12, t3, t6, t9, hLine, mLine, sLine);
    }

    @Override
    protected void setWidth(double value) {
        super.setWidth(value);
        paintClock();
    }

    @Override
    protected void setHeight(double value) {
        super.setHeight(value);
        paintClock();
    }
}
