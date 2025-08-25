package banco.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


public final class IdGenerator {
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private IdGenerator() {}

    public static String accountNumber(String prefix){
        return prefix + "-" +
                LocalDateTime.now().format(FMT) + "-" +
                UUID.randomUUID().toString().substring(0,8).toUpperCase();
    }
}
