package tn.esprit.tic.se.spr01.TrainingSession.Entities;



import java.util.List;

public class ForecastEntry {
    private String dt_txt;
    private Main main;
    private List<Weather> weather;
    private Clouds clouds;
    private Rain rain;

    public String getDt_txt() { return dt_txt; }
    public void setDt_txt(String dt_txt) { this.dt_txt = dt_txt; }

    public Main getMain() { return main; }
    public void setMain(Main main) { this.main = main; }

    public List<Weather> getWeather() { return weather; }
    public void setWeather(List<Weather> weather) { this.weather = weather; }

    public Clouds getClouds() { return clouds; }
    public void setClouds(Clouds clouds) { this.clouds = clouds; }

    public Rain getRain() { return rain; }
    public void setRain(Rain rain) { this.rain = rain; }
}
