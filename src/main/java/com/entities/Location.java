package com.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "location")
public class Location {

    @Id
    @GeneratedValue
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
//    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
//    @Type(type= "uuid-char")
    private UUID id;

    @Column(nullable = false)
    @NotNull(message = "Longitude cannot be null")
    private Double longitude;

    @Column(nullable = false)
    @NotNull(message = "Latitude cannot be null")
    private Double latitude;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "City name cannot be empty")
    private String cityName;

    private String region;

    @Column(nullable = false)
    @NotEmpty(message = "Country name cannot be empty")
    private String countryName;
}