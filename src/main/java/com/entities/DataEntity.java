package com.entities;

import java.util.Date;

public abstract class DataEntity {

    public abstract String getNaturalDisaster();

    public abstract String getDescription();

    public abstract String getCountryName();

    public abstract String getCityName();

    public abstract Double getLatitude();

    public abstract Double getLongitude();

    public abstract Date getDate();

    public abstract Double getTemperature();

    public abstract Integer getPressure();

    public abstract Integer getHumidity();

    public abstract Double getWindSpeed();

    public abstract String getWindDirection();

}
