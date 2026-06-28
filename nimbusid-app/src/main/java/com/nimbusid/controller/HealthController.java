package com.nimbusid.controller;

import com.nimbusid.dto.HealthResponse;
import com.nimbusid.platform.factory.ResponseFactory;
import com.nimbusid.platform.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    private final ResponseFactory responseFactory;

    public HealthController(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    @GetMapping
    @Operation(summary = "Application health check")
    public ApiResponse<HealthResponse> health() {

        HealthResponse response = new HealthResponse(
                "UP",
                "NimbusID",
                "0.0.1"
        );

        return responseFactory.ok(response);
    }
}
