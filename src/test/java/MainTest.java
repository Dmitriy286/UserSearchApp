import junit.framework.TestCase;

import java.io.IOException;
import java.util.*;

public class MainTest extends TestCase {
    Application application;
    Scanner scanner;


    public void testReadFile() throws IOException {
        Map<Integer, ArrayList<Integer>> expectedIdMap = new HashMap<>();
        expectedIdMap.put(1, new ArrayList<>(Arrays.asList(2, 3)));
        expectedIdMap.put(2, new ArrayList<>(Arrays.asList(1, 3, 4)));
        expectedIdMap.put(3, new ArrayList<>(Arrays.asList(1, 2)));
        expectedIdMap.put(4, new ArrayList<>(Arrays.asList(2)));

        Map<Integer, ArrayList<Integer>> actualIdMap = Application.readFile();

        assertEquals(expectedIdMap, actualIdMap);
    }

    public void testFind() throws IOException {
        String forScan="1 4 0";
        scanner = new Scanner(forScan);
        application = new Application(scanner);
        int firstUserId = 1;
        int secondUserId = 4;
        List<Integer> expected = new ArrayList<>(Arrays.asList(1, 2, 4));
        List<Integer> actual = application.find(firstUserId, secondUserId);
        assertEquals(expected, actual);
        scanner.close();
    }

    public void testFindWithExcludeList() throws IOException {
        String forScan="3 4 2 0";
        scanner = new Scanner(forScan);
        application = new Application(scanner);
        int firstUserId = 3;
        int secondUserId = 4;
        List<Integer> excludeList = new ArrayList<>();
        excludeList.add(2);
        List<Integer> expectedList = new ArrayList<>(Arrays.asList(3, 4));
        int expectedSize = 2;
        List<Integer> actualList = application.find(firstUserId, secondUserId, excludeList);
        int actualSize = actualList.size();
        assertEquals(expectedList, actualList);
        assertEquals(expectedSize, actualSize);
        scanner.close();
    }

    public void testFindCutMoreThenSevenItems() throws IOException {
        String forScan="3 4 2 0";
        scanner = new Scanner(forScan);
        application = new Application(scanner);
        application.getFriendsMap().put(3, new ArrayList<>(Arrays.asList(1, 2, 5, 6, 7, 8, 9)));
        application.getFriendsMap().put(4, new ArrayList<>(Arrays.asList(1, 2, 5, 6, 7, 8, 9)));

        int firstUserId = 3;
        int secondUserId = 4;
        List<Integer> excludeList = new ArrayList<>();
        excludeList.add(2);

        List<Integer> expectedList = new ArrayList<>(Arrays.asList(3, 1, 5, 6, 7, 8, 4));
        int expectedSize = 7;
        List<Integer> actualList = application.find(firstUserId, secondUserId, excludeList);
        int actualSize = actualList.size();

        assertEquals(expectedList, actualList);
        assertEquals(expectedSize, actualSize);

        scanner.close();
    }
}