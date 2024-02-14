package com.handlers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;

public class DateConverterTest {

    @Test
    public void testValidDateStringConversion() {
        String validDateString = "2024-02-14";
        Timestamp result = DateConverter.convertStringToTimestamp(validDateString);
        Assertions.assertNotNull(result, "Result should not be null for a valid date string");
        Assertions.assertEquals(Timestamp.valueOf("2024-02-14 00:00:00"), result, "Timestamp should match the expected value");
    }

    @Test
    public void testInvalidDateStringConversion() {
        String invalidDateString = "Invalid Date";
        Timestamp result = DateConverter.convertStringToTimestamp(invalidDateString);
        Assertions.assertNull(result, "Result should be null for an invalid date string");
    }

}
