package kr.co.onmediagroup.health.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HealthControllerTest {

    private final HealthController healthController = new HealthController();

    @Test
    @DisplayName("헬스체크 정상 응답 - status=UP, timestamp 포함")
    void health_Success() {
        // Given
        LocalDateTime before = LocalDateTime.now();

        // When
        HealthController.HealthResponse response = healthController.health();

        // Then
        LocalDateTime after = LocalDateTime.now();
        assertNotNull(response);
        assertEquals("UP", response.status());
        assertNotNull(response.timestamp());
        assertFalse(response.timestamp().isBefore(before));
        assertFalse(response.timestamp().isAfter(after));
    }
}
