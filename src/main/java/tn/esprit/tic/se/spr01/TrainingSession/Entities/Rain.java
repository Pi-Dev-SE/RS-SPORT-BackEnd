package tn.esprit.tic.se.spr01.TrainingSession.Entities;

public class Rain {
    private Double _3h; // Precipitation volume for the last 3 hours, mm

    public Double get3h() { return _3h != null ? _3h : 0.0; }
    public void set3h(Double _3h) { this._3h = _3h; }
}
