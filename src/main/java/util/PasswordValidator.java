package util;

import java.util.regex.Pattern;

public final class PasswordValidator {

    private static final Pattern STRONG_PASSWORD = Pattern.compile(
            "^(?=\\S{8,20}$)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).*$");

    private static final String REQUIREMENT_MESSAGE =
            "Password must be 8 to 20 characters and include at least one uppercase letter, "
            + "one lowercase letter, one number, one special character, and no spaces.";

    private PasswordValidator() {
    }

    public static boolean isValid(String password) {
        return password != null && STRONG_PASSWORD.matcher(password).matches();
    }

    public static String getRequirementMessage() {
        return REQUIREMENT_MESSAGE;
    }
}
