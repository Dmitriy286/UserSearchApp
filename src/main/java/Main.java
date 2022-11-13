import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final static String FILE = "src/data.txt";
    private final static int MAXLENGTH = 7;

    private static Scanner scanner;
    private static int firstUserId;
    private static int secondUserId;
    private static List<Integer> excludeIdsList = new ArrayList<>();
    private static Map<Integer, ArrayList<Integer>> friendsMap = new HashMap<>();


    public static void main(String[] args) throws InterruptedException, IOException {
        init();
        run();
    }


    public static void init() throws IOException, InterruptedException {
        System.out.println("Initializing...");
        Thread.sleep(1000);

        scanner = new Scanner(System.in);

        friendsMap = readFile();
        firstUserId = scan("Enter first user id:");
        secondUserId = scan("Enter second user id:");
        excludeIdsList = createExcludeIdsList();

        scanner.close();
    }

    public static Map<Integer, ArrayList<Integer>> readFile() throws IOException {
        Map<Integer, ArrayList<Integer>> idMap = new HashMap<>();
        List<String> usersData = Files.readAllLines(Paths.get(FILE), StandardCharsets.UTF_8);
        for(String user: usersData){
            System.out.println(user);
            String[] split = user.split(":");
            int key = Integer.parseInt(split[0]);
            ArrayList<Integer> value = new ArrayList<>();
            for (String id : split[1].split(",")) {
                value.add(Integer.parseInt(id));
            }
            idMap.put(key, value);
        }
        System.out.println(idMap);
        return idMap;
    }

    public static void run() throws InterruptedException {
        System.out.println("Searching for user ids...");
        Thread.sleep(1000);
        if (excludeIdsList.size() == 0) {
            find(firstUserId, secondUserId);
        } else {
            find(firstUserId, secondUserId, excludeIdsList);
        }
    }

    public static int scan(String instruction) {
        int result;
        System.out.println(instruction);
        while(true) {
            try {
                result = scanner.nextInt();
                break;
            } catch (InputMismatchException exception) {
                System.out.println("You have to enter a number, please retry:");
                scanner.nextLine();
                }
            }

        return result;
    }

    public static List<Integer> createExcludeIdsList() {
        List<Integer> idList = new ArrayList<>();
        int nextExcludeId;
        while (true) {
            nextExcludeId = scan("Enter id of a user you want to exclude from filter. If you don't need, enter 0");
            if (nextExcludeId != 0) {
                idList.add(nextExcludeId);
            } else {
                break;
            }
        }

        return idList;
    }

    public static void find(int firstUserId, int secondUserId) {
        List<Integer> firstUserFriendsList = friendsMap.get(firstUserId);
        List<Integer> secondUserFriendsList = friendsMap.get(secondUserId);
        List<Integer> resultList = new ArrayList<>(Arrays.asList(firstUserId, secondUserId));
        for (Integer firstId : firstUserFriendsList) {
            for (Integer secondId : secondUserFriendsList) {
                if (firstId == secondId && resultList.size() <= MAXLENGTH) {
                    resultList.add(resultList.size() -1, firstId);
                } else if (resultList.size() > MAXLENGTH) {
                    break;
                }
            }
        }

        System.out.println(resultList.size() > 2 ? resultList : "Not found");
    }


    public static void find(int firstUserId, int secondUserId, List<Integer> excludeList) {
        List<Integer> firstUserFriendsList = friendsMap.get(firstUserId);
        List<Integer> secondUserFriendsList = friendsMap.get(secondUserId);

        List<Integer> filteredFirstUserFriendsList;
        filteredFirstUserFriendsList = firstUserFriendsList
                .stream()
                .filter(e -> !excludeList.contains(e))
                .collect(Collectors.toList());

        List<Integer> resultList;
        resultList = filteredFirstUserFriendsList
                .stream()
                .filter(secondUserFriendsList::contains)
                .limit(MAXLENGTH)
                .collect(Collectors.toList());

        resultList.add(0, firstUserId);
        resultList.add(resultList.size(), secondUserId);

        System.out.println(resultList.size() > 2 ? resultList : "Not found");
    }

}
