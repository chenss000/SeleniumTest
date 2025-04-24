package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public static void info(String message) {
        System.out.println("[INFO] " + message);
        logger.info(message);
    }

    public static void error(String message) {
        System.err.println("[ERROR] " + message);
        logger.error(message);
    }
}
