package tn.esprit.tic.se.spr01.health.services;


import jdk.javadoc.doclet.Reporter;
import tn.esprit.tic.se.spr01.health.dto.ReportDto;
import tn.esprit.tic.se.spr01.health.entities.HealthReport;

import java.util.List;

public interface IHealthReportService {



    public ReportDto addReport(ReportDto reportDto);

    public List<ReportDto> retriveAllReport ();

    public ReportDto retrieveReport(long healthId);
    public void removeReport (long healthId);

    public ReportDto updateReport (ReportDto reportDto);
   // public List<UserDto> getMedicalStaff();
    //public  List<UserDto> getPlayers();

}
