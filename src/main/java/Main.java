import java.io.IOException;
import java.util.*;


public class Main {
    private static Scanner scanner;
    private static Application application;

    public static void main(String[] args) throws InterruptedException, IOException {
        init();
        run();
    }

    public static void init() throws IOException, InterruptedException {
        System.out.println("Initializing...");
        Thread.sleep(1000);

        scanner = new Scanner(System.in);
        application = new Application(scanner);

        scanner.close();
    }

    public static void run() throws InterruptedException {
        System.out.println("Searching for user ids...");
        Thread.sleep(1000);
        if (application.excludeIdsList.size() == 0) {
            application.find(application.firstUserId, application.secondUserId);
        } else {
            application.find(application.firstUserId, application.secondUserId, application.excludeIdsList);
        }
    }

}
