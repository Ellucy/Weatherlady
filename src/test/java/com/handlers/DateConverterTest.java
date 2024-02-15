package com.handlers;

import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

public class DateConverterTest {

        @Test
        public void testConvertStringToTimestamp() {
            DateConverter dateConverter = new DateConverter();
            String dateString = "2024-02-15";
            Timestamp expectedTimestamp = Timestamp.valueOf("2024-02-15 00:00:00");

            Timestamp actualTimestamp = dateConverter.convertStringToTimestamp(dateString);

            assertEquals(expectedTimestamp, actualTimestamp);
        }

        @Test
        public void testConvertStringToTimestampWithInvalidDate() {
            DateConverter dateConverter = new DateConverter();
            String dateString = "InvalidDate";

            Timestamp actualTimestamp = dateConverter.convertStringToTimestamp(dateString);

            assertNull(actualTimestamp);
        }
    }
