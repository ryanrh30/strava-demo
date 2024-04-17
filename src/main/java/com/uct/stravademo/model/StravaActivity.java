package com.uct.stravademo.model;

import lombok.Data;

@Data
public class StravaActivity {
    private Long id;
    private String name;
    private Double distance;
    private Integer movingTime;
    private Integer elapsedTime;
    private Double totalElevationGain;
    private String type;
    private String startDate;
    private String startDateLocal;
    private String timezone;
    private String locationCountry;
    private Boolean trainer;
    private Boolean commute;
    private Boolean manual;
    private Boolean privateActivity;
    private Double averageSpeed;
    private Double maxSpeed;
    private Boolean hasHeartRate;


}

