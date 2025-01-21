package com.example.JsonNeo.service;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CypherQueryService {
    private final Neo4jClient neo4jClient;

    public CypherQueryService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public Object executeQuery(String cypherQuery, Map<String, Object> parameters) {
        try {
            return neo4jClient.query(cypherQuery)
                    .bindAll(parameters) // Bind parameters dynamically
                    .fetch()
                    .all();
        } catch (Exception e) {
            // Handle errors
            throw new RuntimeException("Failed to execute Cypher query: " + e.getMessage());
        }
    }
}

