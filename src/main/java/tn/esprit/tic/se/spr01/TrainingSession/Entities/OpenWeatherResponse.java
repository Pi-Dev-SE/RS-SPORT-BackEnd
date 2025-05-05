package tn.esprit.tic.se.spr01.TrainingSession.Entities;



import java.util.List;

public class OpenWeatherResponse {
    private Main main;
    private List<Weather> weather;
    private Clouds clouds;

    public Main getMain() { return main; }
    public void setMain(Main main) { this.main = main; }

    public List<Weather> getWeather() { return weather; }
    public void setWeather(List<Weather> weather) { this.weather = weather; }

    public Clouds getClouds() { return clouds; }
    public void setClouds(Clouds clouds) { this.clouds = clouds; }
}

