package com.util;

import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

public class DateConverterTest {

        @Test
        public void testConvertStringToTimestamp() {

            String dateString = "2024-02-15";
            Timestamp expectedTimestamp = Timestamp.valueOf("2024-02-15 00:00:00");

            Timestamp actualTimestamp = DateConverter.convertStringToTimestamp(dateString);

            assertEquals(expectedTimestamp, actualTimestamp);
        }

        @Test
        public void testConvertStringToTimestampWithInvalidDate() {

            String dateString = "InvalidDate";

            Timestamp actualTimestamp = DateConverter.convertStringToTimestamp(dateString);

            assertNull(actualTimestamp);
        }
    }
