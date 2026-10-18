package in.civicpulse.service;

import in.civicpulse.model.*;
import in.civicpulse.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Configuration
public class SeedData {
  @Bean CommandLineRunner seedReports(ReportRepository repo, @Value("${app.seed:true}") boolean enabled) {
    return args -> {
      if (!enabled || repo.count() > 0) return;
      add(repo,"Deep pothole near bus stop","Large pothole on the left side near the bus stop",ReportCategory.POTHOLE,4,28.9931,77.0151,"Demo Citizen",ReportStatus.REPORTED,72);
      add(repo,"Road pothole at bus stop","Deep pothole on road close to bus stop; vehicles swerving",ReportCategory.POTHOLE,5,28.9935,77.0154,"Sample User",ReportStatus.VERIFIED,48);
      add(repo,"Water leaking from pipe","Water is flowing continuously from a roadside pipe",ReportCategory.WATER_LEAK,3,28.9895,77.0200,"Demo Citizen",ReportStatus.REPORTED,30);
      add(repo,"Street light not working","Street lamp near crossing is not turning on",ReportCategory.STREETLIGHT,2,28.9960,77.0120,"Sample User",ReportStatus.ASSIGNED,20);
      add(repo,"Garbage overflowing","Garbage pile overflowing near market entrance",ReportCategory.GARBAGE,3,28.9900,77.0180,"Demo Citizen",ReportStatus.IN_PROGRESS,15);
      add(repo,"Drain blocked after rain","Drain is blocked and water is collecting on the road",ReportCategory.DRAINAGE,4,28.9870,77.0170,"Sample User",ReportStatus.REPORTED,10);
    };
  }
  private void add(ReportRepository repo,String title,String desc,ReportCategory cat,int severity,double lat,double lon,String name,ReportStatus status,long hoursAgo) {
    Report r=new Report(); r.setTitle(title); r.setDescription(desc); r.setCategory(cat); r.setSeverity(severity); r.setLatitude(lat); r.setLongitude(lon); r.setReporterName(name); r.setStatus(status); r.setCreatedAt(Instant.now().minus(hoursAgo, ChronoUnit.HOURS)); r.setUpdatedAt(Instant.now()); repo.save(r);
  }
}
