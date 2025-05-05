package tn.esprit.tic.se.spr01.TrainingSession.Entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class WeatherData {
    private String city;
    private LocalDate date;
    private double temperature; // in Celsius
    private String condition; // e.g., "Rain", "Sunny", "Cloudy"
    private double precipitationProbability; // in percentage
    private String recommendation;
}
