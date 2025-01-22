package com.example.JsonNeo.controller;

import org.springframework.web.bind.annotation.*;

import com.example.JsonNeo.service.CypherQueryService;

import java.util.Map;

@RestController
@RequestMapping("/api/neo4j")
public class CypherQueryController {
    private final CypherQueryService cypherQueryService;

    public CypherQueryController(CypherQueryService cypherQueryService) {
        this.cypherQueryService = cypherQueryService;
    }

    @PostMapping("/execute")
    public Object executeCypherQuery(@RequestBody Map<String, Object> payload) {
        String cypherQuery = (String) payload.get("queries");
        Map<String, Object> parameters = (Map<String, Object>) payload.getOrDefault("parameters", Map.of());
        return cypherQueryService.executeQuery(cypherQuery);
    }
}
