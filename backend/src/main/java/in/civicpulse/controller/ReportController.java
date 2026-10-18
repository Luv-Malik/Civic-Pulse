package in.civicpulse.controller;

import in.civicpulse.dto.*;
import in.civicpulse.model.*;
import in.civicpulse.repository.ReportRepository;
import in.civicpulse.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController @RequestMapping("/api")
public class ReportController {
  private final ReportRepository repository; private final IncidentService incidents;
  public ReportController(ReportRepository repository, IncidentService incidents) { this.repository=repository; this.incidents=incidents; }
  @GetMapping("/health") public Map<String,Object> health(){ return Map.of("status","ok","service","CivicPulse API"); }
  @GetMapping("/reports") public List<Report> reports(@RequestParam(required=false) ReportStatus status) { return status==null?repository.findAllByOrderByCreatedAtDesc():repository.findByStatusOrderByCreatedAtDesc(status); }
  @PostMapping("/reports") @ResponseStatus(HttpStatus.CREATED) public Report create(@Valid @RequestBody CreateReportRequest req) {
    Report r=new Report(); r.setTitle(req.title().trim()); r.setDescription(req.description().trim()); r.setCategory(req.category()); r.setSeverity(req.severity()); r.setLatitude(req.latitude()); r.setLongitude(req.longitude()); r.setReporterName(req.reporterName()==null||req.reporterName().isBlank()?"Anonymous":req.reporterName().trim()); r.setStatus(ReportStatus.REPORTED); return repository.save(r);
  }
  @GetMapping("/reports/{id}") public Report get(@PathVariable Long id) { return repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Report not found")); }
  @PatchMapping("/reports/{id}") public Report update(@PathVariable Long id,@Valid @RequestBody UpdateReportRequest req) {
    Report r=get(id); if(req.status()!=null) r.setStatus(req.status()); if(req.assignedTo()!=null) r.setAssignedTo(req.assignedTo().isBlank()?null:req.assignedTo().trim()); return repository.save(r);
  }
  @GetMapping("/incidents") public List<IncidentDto> incidents(){ return incidents.getIncidents(); }
  @GetMapping("/stats") public Map<String,Object> stats() {
    List<Report> all=repository.findAll(); long open=all.stream().filter(r->!List.of(ReportStatus.RESOLVED,ReportStatus.REJECTED).contains(r.getStatus())).count(); long resolved=all.stream().filter(r->r.getStatus()==ReportStatus.RESOLVED).count(); long high=incidents.getIncidents().stream().filter(i->i.priorityScore()>=70).count();
    return Map.of("totalReports",all.size(),"openReports",open,"resolvedReports",resolved,"highPriorityIncidents",high,"incidents",incidents.getIncidents().size());
  }
}
