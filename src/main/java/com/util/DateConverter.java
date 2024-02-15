package com.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    public static Timestamp convertStringToTimestamp(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(dateString);
            return new Timestamp(parsedDate.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
