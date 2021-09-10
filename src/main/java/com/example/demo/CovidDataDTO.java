package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CovidDataDTO {
    double covidCases;
    double covidAdmissions;
    double covidDeaths;

    public CovidDataDTO(double covidCases, double covidAdmissions, double covidDeaths) {
        this.covidCases = covidCases;
        this.covidAdmissions = covidAdmissions;
        this.covidDeaths = covidDeaths;
    }

}
