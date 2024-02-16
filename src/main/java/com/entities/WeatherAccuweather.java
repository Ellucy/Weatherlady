package com.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "weatheraccuweather")
public class WeatherAccuweather implements DataEntity {

    @Id
    @GeneratedValue
    private UUID weatherId;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "region_name")
    private String regionName;

    @NotNull(message = "City name cannot be null")
    @Column(name = "city_name")
    private String cityName;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @NotNull(message = "Date cannot be null")
    @Column(name = "date")
    private Date date;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "pressure")
    private Integer pressure;

    @Column(name = "humidity")
    private Integer humidity;

    @Column(name = "wind_direction")
    private String windDirection;

    @Column(name = "wind_speed")
    private Double windSpeed;

    @NotNull(message = "Disaster cannot be null")
    @Column(name = "natural_disaster")
    private String naturalDisaster;

    @Column(name = "description")
    private String description;
}
