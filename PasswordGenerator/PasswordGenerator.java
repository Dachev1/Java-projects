public class PasswordGenerator {
    private static final String lowerCases = "qwertyuiopasddfghjklzxcvbnm";
    private static final String upperCases = "QWERTYUIOPASDFGHJKLZXCVBNM";
    private static final String symbols = "!@#$%^&*_";

    public static String generate(int lengthOfPassword) {
        StringBuilder generatedPassword = new StringBuilder();

        for (int i = 0; i < lengthOfPassword; i++) {
            int random = (int) (4 * Math.random());

            switch (random) {
                case 0:
                    generatedPassword.append((int) (10 * Math.random()));
                    break;
                case 1:
                    random = (int) (lowerCases.length() * Math.random());
                    generatedPassword.append(lowerCases.charAt(random));
                    break;
                case 2:
                    random = (int) (upperCases.length() * Math.random());
                    generatedPassword.append(upperCases.charAt(random));
                    break;
                case 3:
                    random = (int) (symbols.length() * Math.random());
                    generatedPassword.append(symbols.charAt(random));
                    break;
            }
        }
        return generatedPassword.toString();
    }
}
