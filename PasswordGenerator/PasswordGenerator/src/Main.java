import java.util.Scanner;

public class Main {
    private static final String INSERT_LENGTH = "Want length for password do u want it: ";
    private static final String WHEN_LENGTH_IS_INCORRECT = "Sorry the password must at least 5 character long. Try again :)";
    private static final String TRY_AGAIN = "Want length for password do u want it: ";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int lengthOfPassword;

        System.out.print(INSERT_LENGTH);
        lengthOfPassword = scanner.nextInt();

        while (lengthOfPassword < 5) {
            System.out.println(WHEN_LENGTH_IS_INCORRECT);
            System.out.print(TRY_AGAIN);
            lengthOfPassword = scanner.nextInt();
        }

        System.out.println(PasswordGenerator.generate(lengthOfPassword));
    }
}