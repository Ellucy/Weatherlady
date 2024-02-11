package com.entities;

import com.retrievedata.WeatherData;
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
@Table(name= "weatheropenweather")
public class WeatherOpenweather extends WeatherData {

        @Id
        @GeneratedValue
        private UUID weatherId;

        @Column(name = "country")
        private String country;

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

        @Column(name = "wind_Degree")
        private String windDegree;

        @Column(name = "wind_speed")
        private Double windSpeed;

        @NotNull(message = "Disaster cannot be null")
        @Column(name = "natural_disaster")
        private String naturalDisaster;

        @Column(name = "description")
        private String description;
}
