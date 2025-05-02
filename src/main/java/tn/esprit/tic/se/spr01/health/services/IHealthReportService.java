package tn.esprit.tic.se.spr01.health.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.esprit.tic.se.spr01.health.dto.ReportDto;
import tn.esprit.tic.se.spr01.health.dto.UserDto;

import java.util.List;

public interface IHealthReportService {

    public ReportDto addReport(ReportDto reportDto);

    //public List<ReportDto> retriveAllReport ();
    public Page<ReportDto> retriveAllReport(Pageable pageable);
    public ReportDto retrieveReport(long healthId);
    public void removeReport (long healthId);
    public ReportDto updateReport (ReportDto reportDto);
    public List<UserDto> getMedicalStaff();
    public  List<UserDto> getPlayers();



}
