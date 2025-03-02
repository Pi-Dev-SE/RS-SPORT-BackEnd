package pi_4se3.backend.services;

import pi_4se3.backend.dto.ReportDto;
import pi_4se3.backend.dto.UserDto;
import pi_4se3.backend.entities.HealthReport;
import pi_4se3.backend.entities.User;

import java.util.List;

public interface IHealthReportService {

    public ReportDto addReport(ReportDto reportDto);

    public List<ReportDto> retriveAllReport ();

    public ReportDto retrieveReport(long healthId);
    public void removeReport (long healthId);

    public ReportDto updateReport (ReportDto reportDto);
    public List<UserDto> getMedicalStaff();
    public  List<UserDto> getPlayers();



}
