package in.civicpulse.dto;

import in.civicpulse.model.ReportCategory;
import jakarta.validation.constraints.*;

public record CreateReportRequest(
    @NotBlank @Size(max=120) String title,
    @NotBlank @Size(max=3000) String description,
    @NotNull ReportCategory category,
    @Min(1) @Max(5) int severity,
    @DecimalMin("-90.0") @DecimalMax("90.0") double latitude,
    @DecimalMin("-180.0") @DecimalMax("180.0") double longitude,
    @Size(max=100) String reporterName
) {}
