package com.entities;

import java.util.Date;

public interface DataEntity {

    String getNaturalDisaster();
    String getDescription();
    String getCountryName();
    String getCityName();
    Double getLatitude();
    Double getLongitude();
    Date getDate();
    Double getTemperature();
    Integer getPressure();
    Integer getHumidity();
    Double getWindSpeed();
    String getWindDirection();

}
