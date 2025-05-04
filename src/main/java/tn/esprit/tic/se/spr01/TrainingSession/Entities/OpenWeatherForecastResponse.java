package tn.esprit.tic.se.spr01.TrainingSession.Entities;


import java.util.List;

public class OpenWeatherForecastResponse {
    private List<ForecastEntry> list;

    public List<ForecastEntry> getList() { return list; }
    public void setList(List<ForecastEntry> list) { this.list = list; }
}
