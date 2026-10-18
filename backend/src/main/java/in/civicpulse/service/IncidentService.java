package in.civicpulse.service;

import in.civicpulse.dto.IncidentDto;
import in.civicpulse.model.Report;
import in.civicpulse.model.ReportStatus;
import in.civicpulse.repository.ReportRepository;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class IncidentService {
    private static final double DUPLICATE_RADIUS_METRES = 250.0;
    private final ReportRepository repository;
    public IncidentService(ReportRepository repository) { this.repository = repository; }

    public List<IncidentDto> getIncidents() {
        List<Report> all = repository.findAllByOrderByCreatedAtDesc().stream()
            .filter(r -> r.getStatus() != ReportStatus.REJECTED).toList();
        List<List<Report>> groups = new ArrayList<>();
        Set<Long> assigned = new HashSet<>();
        for (Report seed : all) {
            if (assigned.contains(seed.getId())) continue;
            List<Report> group = new ArrayList<>(); group.add(seed); assigned.add(seed.getId());
            boolean changed;
            do {
                changed = false;
                for (Report candidate : all) {
                    if (assigned.contains(candidate.getId())) continue;
                    for (Report member : group) {
                        if (isLikelyDuplicate(member, candidate)) { group.add(candidate); assigned.add(candidate.getId()); changed = true; break; }
                    }
                }
            } while (changed);
            groups.add(group);
        }
        return groups.stream().map(this::toDto).sorted(Comparator.comparingInt(IncidentDto::priorityScore).reversed()).toList();
    }

    private boolean isLikelyDuplicate(Report a, Report b) {
        if (a.getCategory() != b.getCategory()) return false;
        if (distanceMetres(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude()) > DUPLICATE_RADIUS_METRES) return false;
        double similarity = jaccard(normalise(a.getTitle()+" "+a.getDescription()), normalise(b.getTitle()+" "+b.getDescription()));
        return similarity >= 0.12;
    }
    private String normalise(String text) { return text.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9\s]", " ").replaceAll("\s+", " ").trim(); }
    private double jaccard(String a, String b) {
        Set<String> x = new HashSet<>(Arrays.asList(a.split("\s+"))); Set<String> y = new HashSet<>(Arrays.asList(b.split("\s+")));
        x.remove(""); y.remove(""); if (x.isEmpty() || y.isEmpty()) return 0;
        Set<String> intersection = new HashSet<>(x); intersection.retainAll(y);
        Set<String> union = new HashSet<>(x); union.addAll(y); return (double)intersection.size()/union.size();
    }
    public static double distanceMetres(double lat1, double lon1, double lat2, double lon2) {
        double earth=6371000, dLat=Math.toRadians(lat2-lat1), dLon=Math.toRadians(lon2-lon1);
        double x=Math.sin(dLat/2)*Math.sin(dLat/2)+Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*Math.sin(dLon/2)*Math.sin(dLon/2);
        return earth*2*Math.atan2(Math.sqrt(x), Math.sqrt(1-x));
    }
    private IncidentDto toDto(List<Report> group) {
        Report first = group.get(0);
        double lat=group.stream().mapToDouble(Report::getLatitude).average().orElse(first.getLatitude());
        double lon=group.stream().mapToDouble(Report::getLongitude).average().orElse(first.getLongitude());
        int maxSeverity=group.stream().mapToInt(Report::getSeverity).max().orElse(1);
        long ageHours=group.stream().map(Report::getCreatedAt).mapToLong(t -> Math.max(0, Duration.between(t, Instant.now()).toHours())).max().orElse(0);
        int severityPoints=maxSeverity*10; int reportPoints=Math.min(30, (group.size()-1)*10); int agePoints=(int)Math.min(20, ageHours/12);
        int score=Math.min(100, severityPoints+reportPoints+agePoints);
        String explanation="Severity contributes "+severityPoints+"/50; "+group.size()+" report(s) contribute "+reportPoints+"/30; age contributes "+agePoints+"/20. Verify before acting.";
        ReportStatus status=group.stream().map(Report::getStatus).min(Comparator.comparingInt(this::statusOrder)).orElse(ReportStatus.REPORTED);
        Instant latest=group.stream().map(Report::getCreatedAt).max(Instant::compareTo).orElse(Instant.now());
        List<IncidentDto.ReportSummary> summaries=group.stream().map(r -> new IncidentDto.ReportSummary(r.getId(),r.getTitle(),r.getDescription(),r.getSeverity(),r.getStatus(),r.getLatitude(),r.getLongitude(),r.getCreatedAt())).toList();
        return new IncidentDto("INC-"+group.stream().map(Report::getId).min(Long::compareTo).orElse(0L), first.getTitle(), first.getCategory(), lat, lon, group.size(), maxSeverity, score, explanation, status, latest, summaries);
    }
    private int statusOrder(ReportStatus s) { return switch(s) { case REPORTED -> 0; case VERIFIED -> 1; case ASSIGNED -> 2; case IN_PROGRESS -> 3; case RESOLVED -> 4; case REJECTED -> 5; }; }
}
