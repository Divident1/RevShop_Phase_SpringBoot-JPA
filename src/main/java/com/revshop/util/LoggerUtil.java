package com.revshop.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    private LoggerUtil() {
        // Private constructor to hide the implicit public one
    }

    public static void info(String message) {
        getCallerLogger().info(message);
    }

    public static void info(String format, Object... arguments) {
        getCallerLogger().info(format, arguments);
    }

    public static void warn(String message) {
        getCallerLogger().warn(message);
    }

    public static void warn(String format, Object... arguments) {
        getCallerLogger().warn(format, arguments);
    }

    public static void error(String message) {
        getCallerLogger().error(message);
    }

    public static void error(String message, Throwable t) {
        getCallerLogger().error(message, t);
    }

    private static Logger getCallerLogger() {
        // Skip current method and public api method
        return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(stream -> stream.skip(2)
                        .findFirst()
                        .map(frame -> LoggerFactory.getLogger(frame.getDeclaringClass()))
                        .orElse(LoggerFactory.getLogger(LoggerUtil.class)));
    }
}
