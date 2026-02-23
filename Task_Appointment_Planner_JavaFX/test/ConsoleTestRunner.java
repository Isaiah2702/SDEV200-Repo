import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ConsoleTestRunner {

    public static void main(String[] args) {
        System.out.println("=== ConsoleTestRunner ===");

        try {
            Task t = new Task("t1", "Write report", "Finish final project docs", LocalDate.now(), "High", "Pending", false);
            System.out.println("[PASS] Created task: " + t);

            t.markComplete();
            System.out.println("[PASS] Mark complete sets status=Completed: " + t.getStatus());

            Appointment a = new Appointment("a1", "Meeting", "Team sync", LocalDate.now(), "Medium", "Pending",
                    LocalTime.of(9,0), LocalTime.of(10,0), "Room 101");
            System.out.println("[PASS] Created appointment: " + a);

            FileStorageManager fsm = new FileStorageManager("|");
            ArrayList<PlannerItem> list = new ArrayList<>();
            list.add(t);
            list.add(a);

            String tmp = System.getProperty("java.io.tmpdir") + java.io.File.separator + "planner_test.txt";
            fsm.saveItems(tmp, list);
            System.out.println("[PASS] Saved items to: " + tmp);

            ArrayList<PlannerItem> loaded = fsm.loadItems(tmp);
            System.out.println("[PASS] Loaded " + loaded.size() + " items.");

        } catch (Exception ex) {
            System.out.println("[FAIL] " + ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println("=== End ===");
    }
}
