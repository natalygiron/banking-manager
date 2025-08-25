package banco;

import java.util.regex.Pattern;

final class EmailValidator {
    private static final Pattern P = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$");
    static boolean esEmailValido(String email) {
        return email != null && P.matcher(email).matches();
    }
}
