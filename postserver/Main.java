import java.util.*;

public class Main {
    public static Scanner scanner;
    public static String user = "";
    public static List<String> commands = new ArrayList<>();
    public static List<String> users = new ArrayList<>();
    public static Map<String, User> dataBase = new HashMap<>();
    public static User userObject;

    private static void init() {
        String[] temp = {"add", "list", "send", "inbox", "spam", "outbox", "setfilter", "user", "exit"};
        commands = Arrays.asList(temp);
        scanner = new Scanner(System.in);
    }

    private static List<String> getTokens(String string, boolean makeLowerCase) {
        String[] temp = string.split("[^A-Za-z0-9]");
        List<String> list = new ArrayList<>();
        for (String token: temp)
            if (makeLowerCase)
                list.add(token.toLowerCase());
            else
                list.add(token);
        return list;
    }

    private static String getCommand() {
        System.out.printf("Enter a command (%s): ", user);
        String line = scanner.nextLine().trim().toLowerCase();
        if (user.isEmpty() && !line.equals("add")) {
            System.out.println("First command must be 'add'! No one user is defined");
            return "Error";
        }
        int length = line.length();
        List<String> tokens = getTokens(line, true);
        if (tokens.isEmpty() || tokens.getFirst().length() != length || tokens.size() > 1
                             || !commands.contains(tokens.getFirst())) {
            System.out.println("Wrong command");
            return "Error";
        }
        return tokens.getFirst();
    }

    private static void setUserActive(String name, boolean printIfExist) {
        if (!users.contains(name)) {
            users.add(name);
            Collections.sort(users);
            dataBase.put(name, new User(name));
        } else if (printIfExist)
            System.out.printf("User %s is already exist%n", name);
        user = name;
        userObject = dataBase.get(name);
    }

    public static String getUser(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        List<String> tokens = getTokens(line, false);
        if (tokens.size() > 1) {
            System.out.println("Name must be one word and consist of letters and digits only");
            return "";
        }
        if (tokens.getFirst().isEmpty()) {
            System.out.println("Name can not be a blank line");
            return "";
        }
        if (Character.isDigit(tokens.getFirst().charAt(0))) {
            System.out.println("First character of a name must be a letter");
            return "";
        }
        return tokens.getFirst();
    }

    private static void printListOfUsers() {
        if (users.isEmpty())
            System.out.println("No users");
        else {
            System.out.println("List of users: ");
            for (String user : users)
                System.out.println(user);
        }
    }

    private static void changeUser() {
        String name = getUser("Enter an existing user name: ");
        if (!users.contains(name))
            System.out.printf("User %s is not found%n", name);
        else
            setUserActive(name, false);
    }

    private static void sendLetter() {
        String name = getUser("Enter the receiver name: ");
        if (name.isEmpty())
            return;
        if (users.contains(name)) {
            User receiver = dataBase.get(name);
            Letter letter = new Letter(user, receiver.user);
            if (letter.wrightLetter(scanner)) {
                userObject.sendMail(receiver, letter);
                System.out.printf("Letter from %s to %s is sent%n", user, receiver.user);
            } else
                System.out.println("Something got wrong. Try later");
        } else
            System.out.printf("User %s is not found%n", name);
    }

    private static void printInbox() {
        System.out.println("Inbox: ");
        for (String mail : userObject.inbox) {
            System.out.println(mail);
        }
    }

    private static void printSpam() {
        System.out.println("Spam: ");
        for (String mail : userObject.spam) {
            System.out.println(mail);
        }
    }

    private static void printOutbox() {
        System.out.println("Outbox: ");
        for (String mail : userObject.outbox) {
            System.out.println(mail);
        }
    }

    public static void main(String[] args) {
        init();
        while (true) {
            String command = getCommand();
            if (!command.equals("Error"))
                switch (command) {
                    case "add":
                        String name = getUser("Enter a new user name: ");
                        if (!name.isEmpty())
                            setUserActive(name, true);
                        break;
                    case "list":
                        printListOfUsers();
                        break;
                    case "send":
                        sendLetter();
                        break;
                    case "inbox":
                        printInbox();
                        break;
                    case "spam":
                        printSpam();
                        break;
                    case "outbox":
                        printOutbox();
                        break;
                    case "setfilter":
                        userObject.setFilter(scanner);
                        break;
                    case "user":
                        changeUser();
                        break;
                    case "exit":
                        return;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
        }
    }
}
