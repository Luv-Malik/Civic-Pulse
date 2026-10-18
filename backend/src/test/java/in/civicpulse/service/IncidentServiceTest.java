package in.civicpulse.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IncidentServiceTest {
  @Test void haversineDistanceIsZeroForSamePoint() { assertEquals(0, IncidentService.distanceMetres(28.9,77.0,28.9,77.0), 0.001); }
  @Test void nearbyPointsAreCloserThanFarPoints() {
    double nearby=IncidentService.distanceMetres(28.9931,77.0151,28.9935,77.0154);
    double far=IncidentService.distanceMetres(28.9931,77.0151,29.10,77.20);
    assertTrue(nearby < 250); assertTrue(far > 1000);
  }
}
