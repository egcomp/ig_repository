import java.util.Scanner;

public class Letter {
    public String header;
    public String text;
    public String sender;
    public String receiver;

    public Letter(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public void setHeader(Scanner scanner) {
        System.out.print("Enter a letter header: ");
        header = scanner.nextLine();
    }

    public void setText(Scanner scanner) {
        System.out.print("Enter letter text: ");
        text = scanner.nextLine();
    }

    public boolean wrightLetter(Scanner scanner) {
        setHeader(scanner);
        setText(scanner);
        return true;
    }
}
