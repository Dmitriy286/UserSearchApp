import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Application {
    private final static String FILE = "src/data.txt";
    private final static int MAXLENGTH = 7;

    int firstUserId;
    int secondUserId;
    List<Integer> excludeIdsList;
    Map<Integer, ArrayList<Integer>> friendsMap;

    Scanner scanner;

    public Application(Scanner scanner) throws IOException {
        this.scanner = scanner;
        this.friendsMap = readFile();
        this.firstUserId = scan("Enter first user id:");
        this.secondUserId = scan("Enter second user id:");
        this.excludeIdsList = createExcludeIdsList();
    }

    public Map<Integer, ArrayList<Integer>> getFriendsMap() {
        return friendsMap;
    }

    /**
     * Reads data from file.
     *
     * @return Map of user ids with lists of their friends ids
     * @throws IOException
     */
    public static Map<Integer, ArrayList<Integer>> readFile() throws IOException {
        Map<Integer, ArrayList<Integer>> idMap = new HashMap<>();
        List<String> usersData = Files.readAllLines(Paths.get(FILE), StandardCharsets.UTF_8);
        for (String user : usersData) {
            String[] split = user.split(":");
            int key = Integer.parseInt(split[0]);
            ArrayList<Integer> value = new ArrayList<>();
            for (String id : split[1].split(",")) {
                value.add(Integer.parseInt(id));
            }
            idMap.put(key, value);
        }
        return idMap;
    }

    /**
     * Parses int number from console.
     * @param instruction String instruction for user
     * @return int number
     */
    public int scan(String instruction) {
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

    /**
     * Creates list of ids, which have to be excluded from the search.
     * @return List of ids
     */
    public List<Integer> createExcludeIdsList() {
        List<Integer> idList = new ArrayList<>();
        int nextExcludeId;
        String user;
        int cycleCount = 0;
        while (true) {
            if (cycleCount == 0) {
                user = "first user";
            } else {
                user = "next user";
            }
            nextExcludeId = scan("Enter id of " + user + " you want to exclude from filter. If you don't need, enter 0");

            if (nextExcludeId != 0) {
                idList.add(nextExcludeId);
            } else {
                break;
            }
            cycleCount++;
        }

        return idList;
    }

    /**
     * Find users friends without any exceptions. Result is limited by 7 numbers includes ids of two linked users.
     * @param firstUserId int id of first user
     * @param secondUserId int id of second user
     */
    public List<Integer> find(int firstUserId, int secondUserId) {
        List<Integer> firstUserFriendsList = friendsMap.get(firstUserId);
        List<Integer> secondUserFriendsList = friendsMap.get(secondUserId);
        List<Integer> resultList = new ArrayList<>(Arrays.asList(firstUserId, secondUserId));
        for (Integer firstId : firstUserFriendsList) {
            for (int secondId : secondUserFriendsList) {
                if (firstId == secondId && resultList.size() <= MAXLENGTH) {
                    resultList.add(resultList.size() -1, firstId);
                } else if (resultList.size() > MAXLENGTH) {
                    break;
                }
            }
        }

        System.out.println(resultList.size() > 2 ? resultList : "Not found");
        return resultList;
    }

    /**
     * Find users friends with exception. Result is limited by 7 numbers includes ids of two linked users.
     * @param firstUserId int id of first user
     * @param secondUserId int id of second user
     * @param excludeList List of exclusion ids
     */
    public List<Integer> find(int firstUserId, int secondUserId, List<Integer> excludeList) {
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
                .limit(MAXLENGTH-2)
                .collect(Collectors.toList());

        resultList.add(0, firstUserId);
        resultList.add(resultList.size(), secondUserId);

        System.out.println(resultList.size() > 2 ? resultList : "Not found");
        return resultList;
    }

}
