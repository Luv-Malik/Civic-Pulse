package in.civicpulse.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "civic_reports", indexes = { @Index(name="idx_report_status", columnList="status"), @Index(name="idx_report_created", columnList="created_at") })
public class Report {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=120) private String title;
    @Column(nullable=false, length=3000) private String description;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=30) private ReportCategory category;
    @Column(nullable=false) private int severity;
    @Column(nullable=false) private double latitude;
    @Column(nullable=false) private double longitude;
    @Column(name="reporter_name", length=100) private String reporterName;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=30) private ReportStatus status = ReportStatus.REPORTED;
    @Column(length=120) private String assignedTo;
    @Column(name="created_at", nullable=false, updatable=false) private Instant createdAt = Instant.now();
    @Column(name="updated_at", nullable=false) private Instant updatedAt = Instant.now();
    @PreUpdate void onUpdate() { updatedAt = Instant.now(); }
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public ReportCategory getCategory(){return category;} public void setCategory(ReportCategory v){category=v;}
    public int getSeverity(){return severity;} public void setSeverity(int v){severity=v;}
    public double getLatitude(){return latitude;} public void setLatitude(double v){latitude=v;}
    public double getLongitude(){return longitude;} public void setLongitude(double v){longitude=v;}
    public String getReporterName(){return reporterName;} public void setReporterName(String v){reporterName=v;}
    public ReportStatus getStatus(){return status;} public void setStatus(ReportStatus v){status=v;}
    public String getAssignedTo(){return assignedTo;} public void setAssignedTo(String v){assignedTo=v;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant v){createdAt=v;}
    public Instant getUpdatedAt(){return updatedAt;} public void setUpdatedAt(Instant v){updatedAt=v;}
}
