package in.civicpulse.dto;

import in.civicpulse.model.ReportCategory;
import in.civicpulse.model.ReportStatus;
import java.time.Instant;
import java.util.List;

public record IncidentDto(String incidentKey, String title, ReportCategory category, double latitude, double longitude, int reportCount, int maxSeverity, int priorityScore, String priorityExplanation, ReportStatus status, Instant latestReportAt, List<ReportSummary> reports) {
  public record ReportSummary(Long id, String title, String description, int severity, ReportStatus status, double latitude, double longitude, Instant createdAt) {}
}
