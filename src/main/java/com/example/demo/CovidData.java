package com.example.demo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Getter
@Setter
@Entity
public class CovidData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false)
    @JsonIgnore
    private Long id;

    @Column(name = "cases")
    private int covidCases;
    @Column(name = "admissions")
    private int covidAdmissions;
    @Column(name = "deaths")
    private int covidDeaths;

    @Column(name = "date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate date;

    public CovidData(int covidCases, int covidAdmissions, int covidDeaths, LocalDate date) {
        this.covidCases = covidCases;
        this.covidAdmissions = covidAdmissions;
        this.covidDeaths = covidDeaths;
        this.date = date;
    }

    public CovidData(int covidCases, int covidAdmissions, int covidDeaths) {
        this.covidCases = covidCases;
        this.covidAdmissions = covidAdmissions;
        this.covidDeaths = covidDeaths;
    }

    public CovidData() {

    }

    public CovidDataDTO toDTO() {
        return new CovidDataDTO(covidCases,covidAdmissions,covidDeaths);
    }


//    @Override
//    public String toString() {
//        return "CovidData{" +
//                ", covidCases=" + covidCases +
//                ", covidAdmissions=" + covidAdmissions +
//                ", covidDeaths=" + covidDeaths +
//                ", " + getCalendarString(date) +
//                '}';
//    }
//
//    public String getCalendarString(Calendar date){
//        return "date="+date.get(Calendar.YEAR)+" "+(date.get(Calendar.MONTH)+1)+" "+date.get(Calendar.DAY_OF_MONTH);
//    }
}
