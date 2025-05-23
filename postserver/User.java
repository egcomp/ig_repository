import java.util.*;

public class User {
    public String user;
    public List<String> inbox = new ArrayList<>();
    public List<String> spam = new ArrayList<>();
    public List<String> outbox = new ArrayList<>();
    public Map<String, String> filters = new HashMap<>();

    public User(String user) {
        this.user = user;
    }

    private boolean hasWordSpam(Letter letter) {
        String allText = letter.header + " " + letter.text;
        System.out.println("From hasWordSpam" + allText);
        return allText.toLowerCase().contains("spam");
    }

    private boolean hasKeyWords(Letter letter, String keyWords) {
        String[] keys = keyWords.split("[^a-zA-Z0-9]");
        String allText = (letter.header + " " + letter.text).toLowerCase();
        for (String key : keys) {
            if (allText.contains(key))
                return true;
        }
        return false;
    }

    private boolean isRepetition(Letter letter, int numberRepetitions) {
        String allText = letter.text.toLowerCase();
        String[] words = allText.split(" ");
        if (words.length == 0 || allText.trim().isEmpty())
            return false;
        Map<String, Integer> wordsMap = new HashMap<>();
        for (String word : words) {
            if (wordsMap.containsKey(word))
                wordsMap.put(word, wordsMap.get(word) + 1);
            else
                wordsMap.put(word, 1);
        }
        return Collections.max(wordsMap.values()) > numberRepetitions;
    }

    private boolean isSpam(User receiver, Letter letter) {
//        for (Map.Entry<String, String> entry : receiver.filters.entrySet()) {
        for (String key : receiver.filters.keySet()) {
//            String key = entry.getKey();
            String value = receiver.filters.get(key);
            switch (key) {
                case "simple":
                    if (value.contains("yes") &&
                            hasWordSpam(letter))
                        return true;
                    break;
                case "keywords":
                    if (hasKeyWords(letter, value))
                        return true;
                    break;
                case "repetition":
                    if (isRepetition(letter, Integer.parseInt(value)))
                        return true;
                    break;
                case "sender":
                    if (letter.sender.equals(receiver.filters.get("sender")))
                        return true;
                    break;
                default:
                    return false;
            }
        }
        return false;
    }

    public void sendMail(User receiver, Letter letter) {
        outbox.add(receiver.user + " " + letter.header);
        if (isSpam(receiver, letter))
            receiver.spam.add(user + " " + letter.header);
        else
            receiver.inbox.add(user + " " + letter.header);
    }

    private boolean isNumber(String string) {
        for (char c : string.toCharArray())
            if (!Character.isDigit(c))
                return false;
        return true;
    }

    public void setFilter(Scanner scanner) {
        while (true) {
            System.out.print("Enter filter name or empty line: ");
            String filterName = scanner.nextLine().trim().toLowerCase();
            switch (filterName) {
                case "simple":
                    filters.put(filterName, "yes");
                    break;
                case "keywords":
                    System.out.print("Enter keywords through space: ");
                    String keys = scanner.nextLine().trim().toLowerCase();
                    filters.remove(filterName);
                    filters.put(filterName, keys);
                    break;
                case "repetition":
                    System.out.print("Enter maximum number of repetitions: ");
                    String numberOfRepetitions = scanner.nextLine().trim().toLowerCase();
                    if (!isNumber(numberOfRepetitions)) {
                        System.out.println("Not a number");
                        break;
                    }
                    filters.put(filterName, numberOfRepetitions);
                    break;
                case "sender":
                    String userName = Main.getUser("Enter the sender name: ");
                    if (!userName.isEmpty())
                        filters.put(filterName, userName);
                    break;
                default:
                    if (filterName.isEmpty())
                        return;
            }
        }
    }
}

