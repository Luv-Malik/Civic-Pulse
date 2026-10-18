package in.civicpulse.dto;

import in.civicpulse.model.ReportStatus;
import jakarta.validation.constraints.Size;

public record UpdateReportRequest(ReportStatus status, @Size(max=120) String assignedTo) {}
