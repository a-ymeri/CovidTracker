package com.example.demo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;


@Repository
@Transactional
public interface CovidDataRepository extends JpaRepository<CovidData, Long> {
    @Query("SELECT d from CovidData d where d.date >= :startingDate and d.date <= :endingDate")
    ArrayList<CovidData> getCovidDataBetween(@Param("startingDate") LocalDate startingDate, @Param("endingDate") LocalDate endingDate);

    @Query("select new com.example.demo.CovidDataDTO(avg(d.covidCases) ,avg(d.covidAdmissions), avg(d.covidDeaths))" +
            "from CovidData d " +
            "where d.date >= :startingDate and d.date <= :endingDate")
    Optional<CovidDataDTO> getAverageOfAll(@Param("startingDate") LocalDate startingDate, @Param("endingDate") LocalDate endingDate);

    @Query("select new com.example.demo.CovidDataDTO(avg(d.covidCases) ,avg(d.covidAdmissions), avg(d.covidDeaths))" +
            "from CovidData d")
    Optional<CovidDataDTO> getAverageOfAll();

    @Query("select cd from CovidData cd order by cd.date asc")
    List<CovidData> findAll();

    Optional<CovidData> findByDate(@DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date);

    int deleteByDate(LocalDate localDate);


//    @Query("select d.covidAdmissions from CovidData d order by d.covidAdmissions asc")
//    ArrayList<Integer> getAdmissionsSorted();
//
//    @Query("select d.covidDeaths from CovidData d order by d.covidDeaths asc")
//    ArrayList<Integer> getDeathsSorted();
//
//    @Query("select d.covidCases from CovidData d order by d.covidCases asc")
//    ArrayList<Integer> getCasesSorted();
//
//    @Query("select d.covidAdmissions from CovidData d order by d.covidAdmissions asc")
//    ArrayList<Integer> getCovidAdmissionsSortedBetween(@Param("startingDate") GregorianCalendar startingDate, @Param("endingDate") GregorianCalendar endingDate);
//
//    @Query("select d.covidDeaths from CovidData d order by d.covidDeaths asc")
//    ArrayList<Integer> getCovidDeathsBetween(@Param("startingDate") GregorianCalendar startingDate, @Param("endingDate") GregorianCalendar endingDate);
//
//    @Query("select d.covidCases from CovidData d order by d.covidCases asc")
//    ArrayList<Integer> getCovidCasesBetween(@Param("startingDate") GregorianCalendar startingDate, @Param("endingDate") GregorianCalendar endingDate);

}
