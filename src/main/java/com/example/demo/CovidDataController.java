package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.ResolutionSyntax;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CovidDataController {

    final CovidDataRepository covidDataRepository;

    @Autowired
    public CovidDataController(CovidDataRepository covidDataRepository) {
        this.covidDataRepository = covidDataRepository;
    }

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<CovidData> getData(@RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate) {

        if (startDate == null || endDate == null) {
            return covidDataRepository.findAll();
        } else {
            return covidDataRepository.getCovidDataBetween(startDate, endDate);
        }
    }

    @GetMapping(value = "/mean", produces = "application/json")
    @ResponseBody
    public CovidDataDTO getMeanData(@RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate) {
        Optional<CovidDataDTO> dataSet;
        try {
            if (startDate == null || endDate == null) {
                dataSet = covidDataRepository.getAverageOfAll();
            } else {
                dataSet = covidDataRepository.getAverageOfAll(startDate, endDate);
            }
            return dataSet.get();
        } catch (Exception e) {
            return new CovidDataDTO(0, 0, 0);
        }
    }

    @GetMapping("/{date}")
    @ResponseBody
    public ResponseEntity<CovidDataDTO> getSpecificData(@PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        Optional<CovidData> covidDataDTO = covidDataRepository.findByDate(date);
        if (covidDataDTO.isPresent()) {
            return ResponseEntity.ok(covidDataDTO.get().toDTO());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping(value = "/median", produces = "application/json")
    @ResponseBody
    public CovidDataDTO getMedianData(@RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate) {

        List<CovidData> dataSet;
        if (startDate == null || endDate == null) {
            dataSet = covidDataRepository.findAll();
        } else {
            dataSet = covidDataRepository.getCovidDataBetween(startDate, endDate);
        }

        CovidDataDTO covidDataDTO = new CovidDataDTO();

        dataSet.sort(Comparator.comparing(CovidData::getCovidCases));
        double median = dataSet.size() % 2 == 0 ?
                (dataSet.get(dataSet.size() / 2 - 1).getCovidCases() + dataSet.get(dataSet.size() / 2).getCovidCases()) / 2.0
                : dataSet.get(dataSet.size() / 2).getCovidCases();
        covidDataDTO.setCovidCases(median);

        dataSet.sort(Comparator.comparing(CovidData::getCovidAdmissions));
        median = dataSet.size() % 2 == 0 ?
                (dataSet.get(dataSet.size() / 2 - 1).getCovidAdmissions() + dataSet.get(dataSet.size() / 2).getCovidAdmissions()) / 2.0
                : dataSet.get(dataSet.size() / 2).getCovidAdmissions();
        covidDataDTO.setCovidAdmissions(median);


        dataSet.sort(Comparator.comparing(CovidData::getCovidDeaths));
        median = dataSet.size() % 2 == 0 ?
                (dataSet.get(dataSet.size() / 2 - 1).getCovidDeaths() + dataSet.get(dataSet.size() / 2).getCovidDeaths()) / 2.0
                : dataSet.get(dataSet.size() / 2).getCovidDeaths();
        covidDataDTO.setCovidDeaths(median);

        return covidDataDTO;
    }

    @PutMapping(path = "/{date}")
    @ResponseBody
    public ResponseEntity putData(@PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date, @RequestBody CovidData covidData) {
        Optional<CovidData> covidDataOptional = covidDataRepository.findByDate(date);
        if (validData(covidData)) {
            if (covidDataOptional.isPresent()) {

                CovidData existingEntry = covidDataOptional.get();
                existingEntry.setCovidCases(covidData.getCovidCases());
                existingEntry.setCovidAdmissions(covidData.getCovidAdmissions());
                existingEntry.setCovidDeaths(covidData.getCovidDeaths());
                covidDataRepository.save(existingEntry);
                return ResponseEntity.ok().build();

            }else{
                covidDataRepository.save(covidData);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }

        } else {
            return ResponseEntity.badRequest().body("Cases, admissions and deaths cannot be negative");
        }

    }

    private boolean validData(CovidData covidData) {
        return (covidData.getCovidCases() >= 0) && (covidData.getCovidDeaths() >= 0) && (covidData.getCovidAdmissions() >= 0);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity createData(@RequestBody CovidData covidData) throws URISyntaxException {
        Optional<CovidData> covidDataOptional = covidDataRepository.findByDate(covidData.getDate());
        if (covidDataOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            if (validData(covidData)) {
                covidDataRepository.save(covidData);
                URI uri = new URI(covidData.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                return ResponseEntity.created(uri).build();
            }

            return ResponseEntity.badRequest().body("Cases, admissions and deaths cannot be negative!");
        }
    }

    @DeleteMapping("{date}")
    @ResponseBody
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity deleteData(@PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        try {
            int deleted = covidDataRepository.deleteByDate(date);
            if (deleted > 0) {
                return ResponseEntity.ok().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
}
