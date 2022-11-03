package com.knightboost.apm.trace.api.internal;

import java.util.logging.Level;
import java.util.regex.Pattern;

public class ValidationUtil {

    public static final String API_USAGE_LOGGER_NAME = "com.knightboost.apm.trace";

    /**
     * Instrument names MUST conform to the following syntax.
     *
     * <ul>
     *   <li>They are not null or empty strings.
     *   <li>They are case-insensitive, ASCII strings.
     *   <li>The first character must be an alphabetic character.
     *   <li>Subsequent characters must belong to the alphanumeric characters, '_', '.', and '-'.
     *   <li>They can have a maximum length of 63 characters.
     * </ul>
     */
    private static final Pattern VALID_INSTRUMENT_NAME_PATTERN =
            Pattern.compile("([A-Za-z]){1}([A-Za-z0-9\\_\\-\\.]){0,62}");


    /**
     * Log the {@code message} to the {@link #API_USAGE_LOGGER_NAME API Usage Logger}.
     *
     * <p>Log at {@link Level#FINEST} and include a stack trace.
     */
    public static void log(String message) {
        log(message, Level.FINEST);
    }

    /**
     * Log the {@code message} to the {@link #API_USAGE_LOGGER_NAME API Usage Logger}.
     *
     * <p>Log includes a stack trace.
     */
    public static void log(String message, Level level) {

    }
}
