package tn.esprit.tic.se.spr01.TrainingSession.Entities;

public class TimelineSegment {
    private String label;
    private int duration;
    private String color;
    private int start;
    private int end;

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getStart() { return start; }
    public void setStart(int start) { this.start = start; }

    public int getEnd() { return end; }
    public void setEnd(int end) { this.end = end; }
}
