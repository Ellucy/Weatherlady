package com.dataretrieval;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDetails {
    private String locationKey;
    private String cityName;
    private String regionName;
    private String countryName;
    private double latitude;
    private double longitude;

    public LocationDetails(String locationKey, String cityName, String regionName, String countryName, double latitude, double longitude) {
        this.locationKey = locationKey;
        this.cityName = cityName;
        this.regionName = regionName;
        this.countryName = countryName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
